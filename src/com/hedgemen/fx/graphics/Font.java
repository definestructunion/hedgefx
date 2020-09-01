package com.hedgemen.fx.graphics;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.system.MemoryUtil;

import static java.awt.Font.MONOSPACED;
import static java.awt.Font.PLAIN;
import static java.awt.Font.TRUETYPE_FONT;

public class Font {
	
	// manually added space, might change because every language uses spaces to some extent
	public static final String Ascii = "\u0000 ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$€-%+=#_&~*\u0080" +
											   "\u0081\u0082\u0083\u0084\u0085\u0086\u0087\u0088\u0089\u008A\u008B\u008C\u008D\u008E\u008F\u0090\u0091" +
											   "\u0092\u0093\u0094\u0095\u0096\u0097\u0098\u0099\u009A\u009B\u009C\u009D\u009E\u009F\u00A0\u00A1\u00A2" +
											   "\u00A3\u00A4\u00A5\u00A6\u00A7\u00A8\u00A9\u00AA\u00AB\u00AC\u00AD\u00AE\u00AF\u00B0\u00B1\u00B2\u00B3" +
											   "\u00B4\u00B5\u00B6\u00B7\u00B8\u00B9\u00BA\u00BB\u00BC\u00BD\u00BE\u00BF\u00C0\u00C1\u00C2\u00C3\u00C4" +
											   "\u00C5\u00C6\u00C7\u00C8\u00C9\u00CA\u00CB\u00CC\u00CD\u00CE\u00CF\u00D0\u00D1\u00D2\u00D3\u00D4\u00D5" +
											   "\u00D6\u00D7\u00D8\u00D9\u00DA\u00DB\u00DC\u00DD\u00DE\u00DF\u00E0\u00E1\u00E2\u00E3\u00E4\u00E5\u00E6" +
											   "\u00E7\u00E8\u00E9\u00EA\u00EB\u00EC\u00ED\u00EE\u00EF\u00F0\u00F1\u00F2\u00F3\u00F4\u00F5\u00F6\u00F7" +
											   "\u00F8\u00F9\u00FA\u00FB\u00FC\u00FD\u00FE\u00FF";
	
	
	
	public static class Glyph {
		public final int width;
		public final int height;
		public final int x;
		public final int y;
		public final float advance;
		
		public Glyph(int width, int height, int x, int y, float advance) {
			this.width = width;
			this.height = height;
			this.x = x;
			this.y = y;
			this.advance = advance;
		}
	}
	
	private final Map<Integer, Glyph> glyphs;
	
	private final Texture texture;
	public Texture getTexture() { return texture; }
	
	private int fontHeight;
	
	public Font() {
		this(new java.awt.Font(MONOSPACED, PLAIN, 16), true);
	}
	
	public Font(boolean antiAlias) {
		this(new java.awt.Font(MONOSPACED, PLAIN, 16), antiAlias);
	}
	
	public Font(int size) {
		this(new java.awt.Font(MONOSPACED, PLAIN, size), true);
	}
	
	public Font(int size, boolean antiAlias) {
		this(new java.awt.Font(MONOSPACED, PLAIN, size), antiAlias);
	}
	
	public Font(InputStream in, int size) throws FontFormatException, IOException {
		this(in, size, true);
	}
	
	public Font(InputStream in, int size, boolean antiAlias) throws FontFormatException, IOException {
		this(java.awt.Font.createFont(TRUETYPE_FONT, in).deriveFont(PLAIN, size), antiAlias);
	}
	
	public Font(java.awt.Font font) {
		this(font, true);
	}
	
	public Font(java.awt.Font font, boolean antiAlias) {
		glyphs = new HashMap<>();
		texture = createFontTexture(font, antiAlias);
	}
	
	private Texture createFontTexture(java.awt.Font font, boolean antiAlias) {
		int imageWidth = 0;
		int imageHeight = 0;
		
		for(int i = 0; i < Ascii.length(); ++i) {
			int c = Ascii.codePointAt(i);
			
			BufferedImage ch = createCharImage(font, c, antiAlias);
			if (ch == null) {
				
				continue;
			}
			
			imageWidth += ch.getWidth();
			imageHeight = Math.max(imageHeight, ch.getHeight());
		}
		
		fontHeight = imageHeight;
		
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		
		int x = 0;

		for(int i = 0; i < Ascii.length(); ++i) {
			
			int c = Ascii.codePointAt(i);
			BufferedImage charImage = createCharImage(font, c, antiAlias);
			if (charImage == null) {
				
				continue;
			}
			
			int charWidth = charImage.getWidth();
			int charHeight = charImage.getHeight();
			
			Glyph ch = new Glyph(charWidth, charHeight, x, image.getHeight() - charHeight, 0f);
			g.drawImage(charImage, x, 0, null);
			x += ch.width;

			glyphs.put(c, ch);
		}
		
		int width = image.getWidth();
		int height = image.getHeight();
		
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = pixels[i * width + j];
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				buffer.put((byte) (pixel & 0xFF));
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		
		buffer.flip();
		
		Texture fontTexture = new Texture(width, height, buffer);
		MemoryUtil.memFree(buffer);
		return fontTexture;
	}
	
	private BufferedImage createCharImage(java.awt.Font font, int c, boolean antiAlias) {
		
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		if (antiAlias) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();
		g.dispose();
		
		
		int charWidth = metrics.charWidth(c);
		int charHeight = metrics.getHeight();
		
		
		if (charWidth == 0) {
			return null;
		}
		
		
		image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		if (antiAlias) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		g.setFont(font);
		g.setPaint(java.awt.Color.WHITE);
		
		int[] codePoint = new int[] { c };
		g.drawString(new String(codePoint, 0, 1), 0, metrics.getAscent());
		
		g.dispose();
		return image;
	}
	
	public int getWidth(String text) {
		int width = 0;
		int lineWidth = 0;
		for (int i = 0; i < text.length(); i++) {

			int c = text.codePointAt(i);
			if (c == '\n') {
				
				width = Math.max(width, lineWidth);
				lineWidth = 0;
				continue;
			}
			if (c == '\r') {
				
				continue;
			}
			Glyph g = glyphs.get(c);

			lineWidth += g.width;
		}
		width = Math.max(width, lineWidth);
		return width;
	}
	
	public int getHeight(String text) {
		int height = 0;
		int lineHeight = 0;
		for (int i = 0; i < text.length(); i++) {

			int c = text.codePointAt(i);
			if (c == '\n') {
				
				height += lineHeight;
				lineHeight = 0;
				continue;
			}
			if (c == '\r') {
				
				continue;
			}
			Glyph g = glyphs.get(c);

			lineHeight = Math.max(lineHeight, g.height);
		}
		height += lineHeight;
		return height;
	}
	
	public Glyph getGlyph(int c) {
		if(!glyphs.containsKey(c)) {
			System.out.println(c);
		}
		return glyphs.get(c);

	}
	
	public void dispose() {
		texture.dispose();
	}
	
}
/*
 * The MIT License (MIT)
 *
 * Copyright © 2015-2017, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package hedge.fx.graphics;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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

/**
 * This class contains a font texture for drawing text.
 *
 * @author Heiko Brumme
 */
public class Font {
	
	public static final String Ascii = "\u0000ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890\"!`?'.,;:()[]{}<>|/@\\^$€-%+=#_&~*\u0080" +
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
		
		/**
		 * Creates a font Glyph.
		 *
		 * @param width   Width of the Glyph
		 * @param height  Height of the Glyph
		 * @param x       X coordinate on the font texture
		 * @param y       Y coordinate on the font texture
		 * @param advance Advance width
		 */
		public Glyph(int width, int height, int x, int y, float advance) {
			this.width = width;
			this.height = height;
			this.x = x;
			this.y = y;
			this.advance = advance;
		}
		
	}
	
	/**
	 * Contains the glyphs for each char.
	 */
	private final Map<Character, Glyph> glyphs;
	/**
	 * Contains the font texture.
	 */
	private final Texture texture;
	public Texture getTexture() { return texture; }
	
	/**
	 * Height of the font.
	 */
	private int fontHeight;
	
	/**
	 * Creates a default antialiased font with monospaced glyphs and default
	 * size 16.
	 */
	public Font() {
		this(new java.awt.Font(MONOSPACED, PLAIN, 16), true);
	}
	
	/**
	 * Creates a default font with monospaced glyphs and default size 16.
	 *
	 * @param antiAlias Wheter the font should be antialiased or not
	 */
	public Font(boolean antiAlias) {
		this(new java.awt.Font(MONOSPACED, PLAIN, 16), antiAlias);
	}
	
	/**
	 * Creates a default antialiased font with monospaced glyphs and specified
	 * size.
	 *
	 * @param size Font size
	 */
	public Font(int size) {
		this(new java.awt.Font(MONOSPACED, PLAIN, size), true);
	}
	
	/**
	 * Creates a default font with monospaced glyphs and specified size.
	 *
	 * @param size      Font size
	 * @param antiAlias Wheter the font should be antialiased or not
	 */
	public Font(int size, boolean antiAlias) {
		this(new java.awt.Font(MONOSPACED, PLAIN, size), antiAlias);
	}
	
	/**
	 * Creates a antialiased Font from an input stream.
	 *
	 * @param in   The input stream
	 * @param size Font size
	 *
	 * @throws FontFormatException if fontFile does not contain the required
	 *                             font tables for the specified format
	 * @throws IOException         If font can't be read
	 */
	public Font(InputStream in, int size) throws FontFormatException, IOException {
		this(in, size, true);
	}
	
	/**
	 * Creates a Font from an input stream.
	 *
	 * @param in        The input stream
	 * @param size      Font size
	 * @param antiAlias Wheter the font should be antialiased or not
	 *
	 * @throws FontFormatException if fontFile does not contain the required
	 *                             font tables for the specified format
	 * @throws IOException         If font can't be read
	 */
	public Font(InputStream in, int size, boolean antiAlias) throws FontFormatException, IOException {
		this(java.awt.Font.createFont(TRUETYPE_FONT, in).deriveFont(PLAIN, size), antiAlias);
	}
	
	/**
	 * Creates a antialiased font from an AWT Font.
	 *
	 * @param font The AWT Font
	 */
	public Font(java.awt.Font font) {
		this(font, true);
	}
	
	/**
	 * Creates a font from an AWT Font.
	 *
	 * @param font      The AWT Font
	 * @param antiAlias Wheter the font should be antialiased or not
	 */
	public Font(java.awt.Font font, boolean antiAlias) {
		glyphs = new HashMap<>();
		texture = createFontTexture(font, antiAlias);
	}
	
	/**
	 * Creates a font texture from specified AWT font.
	 *
	 * @param font      The AWT font
	 * @param antiAlias Wheter the font should be antialiased or not
	 *
	 * @return Font texture
	 */
	private Texture createFontTexture(java.awt.Font font, boolean antiAlias) {
		/* Loop through the characters to get charWidth and charHeight */
		int imageWidth = 0;
		int imageHeight = 0;
		
		/* Start at char #32, because ASCII 0 to 31 are just control codes */
		for (int i = 32; i < 256; i++) {
			if (i == 127) {
				/* ASCII 127 is the DEL control code, so we can skip it */
				continue;
			}
			char c = (char) i;
			BufferedImage ch = createCharImage(font, c, antiAlias);
			if (ch == null) {
				/* If char image is null that font does not contain the char */
				continue;
			}
			
			imageWidth += ch.getWidth();
			imageHeight = Math.max(imageHeight, ch.getHeight());
		}
		
		fontHeight = imageHeight;
		
		/* Image for the texture */
		BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		
		int x = 0;
		
		/* Create image for the standard chars, again we omit ASCII 0 to 31
		 * because they are just control codes */
		for (int i = 32; i < 256; i++) {
			if (i == 127) {
				/* ASCII 127 is the DEL control code, so we can skip it */
				continue;
			}
			char c = (char) i;
			BufferedImage charImage = createCharImage(font, c, antiAlias);
			if (charImage == null) {
				/* If char image is null that font does not contain the char */
				continue;
			}
			
			int charWidth = charImage.getWidth();
			int charHeight = charImage.getHeight();
			
			/* Create glyph and draw char on image */
			Glyph ch = new Glyph(charWidth, charHeight, x, image.getHeight() - charHeight, 0f);
			g.drawImage(charImage, x, 0, null);
			x += ch.width;
			glyphs.put(c, ch);
		}
		
		/* Flip image Horizontal to get the origin to bottom left */
		/*AffineTransform transform = AffineTransform.getScaleInstance(1f, -1f);
		transform.translate(0, -image.getHeight());
		AffineTransformOp operation = new AffineTransformOp(transform,
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		image = operation.filter(image, null);*/
		
		/* Get charWidth and charHeight of image */
		int width = image.getWidth();
		int height = image.getHeight();
		
		System.out.println(width + ", " + height);
		
		/* Get pixel data of image */
		int[] pixels = new int[width * height];
		image.getRGB(0, 0, width, height, pixels, 0, width);
		
		/* Put pixel data into a ByteBuffer */
		ByteBuffer buffer = MemoryUtil.memAlloc(width * height * 4);
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				/* Pixel as RGBA: 0xAARRGGBB */
				int pixel = pixels[i * width + j];
				/* Red component 0xAARRGGBB >> 16 = 0x0000AARR */
				buffer.put((byte) ((pixel >> 16) & 0xFF));
				/* Green component 0xAARRGGBB >> 8 = 0x00AARRGG */
				buffer.put((byte) ((pixel >> 8) & 0xFF));
				/* Blue component 0xAARRGGBB >> 0 = 0xAARRGGBB */
				buffer.put((byte) (pixel & 0xFF));
				/* Alpha component 0xAARRGGBB >> 24 = 0x000000AA */
				buffer.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		/* Do not forget to flip the buffer! */
		buffer.flip();
		
		/* Create texture */
		Texture fontTexture = new Texture(width, height, buffer);
		MemoryUtil.memFree(buffer);
		return fontTexture;
	}
	
	/**
	 * Creates a char image from specified AWT font and char.
	 *
	 * @param font      The AWT font
	 * @param c         The char
	 * @param antiAlias Wheter the char should be antialiased or not
	 *
	 * @return Char image
	 */
	private BufferedImage createCharImage(java.awt.Font font, char c, boolean antiAlias) {
		/* Creating temporary image to extract character size */
		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		if (antiAlias) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setFont(font);
		FontMetrics metrics = g.getFontMetrics();
		g.dispose();
		
		/* Get char charWidth and charHeight */
		int charWidth = metrics.charWidth(c);
		int charHeight = metrics.getHeight();
		
		/* Check if charWidth is 0 */
		if (charWidth == 0) {
			return null;
		}
		
		/* Create image for holding the char */
		image = new BufferedImage(charWidth, charHeight, BufferedImage.TYPE_INT_ARGB);
		g = image.createGraphics();
		if (antiAlias) {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setFont(font);
		g.setPaint(java.awt.Color.WHITE);
		g.drawString(String.valueOf(c), 0, metrics.getAscent());
		g.dispose();
		return image;
	}
	
	/**
	 * Gets the width of the specified text.
	 *
	 * @param text The text
	 *
	 * @return Width of text
	 */
	public int getWidth(CharSequence text) {
		int width = 0;
		int lineWidth = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\n') {
				/* Line end, set width to maximum from line width and stored
				 * width */
				width = Math.max(width, lineWidth);
				lineWidth = 0;
				continue;
			}
			if (c == '\r') {
				/* Carriage return, just skip it */
				continue;
			}
			Glyph g = glyphs.get(c);
			lineWidth += g.width;
		}
		width = Math.max(width, lineWidth);
		return width;
	}
	
	/**
	 * Gets the height of the specified text.
	 *
	 * @param text The text
	 *
	 * @return Height of text
	 */
	public int getHeight(CharSequence text) {
		int height = 0;
		int lineHeight = 0;
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == '\n') {
				/* Line end, add line height to stored height */
				height += lineHeight;
				lineHeight = 0;
				continue;
			}
			if (c == '\r') {
				/* Carriage return, just skip it */
				continue;
			}
			Glyph g = glyphs.get(c);
			lineHeight = Math.max(lineHeight, g.height);
		}
		height += lineHeight;
		return height;
	}
	
	/*public void drawText(Renderer renderer, CharSequence text, float x, float y, Color c) {
		int textHeight = getHeight(text);
		
		float drawX = x;
		float drawY = y;
		if (textHeight > fontHeight) {
			drawY += textHeight - fontHeight;
		}
		
		texture.bind();
		renderer.begin();
		for (int i = 0; i < text.length(); i++) {
			char ch = text.charAt(i);
			if (ch == '\n') {
				drawY -= fontHeight;
				drawX = x;
				continue;
			}
			if (ch == '\r') {
				continue;
			}
			Glyph g = glyphs.get(ch);
			renderer.drawTextureRegion(texture, drawX, drawY, g.x, g.y, g.width, g.height, c);
			drawX += g.width;
		}
		renderer.end();
	}
	
	public void drawText(Renderer renderer, CharSequence text, float x, float y) {
		drawText(renderer, text, x, y, Color.WHITE);
	}*/
	
	/**
	 * Disposes the font.
	 */
	public void dispose() {
		texture.dispose();
	}
	
}
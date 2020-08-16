package hedge.fx.graphics;

import java.util.HashMap;

public class TrueTypeFont {

	/*public Texture fontTexture;
	private HashMap<Character, CharacterRegion> characterRegions;

	private float fontPixelsHeight;
	private float fontToScale;
	private float fontScale;

	private Bitmap[] glyphBitmaps;
	
	public void drawLine(SpriteBatch spriteBatch, CharSequence characters,
			float x, float y) {
		int nCharacters = characters.length();
		for (int i = 0; i < nCharacters; i++) {
			final char ch = characters.charAt(i);
			if (ch == ' ') {
				x += fontPixelsHeight / 2f;
			}
			final CharacterRegion cR = this.characterRegions.get(ch);
			if (cR != null) {
				final float fontToScale = this.fontToScale;
				spriteBatch.draw(this.fontTexture,
						x + cR.xOffset * fontToScale, y - cR.yOffset
								* fontToScale, cR.w * fontToScale, cR.h
								* fontToScale, cR.x, cR.y, cR.w, cR.h, false,
						false);
				x += cR.xAdvance * fontToScale;
			}
		}
	}

	public TrueTypeFont(String fontPath, String characters, float worldWidth,
			float worldHeight, float fontPixelsHeight, int deviceWidth,
			int deviceHeight, int fontPadding, int initialAtlasSize,
			TextureFilter minTextureFilter, TextureFilter magTextureFilter) {

		this.fontPixelsHeight = fontPixelsHeight;
		this.characterRegions = new HashMap<Character, CharacterRegion>();

		StbTrueTypeFont stbTrueTypeFont = new StbTrueTypeFont(
				Gdx.files.internal(fontPath));

		this.generateFontScales(stbTrueTypeFont, worldWidth, worldHeight,
				fontPixelsHeight, deviceWidth, deviceHeight);

		this.generateGlyphs(stbTrueTypeFont, characters, this.fontScale,
				this.fontScale);

		stbTrueTypeFont.dispose();

		this.generateFontTexture(this.glyphBitmaps, characters, fontPadding,
				initialAtlasSize, minTextureFilter, magTextureFilter);
	}

	@Override
	public void dispose() {
		this.fontTexture.dispose();
		this.characterRegions = null;
	}

	private void generateFontScales(StbTrueTypeFont stbTrueTypeFont,
			float worldWidth, float worldHeight, float fontPixelsHeight,
			int deviceWidth, int deviceHeight) {

		float worldAR = worldWidth / worldHeight;
		float realAR = (float) deviceWidth / (float) deviceHeight;

		float fontSizeDevice;
		if (realAR <= worldAR) {
			fontSizeDevice = deviceWidth * fontPixelsHeight / worldWidth;
		} else {
			fontSizeDevice = deviceHeight * fontPixelsHeight / worldHeight;
		}

		this.fontToScale = fontPixelsHeight / fontSizeDevice;
		this.fontScale = stbTrueTypeFont.scaleForPixelHeight(fontSizeDevice);
	}

	private void generateGlyphs(StbTrueTypeFont stbTrueTypeFont,
			String fontCharacters, float fontScaleW, float fontScaleH) {

		char[] characters = fontCharacters.toCharArray();
		int nCharacters = characters.length;

		this.glyphBitmaps = new Bitmap[nCharacters];

		for (int i = 0; i < nCharacters; i++) {
			int glyphCode = stbTrueTypeFont.findGlyphIndex(characters[i]);
			if (glyphCode == 0) {
				continue;
			}
			this.glyphBitmaps[i] = stbTrueTypeFont.makeGlyphBitmap(fontScaleW,
					fontScaleH, 0, 0, glyphCode);
		}
	}

	private void generateFontTexture(Bitmap[] glyphBitmaps,
			String fontCharacters, int padding, int atlasSize,
			TextureFilter minFilter, TextureFilter magFilter) {

		char[] characters = fontCharacters.toCharArray();
		int nCharacters = characters.length;

		int atlasSizeW = atlasSize;
		int atlasSizeH = atlasSize;

		PTNode root = new PTNode();
		root.rL = 0;
		root.rT = 0;
		root.rR = atlasSizeW;
		root.rB = atlasSizeH;

		for (int imageID = 0; imageID < nCharacters; imageID++) {
			final Bitmap glyphBitmap = glyphBitmaps[imageID];

			if (glyphBitmap == null) {
				continue;
			}

			int glyphWidth = glyphBitmap.pixmap.getWidth() + padding * 2;
			int glyphHeight = glyphBitmap.pixmap.getHeight() + padding * 2;

			int[] glyphPos = root.insertImage(imageID, glyphWidth, glyphHeight);

			if (glyphPos == null) {
				// hmm atlasSize too small =( restarting xD
				atlasSize *= 2;
				root = new PTNode();
				root.rL = 0;
				root.rT = 0;

				if ((minFilter == TextureFilter.Linear || minFilter == TextureFilter.Nearest)
						&& (magFilter == TextureFilter.Linear || magFilter == TextureFilter.Nearest)) {
					if (atlasSizeH > atlasSizeW) {
						atlasSizeW *= 2;
					} else {
						atlasSizeH *= 2;
					}
				} else {
					atlasSizeH *= 2;
					atlasSizeW *= 2;
				}
				root.rR = atlasSizeW;
				root.rB = atlasSizeH;

				imageID = -1;
				continue;
			}

			final Box glyphBox = glyphBitmap.box;

			CharacterRegion cR = new CharacterRegion(glyphPos[0], glyphPos[1],
					glyphWidth, glyphHeight, glyphBox.x0, glyphBox.y1,
					glyphBox.x1);

			this.characterRegions.put(characters[imageID], cR);
		}

		this.fontTexture = new Texture(root.getTextureWidth(),
				root.getTextureHeight(), Format.RGBA4444);
		this.fontTexture.setFilter(minFilter, magFilter);

		for (Character KEY : this.characterRegions.keySet()) {
			for (int i = 0; i < nCharacters; i++) {
				if (characters[i] == KEY) {
					CharacterRegion cR = this.characterRegions.get(KEY);
					this.fontTexture.draw(glyphBitmaps[i].pixmap, cR.x
							+ padding, cR.y + padding);
					break;
				}
			}
		}

		for (int i = 0; i < glyphBitmaps.length; i++) {
			if (glyphBitmaps[i] != null) {
				glyphBitmaps[i].dispose();
			}
		}
	}

	public class CharacterRegion {
		// SPRITE VALUES
		public final int x;
		public final int y;
		public final int w;
		public final int h;

		// BOX VALUES
		public final int xOffset;
		public final int yOffset;
		public final int xAdvance;

		public CharacterRegion(int x, int y, int w, int h, int xOffset,
				int yOffset, int xAdvance) {
			super();
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.xOffset = xOffset;
			this.yOffset = yOffset;
			this.xAdvance = xAdvance;
		}
	}

	public static class PTNode {
		public int imageID = -1;
		public boolean leaf = true;
		public PTNode childA;
		public PTNode childB;
		public int rL;
		public int rT;
		public int rR;
		public int rB;

		public int[] insertImage(int imageID, int imageWidth, int imageHeight) {
			PTNode node = insertImageRecursive(this, imageID, imageWidth,
					imageHeight);
			// if there was no space, increase texture size =P
			if (node == null) {
				return null;
			}

			int[] pos = new int[2];
			pos[0] = node.rL;
			pos[1] = node.rT;
			return pos;
		}

		public int getTextureWidth() {
			return this.rR;
		}

		public int getTextureHeight() {
			return this.rB;
		}

		private static PTNode insertImageRecursive(PTNode node, int imageID,
				int imageWidth, int imageHeight) {
			if (!node.leaf) {
				PTNode newNode = insertImageRecursive(node.childA, imageID,
						imageWidth, imageHeight);
				if (newNode != null) {
					return newNode;
				}

				return insertImageRecursive(node.childB, imageID, imageWidth,
						imageHeight);
			}
			// its a leaf =)
			// if there is an image here already!
			if (node.imageID != -1) {
				return null;
			}

			final int rWidth = node.rR - node.rL;
			final int rHeight = node.rB - node.rT;

			// if space is too small for the image -_-
			if (rWidth < imageWidth || rHeight < imageHeight) {
				return null;
			}

			// if it fits perfectly, thats the spot <3
			if (rWidth == imageWidth && rHeight == imageHeight) {
				node.leaf = true;
				node.imageID = imageID;
				return node;
			}

			// if none of the above, split the rectangular xD
			PTNode childA = new PTNode();
			PTNode childB = new PTNode();

			// decide which way to split
			int dW = rWidth - imageWidth;
			int dH = rHeight - imageHeight;

			if (dW > dH) {
				childA.rL = node.rL;
				childA.rT = node.rT;
				childA.rR = node.rL + imageWidth;
				childA.rB = node.rB;

				childB.rL = node.rL + imageWidth;
				childB.rT = node.rT;
				childB.rR = node.rR;
				childB.rB = node.rB;
			} else {
				childA.rL = node.rL;
				childA.rT = node.rT;
				childA.rR = node.rR;
				childA.rB = node.rT + imageHeight;

				childB.rL = node.rL;
				childB.rT = node.rT + imageHeight;
				childB.rR = node.rR;
				childB.rB = node.rB;
			}

			node.leaf = false;
			node.childA = childA;
			node.childB = childB;

			return insertImageRecursive(node.childA, imageID, imageWidth,
					imageHeight);
		}
	}*/
}

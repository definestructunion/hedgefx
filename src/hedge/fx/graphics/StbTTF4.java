package hedge.fx.graphics;


public class StbTTF4 {

	public static final float WORLD_WIDTH = 12.5f;
	public static final float WORLD_HEIGHT = 7.5f;

	public static final float FONT_SIZE = 0.7f;
	public static final int FONT_PADDING = 1;
	public static final int FONT_ATLASSIZE = 32;
	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
	public static final String FONT_PATH = "data/DroidSerif-Regular.ttf";

	private TrueTypeFont fontAtlasDroid;
	private SpriteBatch spriteBatch;

	private long start = System.currentTimeMillis();
	private String text = "Hello world! %#@|\\/?-+=()*&.;,{}";

	private TrueTypeFont fontAtlasKatakana;

	public static final String FONT_KATAKANA = "アイウエオ";
	public static final String FONT_KATAKANA_PATH = "data/cinecaption227.TTF";
	private String textKatakana = "アイウエオ";

	//private Texture stone;

	//@Override
	public void resize(int width, int height) {
		//Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		float worldAR = WORLD_WIDTH / WORLD_HEIGHT;
		float realAR = (float) width / (float) height;

		float newW;
		float newH;

		if (realAR <= worldAR) {
			newW = width;
			newH = (float) width / worldAR;
			float diff = height - newH;
			//Gdx.gl.glViewport(0, (int) (diff / 2f), width, (int) newH);
		} else {
			newH = height;
			newW = worldAR * ((float) height);
			float diff = width - newW;
			//Gdx.gl.glViewport((int) (diff / 2f), 0, (int) newW, height);
		}

		//this.deviceProjection = new Matrix4().setToOrtho2D(0, 0, newW, newH);

		long start = System.currentTimeMillis();
		if (fontAtlasDroid != null) {
			//fontAtlasDroid.dispose();
		}
		/*fontAtlasDroid = new TrueTypeFont(FONT_PATH, FONT_CHARACTERS,
				WORLD_WIDTH, WORLD_HEIGHT, FONT_SIZE, (int) newW, (int) newH,
				FONT_PADDING, FONT_ATLASSIZE, TextureFilter.Nearest,
				TextureFilter.Nearest);*/
		//Gdx.app.log("FONTGENERATION", "Took: "
		//		+ (System.currentTimeMillis() - start) + "ms.");

		if (fontAtlasKatakana != null) {
			//fontAtlasKatakana.dispose();
		}
		/*fontAtlasKatakana = new TrueTypeFont(FONT_KATAKANA_PATH, FONT_KATAKANA,
				WORLD_WIDTH, WORLD_HEIGHT, FONT_SIZE * 2, (int) newW,
				(int) newH, FONT_PADDING, FONT_ATLASSIZE,
				TextureFilter.Nearest, TextureFilter.Nearest);*/
	}
}
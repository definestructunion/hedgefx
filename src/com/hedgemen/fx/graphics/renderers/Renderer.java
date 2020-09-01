package com.hedgemen.fx.graphics.renderers;

import com.hedgemen.fx.util.Disposable;
import com.hedgemen.fx.Rectangle;
import com.hedgemen.fx.graphics.Color;
import com.hedgemen.fx.graphics.Font;
import com.hedgemen.fx.graphics.Texture;
import com.hedgemen.fx.math.Vector2;

public interface Renderer extends Disposable {
	
	void begin();
	void end();
	
	void draw(Texture texture, float x, float y, Color color);
	void draw(Texture texture, float x, float y, float width, float height, Color color);
	void draw(Texture texture, Vector2 position, Color color);
	void draw(Texture texture, Vector2 position, Vector2 size, Color color);
	void draw(Texture texture, Rectangle dimensions, Color color);
	void draw(Texture texture, Rectangle dimensions, Rectangle textureCoords, Color color);
	void draw(Texture texture, Rectangle dimensions, Rectangle textureCoords, Color color, float rotation, float originX, float originY);
	void draw(Texture texture, Rectangle dimensions, Rectangle textureCoords, Color color, float rotation, Vector2 origin);
	
	void drawString(Font font, String message, float x, float y, Color color);
	void drawString(Font font, String message, Vector2 position, Color color);
	void drawString(Font font, String message, float x, float y, Color color, float rotation, float originX, float originY);
	void drawString(Font font, String message, Vector2 position, Color color, float rotation, Vector2 origin);
	void drawString(Font font, String message, Vector2 position, float scale, Color color, float rotation, Vector2 origin);
	
	void draw(Texture texture, float x, float y, float w, float h, float rx, float ry, float rw, float rh,
			  Color color, float rotation, float originX, float originY);
	void drawString(Font font, String message, float x, float y, float scale, Color color, float rotation, float originX, float originY);
	
	boolean canDrawMore();
}

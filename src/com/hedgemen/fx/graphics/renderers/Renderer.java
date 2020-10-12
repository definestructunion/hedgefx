package com.hedgemen.fx.graphics.renderers;

import com.hedgemen.fx.util.Disposable;
import com.hedgemen.fx.Rectangle;
import com.hedgemen.fx.graphics.Color;
import com.hedgemen.fx.graphics.Font;
import com.hedgemen.fx.graphics.Texture;
import com.hedgemen.fx.math.Vector2;
import org.jetbrains.annotations.Nullable;

public interface Renderer extends Disposable {
	
	void begin();
	void end();
	boolean canDrawMore();
	
	void draw(Texture texture, Rectangle dimensions, @Nullable Rectangle textureCoords, @Nullable Color color, float rotation, @Nullable Vector2 origin);
	void draw(Texture texture, float x, float y, float w, float h, float u0, float v0, float u1, float v1,
			  Color color, float rotation, float originX, float originY);
	
	void drawString(Font font, String message, Vector2 position, float scale, @Nullable Color color, float rotation, @Nullable Vector2 origin);
	void drawString(Font font, String message, float x, float y, float scale, Color color, float rotation, float originX, float originY);
}

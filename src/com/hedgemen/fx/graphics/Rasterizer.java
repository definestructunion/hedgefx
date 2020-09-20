package com.hedgemen.fx.graphics;

import com.hedgemen.fx.Rectangle;

public final class Rasterizer {
	
	private Rectangle scissor;
	public Rectangle getScissor() { return scissor; }
	public void setScissor(Rectangle value) { scissor = value; }
	
	public Rasterizer(GraphicsDevice graphicsDevice) {
		scissor = new Rectangle(0, 0, graphicsDevice.getBackBufferWidth(), graphicsDevice.getBackBufferHeight());
	}
}

package com.hedgemen.fx;

import com.hedgemen.fx.util.copying.Copy;

public class Rectangle implements Copy<Rectangle> {
	
	public float x, y, width, height;
	
	public Rectangle() {
		this(0, 0, 0, 0);
	}
	
	public Rectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public Rectangle deepCopy() {
		return new Rectangle(x, y, width, height);
	}
	
	@Override
	public Rectangle shallowCopy() {
		return new Rectangle(x, y, width, height);
	}
}

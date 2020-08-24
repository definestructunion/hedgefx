package com.hgm.fx;

import com.hgm.fx.util.copying.Copy;

public class Rectangle implements Copy<Rectangle> {
	
	public int x, y, width, height;
	
	public Rectangle() {
		this(0, 0, 0, 0);
	}
	
	public Rectangle(int x, int y, int width, int height) {
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

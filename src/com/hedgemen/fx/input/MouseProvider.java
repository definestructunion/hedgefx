package com.hedgemen.fx.input;

import com.hedgemen.fx.graphics.GraphicsDeviceListener;

public interface MouseProvider extends GraphicsDeviceListener {
	
	float cursorPosX();
	float cursorPosY();
	
	void update();
}

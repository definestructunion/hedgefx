package com.hedgemen.fx.input;

import com.hedgemen.fx.graphics.GraphicsDeviceListener;

public interface MouseProvider extends GraphicsDeviceListener {
	
	boolean isButtonDown(Buttons button);
	boolean isButtonPressed(Buttons button);
	
	boolean isCapsLockOn();
	boolean numLockOn();
	
	boolean anyButtonDown();
	boolean anyButtonPressed();
	
	boolean anyButtonsDown(Buttons[] buttons);
	boolean anyButtonsPressed(Buttons[] buttons);
	
	float cursorPosX();
	float cursorPosY();
	
	void update();
}

package com.hedgemen.fx.input;

public interface InputHandler {

	boolean isKeyDown(Keys key);
	boolean isKeyPressed(Keys key);
	
	boolean isButtonDown(Buttons button);
	boolean isButtonPressed(Buttons button);
	
	boolean capsLockOn();
	boolean numLockOn();
	
	boolean anyKeyDown();
	boolean anyKeyPressed();
	
	boolean anyButtonDown();
	boolean anyButtonPressed();
	
	boolean anyKeysDown(Keys[] keys);
	boolean anyKeysPressed(Keys[] keys);
	
	boolean anyButtonsDown(Buttons[] buttons);
	boolean anyButtonsPressed(Buttons[] buttons);
}

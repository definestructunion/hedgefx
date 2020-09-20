package com.hedgemen.fx.input;

public interface InputHandler extends KeyboardProvider, MouseProvider {

	boolean isKeyDown(Keys key);
	boolean isKeyPressed(Keys key);
	
	boolean isButtonDown(Buttons button);
	boolean isButtonPressed(Buttons button);
	
	boolean isCapsLockOn();
	boolean numLockOn();
	
	boolean anyKeyDown();
	boolean anyKeyPressed();
	
	boolean anyButtonDown();
	boolean anyButtonPressed();
	
	boolean anyKeysDown(Keys[] keys);
	boolean anyKeysPressed(Keys[] keys);
	
	boolean anyButtonsDown(Buttons[] buttons);
	boolean anyButtonsPressed(Buttons[] buttons);
	
	String getTypedChars();
}

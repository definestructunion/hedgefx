package com.hedgemen.fx.input;

import com.hedgemen.fx.graphics.GraphicsDeviceListener;

import java.util.List;

public interface KeyboardProvider extends GraphicsDeviceListener {
	
	boolean isKeyDown(Keys key);
	boolean isKeyPressed(Keys key);
	
	boolean anyKeysDown(List<Keys> keys);
	boolean anyKeysPressed(List<Keys> keys);
	
	boolean anyKeysDown(Keys[] keys);
	boolean anyKeysPressed(Keys[] keys);
	
	boolean anyButtonsDown(List<Buttons> buttons);
	boolean anyButtonsPressed(List<Buttons> buttons);
	
	boolean anyKeyDown();
	boolean anyKeyPressed();
	
	List<Keys> getKeysDown();
	List<Keys> getKeysPressed();
	
	String getTypedChars();
	
	void update();
}

package com.hedgemen.fx.input;

import com.hedgemen.fx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class DefaultInputHandler implements InputHandler {
	
	private Vector2 cursorPosition;
	
	private boolean[] frameKeys;
	private boolean[] pressedKeys;
	private boolean[] downKeys;
	
	private boolean[] frameButtons;
	private boolean[] pressedButtons;
	private boolean[] downButtons;
	
	private StringBuilder typedCharacters;
	
	public DefaultInputHandler() {
		cursorPosition = new Vector2(0, 0);
		typedCharacters = new StringBuilder();
		
		int keysLength = Keys.values().length;
		int buttonsLength = Buttons.values().length;
		
		frameKeys = new boolean[keysLength];
		pressedKeys = new boolean[keysLength];
		downKeys = new boolean[keysLength];
		
		frameButtons = new boolean[buttonsLength];
		pressedButtons = new boolean[buttonsLength];
		downButtons = new boolean[buttonsLength];
		
		update();
	}
	
	@Override
	public boolean isKeyDown(Keys key) {
		return downKeys[key.ordinal()];
	}
	
	@Override
	public boolean isKeyPressed(Keys key) {
		return pressedKeys[key.ordinal()];
	}
	
	@Override
	public boolean isButtonDown(Buttons button) {
		return downButtons[button.ordinal()];
	}
	
	@Override
	public List<Keys> getKeysDown() {
		var keysDown = new ArrayList<Keys>(4);
		
		for(int i = 0; i < frameKeys.length; ++i) {
			var key = Keys.fromOrdinal(i);
			var fired = isKeyDown(key);
			if(fired) keysDown.add(key);
		}
		
		return keysDown;
	}
	
	@Override
	public List<Keys> getKeysPressed() {
		var keysPressed = new ArrayList<Keys>(4);
		
		for(int i = 0; i < frameKeys.length; ++i) {
			var key = Keys.fromOrdinal(i);
			var fired = isKeyPressed(key);
			if(fired) keysPressed.add(key);
		}
		
		return keysPressed;
	}
	
	@Override
	public boolean isButtonPressed(Buttons button) {
		return pressedButtons[button.ordinal()];
	}
	
	@Override
	public boolean isCapsLockOn() {
		return false;
	}
	
	@Override
	public boolean numLockOn() {
		return false;
	}
	
	@Override
	public boolean anyKeyDown() {
		return false;
	}
	
	@Override
	public boolean anyKeyPressed() {
		return false;
	}
	
	@Override
	public boolean anyButtonDown() {
		return false;
	}
	
	@Override
	public boolean anyButtonPressed() {
		return false;
	}
	
	@Override
	public String getTypedChars() {
		String characters = typedCharacters.toString();
		return characters;
	}
	
	@Override
	public float cursorPosX() {
		return cursorPosition.x;
	}
	
	@Override
	public float cursorPosY() {
		return cursorPosition.y;
	}
	
	@Override
	public void update() {
		
		for(int i = 0; i < frameKeys.length; ++i) {
			pressedKeys[i] = frameKeys[i] && !downKeys[i];
		}
		
		System.arraycopy(frameKeys, 0, downKeys, 0, frameKeys.length);
		
		for(int i = 0; i < frameButtons.length; ++i) {
			pressedButtons[i] = frameButtons[i] && !downButtons[i];
		}
		
		System.arraycopy(frameButtons, 0, downButtons, 0, frameButtons.length);
		typedCharacters.delete(0, typedCharacters.length());
	}
	
	@Override
	public boolean anyKeysDown(Keys[] keys) {
		return false;
	}
	
	@Override
	public boolean anyKeysPressed(Keys[] keys) {
		return false;
	}
	
	@Override
	public boolean anyButtonsDown(Buttons[] buttons) {
		return false;
	}
	
	@Override
	public boolean anyButtonsPressed(Buttons[] buttons) {
		return false;
	}
	
	@Override
	public boolean anyKeysDown(List<Keys> keys) {
		return false;
	}
	
	@Override
	public boolean anyKeysPressed(List<Keys> keys) {
		return false;
	}
	
	@Override
	public boolean anyButtonsDown(List<Buttons> buttons) {
		return false;
	}
	
	@Override
	public boolean anyButtonsPressed(List<Buttons> buttons) {
		return false;
	}
	
	@Override
	public void onKeyCallback(int key, int scanCode, int action, int mods) {
		if(key == Keys.Unknown.value) return;
		Keys keys = Keys.fromValue(key);
		frameKeys[keys.ordinal()] = (action == GLFW_PRESS);
	}
	
	@Override
	public void onButtonCallback(int button, int action, int mods) {
		frameButtons[button] = (action == GLFW_PRESS);
	}
	
	@Override
	public void onCursorPosCallback(double x, double y) {
		cursorPosition.x = (float)x;
		cursorPosition.y = (float)y;
	}
	
	@Override
	public void onScrollWheelCallback(double xOffset, double yOffset) {
	
	}
	
	@Override
	public void onWindowSizeCallback(int width, int height) {
	
	}
	
	@Override
	public void onWindowMinimizedCallback() {
	
	}
	
	@Override
	public void onWindowMaximizedCallback() {
	
	}
	
	@Override
	public void onCharCallback(int codePoint) {
		typedCharacters.append(Character.toChars(codePoint));
	}
}
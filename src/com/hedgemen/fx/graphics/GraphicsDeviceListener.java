package com.hedgemen.fx.graphics;

public interface GraphicsDeviceListener {

	void onKeyCallback(int key, int scanCode, int action, int mods);
	void onButtonCallback(int button, int action, int mods);
	void onCursorPosCallback(double x, double y);
	void onScrollWheelCallback(double xOffset, double yOffset);
	void onWindowSizeCallback(int width, int height);
	void onWindowMinimizedCallback();
	void onWindowMaximizedCallback();
	void onCharCallback(int codePoint);
}

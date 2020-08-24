package com.hgm.fx.app;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hgm.fx.io.files.FileHandle;
import com.hgm.fx.io.serialization.json.JsonConvert;
import com.hgm.fx.platform.GraphicsAPI;
import com.hgm.fx.platform.DisplayMode;

public final class GameConfiguration {
	
	public static final String DefaultTitle = "";
	public static final DisplayMode DefaultDisplayMode = DisplayMode.Windowed;
	public static final int DefaultBackBufferWidth = 960;
	public static final int DefaultBackBufferHeight = 540;
	public static final boolean DefaultUseVSync = true;
	public static final GraphicsAPI DefaultGraphicsAPI = GraphicsAPI.OpenGL;
	
	@JsonIgnore
	private String title;
	public String getTitle() { return title; }
	public GameConfiguration setTitle(String value) { title = value; return this; }
	
	private DisplayMode display;
	public DisplayMode getDisplay() { return display; }
	public GameConfiguration setDisplay(DisplayMode value) { display = value; return this; }
	
	private int backBufferWidth;
	public int getBackBufferWidth() { return backBufferWidth; }
	public GameConfiguration setBackBufferWidth(int value) { backBufferWidth = value; return this; }
	
	private int backBufferHeight;
	public int getBackBufferHeight() { return backBufferHeight; }
	public GameConfiguration setBackBufferHeight(int value) { backBufferHeight = value; return this; }
	
	private boolean useVSync;
	public boolean useVSync() { return useVSync; }
	public GameConfiguration setUseVSync(boolean value) { useVSync = value; return this; }
	
	private GraphicsAPI preferredBackend;
	public GraphicsAPI getPreferredBackend() { return preferredBackend; }
	public GameConfiguration setPreferredBackend(GraphicsAPI value) { preferredBackend = value; return this; }
	
	public static GameConfiguration fromFile(FileHandle file) {
		return JsonConvert.deserialize(file, GameConfiguration.class);
	}
	
	public GameConfiguration() {
		title = DefaultTitle;
		display = DefaultDisplayMode;
		backBufferWidth = DefaultBackBufferWidth;
		backBufferHeight = DefaultBackBufferHeight;
		useVSync = DefaultUseVSync;
		preferredBackend = DefaultGraphicsAPI;
	}
}

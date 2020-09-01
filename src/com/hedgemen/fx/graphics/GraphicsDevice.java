package com.hedgemen.fx.graphics;

import com.hedgemen.fx.app.GameInitializer;
import com.hedgemen.fx.platform.DisplayMode;
import com.hedgemen.fx.platform.GraphicsAPI;
import com.hedgemen.fx.platform.PlatformAPI;
import com.hedgemen.fx.util.NativeResource;
import org.lwjgl.bgfx.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.bgfx.BGFX.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWNativeWin32.*;
import static org.lwjgl.glfw.GLFWNativeCocoa.*;
import static org.lwjgl.glfw.GLFWNativeX11.*;

public class GraphicsDevice implements NativeResource {
	
	private long handle;
	public long getHandle() { return handle; }
	
	private String title;
	public String getTitle() { return title; }
	public void setTitle(String value) { title = value; }
	
	private DisplayMode display;
	public DisplayMode getDisplay() { return display; }
	public void setDisplay(DisplayMode value) { display = value; }
	
	private int backBufferWidth;
	public int getBackBufferWidth() { return backBufferWidth; }
	public void setBackBufferWidth(int value) { backBufferWidth = value; }
	
	private int backBufferHeight;
	public int getBackBufferHeight() { return backBufferHeight; }
	public void setBackBufferHeight(int value) { backBufferHeight = value; }
	
	private boolean useVSync;
	public boolean useVSync() { return useVSync; }
	public void setUseVSync(boolean value) { useVSync = value; }
	
	private GraphicsAPI api;
	public GraphicsAPI getApi() { return api; }
	
	private Color clearColor;
	public void setClearColor(Color value) { clearColor = value; }
	
	private Encoder encoder;
	public Encoder getEncoder() { return encoder; }
	
	private List<GraphicsDeviceListener> listeners;
	
	public GraphicsDevice(GameInitializer initializer) {
		var config = initializer.getConfig();
		this.listeners = new ArrayList<>(1);
		this.title = config.getTitle();
		this.display = config.getDisplay();
		this.backBufferWidth = config.getBackBufferWidth();
		this.backBufferHeight = config.getBackBufferHeight();
		this.useVSync = config.useVSync();
		this.api = config.getPreferredBackend();
		clearColor = new Color(0x000000FF);
		initialize(initializer);
	}
	
	private void initialize(GameInitializer initializer) {
		
		glfwSetErrorCallback(this::errorCallback);
		
		if(!glfwInit()) {
			throw new IllegalStateException("GLFW failed to initialize");
		}
		
		long monitor = glfwGetPrimaryMonitor();
		GLFWVidMode mode = glfwGetVideoMode(monitor);
		
		assert mode != null;
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CLIENT_API, GLFW_NO_API);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_RED_BITS, mode.redBits());
		glfwWindowHint(GLFW_GREEN_BITS, mode.greenBits());
		glfwWindowHint(GLFW_BLUE_BITS, mode.blueBits());
		glfwWindowHint(GLFW_ALPHA_BITS, GLFW_DONT_CARE);
		glfwWindowHint(GLFW_STENCIL_BITS, 8);
		glfwWindowHint(GLFW_REFRESH_RATE, GLFW_DONT_CARE);
		glfwWindowHint(GLFW_DECORATED, display == DisplayMode.Borderless ? GLFW_FALSE : GLFW_TRUE);
		
		handle = 0;
		if(display == DisplayMode.Fullscreen) {
			boolean nativeResolution = backBufferWidth <= 0 && backBufferHeight <= 0;
			backBufferWidth = nativeResolution ? mode.width() : backBufferWidth;
			backBufferHeight = nativeResolution ? mode.height() : backBufferHeight;
			handle = glfwCreateWindow(backBufferWidth, backBufferHeight, title, monitor, 0);
		} else {
			handle = glfwCreateWindow(backBufferWidth, backBufferHeight, title, 0, 0);
			
			if(handle == 0) {
				throw new IllegalStateException("Window failed to create");
			}
			
			glfwSetWindowPos(handle, (mode.width() - backBufferWidth) / 2, (mode.height() - backBufferHeight) / 2);
		}
		
		if(handle == 0) {
			throw new IllegalStateException("Window failed to create");
		}
		
		var stack = MemoryStack.stackPush();
		
		var platformData = BGFXPlatformData.callocStack(stack);
		
		switch(initializer.getPlatform()) {
			case Windows:
				platformData.ndt(0);
				platformData.nwh(glfwGetWin32Window(handle));
				break;
			case MacOS:
				platformData.ndt(0);
				platformData.nwh(glfwGetCocoaWindow(handle));
				break;
			case Linux:
				platformData.ndt(glfwGetX11Display());
				platformData.nwh(glfwGetX11Window(handle));
				break;
			case Unix:
				platformData.ndt(glfwGetX11Display());
				platformData.nwh(glfwGetX11Window(handle));
				break;
		}
		
		glfwSetKeyCallback(handle, this::onKeyCallback);
		glfwSetMouseButtonCallback(handle, this::onButtonCallback);
		glfwSetScrollCallback(handle, this::onScrollWheelCallback);
		glfwSetCursorPosCallback(handle, this::onCursorPosCallback);
		glfwSetWindowSizeCallback(handle, this::onWindowSizeCallback);
		glfwSetWindowIconifyCallback(handle, this::onWindowIconifiedCallback);
		glfwSetCharCallback(handle, this::onCharCallback);
		//glfwSetWindowUserPointer(handle, );
		
		var initLimitsData = BGFXInitLimits.callocStack(stack);
		initLimitsData.maxEncoders((short)1);
		
		var resolutionData = BGFXResolution.callocStack(stack);
		resolutionData.reset(useVSync ? BGFX_RESET_VSYNC : BGFX_RESET_NONE);
		resolutionData.width(backBufferWidth);
		resolutionData.height(backBufferHeight);
		resolutionData.format(BGFX_TEXTURE_FORMAT_RGBA8);
		
		var initData = BGFXInit.callocStack(stack);
		bgfx_init_ctor(initData);
		initData.platformData(platformData);
		initData.resolution(resolutionData);
		//initData.limits(initLimitsData);
		initData.limits().transientVbSize(0xFFFFFFF);
		initData.limits().transientIbSize(0xFFFFFFF);
		initData.limits().maxEncoders((short)1);
		initData.type(initializer.getConfig().getPreferredBackend().value);
		initData.vendorId(BGFX_PCI_ID_NONE);
		initData.deviceId((short)0);
		initData.allocator(null);
		initData.callback(null);
		
		if(!bgfx_init(initData)) {
			throw new IllegalStateException("BGFX failed to initialize");
		}
		
		var specification = new EncoderSpecification(false, BGFX_STATE_WRITE_RGB | BGFX_STATE_WRITE_A | BGFX_STATE_BLEND_ALPHA);
		encoder = new Encoder(specification, this);
		
		BGFXCaps caps = bgfx_get_caps();
		long supported = caps.supported();
		System.out.println(supported);
		
		System.out.println("Texture 2D Arrays: " + (getBit((int)supported, (int)BGFX_CAPS_TEXTURE_2D_ARRAY)));
		
		stack.close();
		
		applyChanges();
		listeners.add(createDefaultListener());
	}
	
	int getBit(int n, int k) {
		return (n >> k) & 1;
	}
	
	public void frame() {
		encoder.begin();
		encoder.end();
		bgfx_frame(false);
	}
	
	public void show() {
		glfwShowWindow(handle);
	}
	
	public void hide() {
		glfwHideWindow(handle);
	}
	
	public void pollEvents() {
		glfwPollEvents();
	}
	
	public boolean isRunning() {
		return !glfwWindowShouldClose(handle);
	}
	
	public void applyChanges() {
		bgfx_set_view_clear(0, BGFX_CLEAR_COLOR | BGFX_CLEAR_DEPTH, clearColor.hexInt(), 1.0f, 0);
		bgfx_set_view_rect(0, 0, 0, backBufferWidth, backBufferHeight);
	}
	
	@Override
	public void dispose() {
		glfwDestroyWindow(handle);
		glfwTerminate();
		bgfx_shutdown();
	}
	
	private GraphicsDeviceListener createDefaultListener() {
		return new GraphicsDeviceListener() {
			@Override
			public void onKeyCallback(int key, int scanCode, int action, int mods) {
			
			}
			
			@Override
			public void onButtonCallback(int button, int action, int mods) {
			
			}
			
			@Override
			public void onCursorPosCallback(double x, double y) {
			
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
			
			}
		};
	}
	
	private void errorCallback(int error, long description) {
	
	}
	
	private void onKeyCallback(long window, int key, int scanCode, int action, int mods) {
		for(var listener : listeners) {
			listener.onKeyCallback(key, scanCode, action, mods);
		}
	}
	
	private void onButtonCallback(long window, int button, int action, int mods) {
		for(var listener : listeners) {
			listener.onButtonCallback(button, action, mods);
		}
	}
	
	private void onCursorPosCallback(long window, double x, double y) {
		for(var listener : listeners) {
			listener.onCursorPosCallback(x, y);
		}
	}
	
	private void onScrollWheelCallback(long window, double xOffset, double yOffset) {
		for(var listener : listeners) {
			listener.onScrollWheelCallback(xOffset, yOffset);
		}
	}
	
	private void onWindowSizeCallback(long window, int width, int height) {
		for(var listener : listeners) {
			listener.onWindowSizeCallback(width, height);
		}
	}
	
	private void onWindowIconifiedCallback(long window, boolean iconified) {
		for(var listener : listeners) {
			if(iconified) listener.onWindowMinimizedCallback();
			else listener.onWindowMaximizedCallback();
		}
	}
	
	private void onCharCallback(long window, int codePoint) {
		for(var listener : listeners) {
			listener.onCharCallback(codePoint);
		}
	}
}
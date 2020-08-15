package hedge.fx.graphics;

import hedge.fx.app.GameInitializer;
import hedge.fx.platform.GraphicsAPI;
import hedge.fx.platform.DisplayMode;
import hedge.fx.util.NativeResource;
import org.lwjgl.bgfx.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

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
	
	public GraphicsDevice(GameInitializer initializer) {
		var config = initializer.getConfig();
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
				platformData.ndt(glfwGetX11Display());
				platformData.nwh(glfwGetX11Window(handle));
				break;
			case Linux:
				platformData.ndt(0);
				platformData.nwh(glfwGetCocoaWindow(handle));
				break;
		}
		
		/*glfwSetKeyCallback(win->m_GLFWHandle, glfw_keyCallback);
		glfwSetCursorPosCallback(win->m_GLFWHandle, glfw_cursorCallback);
		glfwSetMouseButtonCallback(win->m_GLFWHandle, glfw_mouseButtonCallback);
		glfwSetScrollCallback(win->m_GLFWHandle, glfw_scrollCallback);
		glfwSetWindowSizeCallback(win->m_GLFWHandle, glfw_resizeCallback);
		glfwSetWindowIconifyCallback(win->m_GLFWHandle, glfw_iconifyCallback);
		glfwSetCharCallback(win->m_GLFWHandle, glfw_textInputCallback);
		glfwSetWindowUserPointer(win->m_GLFWHandle, win);*/
		
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
		initData.limits().transientVbSize(0xFFFFFF);
		initData.limits().transientIbSize(0xFFFFFF);
		initData.limits().maxEncoders((short)1);
		initData.type(initializer.getConfig().getPreferredBackend().value);
		initData.vendorId(BGFX_PCI_ID_NONE);
		initData.deviceId((short)0);
		initData.allocator(null);
		initData.callback(null);
		
		if(!bgfx_init(initData)) {
			throw new IllegalStateException("BGFX failed to initialize");
		}
		
		BGFXCaps caps = bgfx_get_caps();
		long supported = caps.supported();
		System.out.println(supported);
		
		System.out.println("Texture 2D Arrays: " + (getBit((int)supported, (int)BGFX_CAPS_TEXTURE_2D_ARRAY)));
		
		stack.close();
		
		applyChanges();
	}
	
	int getBit(int n, int k) {
		return (n >> k) & 1;
	}
	
	private long encoder;
	
	public long getEncoder() { return encoder; }
	
	public void beginEncoder() {
		encoder = bgfx_encoder_begin(false);
	}
	
	public void submitEncoder() {
		bgfx_encoder_submit(encoder, 0, (short)0, 0, false);
	}
	
	public void endEncoder() {
		bgfx_encoder_end(encoder);
	}
	
	public void frame() {
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
	}
	
	private void errorCallback(int errorCode, long message) {
	
	}
}
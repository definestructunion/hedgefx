package hedge.fx.platform;

import java.util.Arrays;

import static hedge.fx.platform.GraphicsAPI.*;

public enum PlatformAPI {

	Windows(true, DirectX12, DirectX11, DirectX9, OpenGL, Vulkan),
	Linux(true, OpenGL, Vulkan),
	MacOS(true, Metal, OpenGL),
	Unix(true, OpenGL, Vulkan),
	Android(true, OpenGLES, Vulkan),
	iOS(false, Metal),
	XBox(false, DirectX12, DirectX11, DirectX9),
	Playstation(false, GNM),
	Switch(false, NVM, Vulkan, OpenGL);
	
	public final boolean canTarget;
	private final GraphicsAPI[] apis;
	
	public GraphicsAPI[] getApis() {
		return Arrays.copyOf(apis, apis.length);
	}
	
	private PlatformAPI(boolean canTarget, GraphicsAPI... apis) {
		this.canTarget = canTarget;
		this.apis = apis;
	}
	
	public static PlatformAPI getPlatform() {
		
		String os = System.getProperty("os.name").toLowerCase();
		// this is for android it might not work though
		String vm = System.getProperty("java.vm.name").toLowerCase();
		
		if(os.contains("win")) return Windows;
		if(os.contains("nix")) return Linux;
		if(os.contains("nux") || os.contains("aix")) return Unix;
		if(os.contains("mac") || os.contains("darwin")) return MacOS;
		if(vm.equals("dalvik")) return Android;
		// RoboVM is dead :'(
		if(vm.equals("robovm")) return iOS;
		
		throw new IllegalStateException("Can't deduce Platform type");
	}
}

package com.hedgemen.fx.platform;

import java.util.Arrays;

public enum PlatformAPI {

	Windows(true, GraphicsAPI.DirectX12, GraphicsAPI.DirectX11, GraphicsAPI.DirectX9, GraphicsAPI.OpenGL, GraphicsAPI.Vulkan),
	Linux(true, GraphicsAPI.OpenGL, GraphicsAPI.Vulkan),
	MacOS(true, GraphicsAPI.Metal, GraphicsAPI.OpenGL),
	Unix(true, GraphicsAPI.OpenGL, GraphicsAPI.Vulkan),
	Android(true, GraphicsAPI.OpenGLES, GraphicsAPI.Vulkan),
	iOS(false, GraphicsAPI.Metal),
	XBox(false, GraphicsAPI.DirectX12, GraphicsAPI.DirectX11, GraphicsAPI.DirectX9),
	Playstation(false, GraphicsAPI.GNM),
	Switch(false, GraphicsAPI.NVM, GraphicsAPI.Vulkan, GraphicsAPI.OpenGL);
	
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
		if(os.contains("nux")) return Linux;
		if(os.contains("nix") || os.contains("aix")) return Unix;
		if(os.contains("mac") || os.contains("darwin")) return MacOS;
		if(vm.equals("dalvik")) return Android;
		// RoboVM is dead :'(
		if(vm.equals("robovm")) return iOS;
		
		throw new IllegalStateException("Can't deduce Platform type");
	}
}

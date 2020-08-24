package com.hgm.fx.platform;

import static org.lwjgl.bgfx.BGFX.*;

public enum GraphicsAPI {
	
	None(BGFX_RENDERER_TYPE_NOOP, false),
	DirectX9(BGFX_RENDERER_TYPE_DIRECT3D9, false),
	DirectX11(BGFX_RENDERER_TYPE_DIRECT3D11, true),
	DirectX12(BGFX_RENDERER_TYPE_DIRECT3D12, false),
	GNM(BGFX_RENDERER_TYPE_GNM, false),
	Metal(BGFX_RENDERER_TYPE_METAL, true),
	NVM(BGFX_RENDERER_TYPE_NVN, false),
	OpenGLES(BGFX_RENDERER_TYPE_OPENGLES, false),
	OpenGL(BGFX_RENDERER_TYPE_OPENGL, true),
	Vulkan(BGFX_RENDERER_TYPE_VULKAN, false);
	//WebGPU(BGFX_RENDERER_TYPE_WEBGPU, false);
	
	public final int value;
	public final boolean supported;
	
	private GraphicsAPI(int value, boolean supported) {
		this.value = value;
		this.supported = supported;
	}
}

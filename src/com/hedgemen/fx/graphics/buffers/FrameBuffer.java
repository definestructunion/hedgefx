package com.hedgemen.fx.graphics.buffers;

import com.hedgemen.fx.graphics.GraphicsDevice;
import com.hedgemen.fx.graphics.Texture;
import com.hedgemen.fx.util.BGFXResource;

import static org.lwjgl.bgfx.BGFX.*;

public class FrameBuffer implements BGFXResource {
	
	private short handle;
	public short getHandle() { return handle; }
	
	private GraphicsDevice graphicsDevice;
	
	public FrameBuffer(GraphicsDevice graphicsDevice) {
		this.graphicsDevice = graphicsDevice;
	}
	
	public void begin() {
		begin(graphicsDevice.getBackBufferWidth(),
				graphicsDevice.getBackBufferHeight(),
				BGFX_TEXTURE_FORMAT_RGBA8,
				BGFX_SAMPLER_U_CLAMP |
						BGFX_SAMPLER_V_CLAMP |
						BGFX_SAMPLER_MIN_POINT |
						BGFX_SAMPLER_MAG_POINT);
	}
	
	public void begin(int width, int height) {
		begin(width,
				height,
				BGFX_TEXTURE_FORMAT_RGBA8,
				BGFX_SAMPLER_U_CLAMP |
						BGFX_SAMPLER_V_CLAMP |
						BGFX_SAMPLER_MIN_POINT |
						BGFX_SAMPLER_MAG_POINT);
	}
	
	public void begin(int width, int height, int textureFormat, long textureFlags) {
		bgfx_create_frame_buffer(width, height, textureFormat, textureFlags);
		bgfx_set_view_frame_buffer(0, handle);
	}
	
	public Texture end() {
		bgfx_set_view_frame_buffer(0, BGFX_INVALID_HANDLE);
		return new Texture(handle, 1280, 720);
	}
	
	public void destroy() {
		dispose();
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_frame_buffer(handle);
	}
}

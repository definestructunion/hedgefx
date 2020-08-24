package com.hgm.fx.graphics;

import com.hgm.fx.graphics.buffers.IndexBuffer;
import com.hgm.fx.graphics.buffers.TransientVertexBuffer;
import com.hgm.fx.graphics.buffers.VertexBuffer;
import com.hgm.fx.util.Nullable;

import static org.lwjgl.bgfx.BGFX.*;

public class Encoder {
	
	private long handle;
	public long getHandle() { return handle; }
	
	private GraphicsDevice graphicsDevice;
	
	private EncoderSpecification specification;
	
	public Encoder(EncoderSpecification specification, GraphicsDevice graphicsDevice) {
		this.graphicsDevice = graphicsDevice;
		this.specification = specification;
	}
	
	public void begin() {
		handle = bgfx_encoder_begin(specification.isMultithreaded());
	}
	
	public void end() {
		bgfx_encoder_end(handle);
	}
	
	public void submit(ShaderProgram program) {
		bgfx_encoder_submit(handle, 0, program.getHandle(), 0, false);
	}
	
	public void setState(long state) {
		bgfx_encoder_set_state(handle, state, 0x00000000);
	}
	
	public void setState() {
		setState(specification.getState());
	}
	
	public void setTexture(@Nullable Texture texture, ShaderUniform uniform, int flags) {
		short textureHandle = (texture != null) ? texture.getHandle() : BGFX_INVALID_HANDLE;
		// might change texture to have a stage field if it's for texture arrays
		bgfx_encoder_set_texture(handle, 0, uniform.getHandle(), textureHandle, flags);
	}
	
	public void setTexture(@Nullable Texture texture, ShaderUniform uniform) {
		setTexture(texture, uniform, 0xFFFFFFFF);
	}
	
	public void setVertexBuffer(VertexBuffer buffer) {
		bgfx_encoder_set_dynamic_vertex_buffer(handle, 0, buffer.getHandle(), 0, buffer.getSize(), buffer.getLayout().getHandle());
	}
	
	public void setTransientVertexBuffer(TransientVertexBuffer buffer) {
		setTransientVertexBuffer(buffer, 0, buffer.getAllocateSize());
	}
	
	public void setTransientVertexBuffer(TransientVertexBuffer buffer, int start, int end) {
		bgfx_encoder_set_transient_vertex_buffer(handle, 0, buffer.getHandle(), start, end, buffer.getLayout().getHandle());
	}
	
	public void setIndexBuffer(IndexBuffer buffer, int start, int end) {
		bgfx_encoder_set_dynamic_index_buffer(handle, buffer.getHandle(), start, end);
	}
	
	public void setIndexBuffer(IndexBuffer buffer) {
		setIndexBuffer(buffer, 0, buffer.getSize());
	}
}

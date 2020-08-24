package com.hgm.fx.graphics.buffers;

import com.hgm.fx.util.BGFXResource;
import com.hgm.fx.util.BufferUtils;
import org.lwjgl.bgfx.BGFXMemory;

import java.nio.ByteBuffer;

import static org.lwjgl.bgfx.BGFX.*;

public class VertexBuffer implements BGFXResource {
	
	private short handle;
	public short getHandle() { return handle; }
	
	private int size;
	public int getSize() { return size; }
	
	private VertexLayout layout;
	public VertexLayout getLayout() { return layout; }
	
	public VertexBuffer(int size, VertexLayout layout) {
		this.layout = layout;
		this.size = size;
		handle = bgfx_create_dynamic_vertex_buffer(size, layout.internal(), layout.getHandle());
		bgfx_set_dynamic_vertex_buffer(0, (short)0, 0, 0);
	}
	
	public VertexBuffer(float[] data, VertexLayout layout) {
		this(BufferUtils.toByteBuffer(data), data.length, layout);
	}
	
	public VertexBuffer(ByteBuffer data, int size, VertexLayout layout) {
		this.layout = layout;
		this.size = size;
		BGFXMemory buffer = bgfx_make_ref(data); // make_ref might cause issues
		handle = bgfx_create_dynamic_vertex_buffer_mem(buffer, layout.internal(), layout.getHandle());
		bgfx_set_dynamic_vertex_buffer(0, (short)0, 0, 0);
	}
	
	public void setVertices(float[] data) {
		setVertices(BufferUtils.toByteBuffer(data), data.length);
	}
	
	public void setVertices(ByteBuffer data, int size) {
		this.size = size;
		BGFXMemory buffer = bgfx_make_ref(data); // make_ref might cause issues
		bgfx_update_dynamic_vertex_buffer(handle, 0, buffer);
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_dynamic_vertex_buffer(handle);
	}
}

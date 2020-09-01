package com.hedgemen.fx.graphics.buffers;

import com.hedgemen.fx.util.BGFXResource;
import com.hedgemen.fx.util.BufferUtils;
import org.lwjgl.bgfx.BGFXMemory;

import java.nio.ByteBuffer;

import static org.lwjgl.bgfx.BGFX.*;

public class IndexBuffer implements BGFXResource {
	
	private short handle;
	public short getHandle() { return handle; }
	
	private int size;
	public int getSize() { return size; }
	
	public IndexBuffer(short[] data) {
		this(BufferUtils.toByteBuffer(data));
	}
	
	public IndexBuffer(ByteBuffer data) {
		BGFXMemory buffer = bgfx_make_ref(data); // make_ref might cause issues
		handle = bgfx_create_dynamic_index_buffer_mem(buffer, BGFX_BUFFER_NONE);
		bgfx_set_dynamic_index_buffer((short)0, 0, 0);
	}
	
	public void setIndices(short[] data) {
		setIndices(BufferUtils.toByteBuffer(data), data.length);
	}
	
	public void setIndices(ByteBuffer data, int size) {
		this.size = size;
		BGFXMemory buffer = bgfx_make_ref(data); // make_ref might cause issues
		bgfx_update_dynamic_index_buffer(handle, 0, buffer);
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_dynamic_index_buffer(handle);
	}
}

package com.hedgemen.fx.graphics.buffers;

import com.hedgemen.fx.graphics.GraphicsDevice;
import com.hedgemen.fx.util.Disposable;
import org.lwjgl.bgfx.BGFXTransientVertexBuffer;

import java.nio.ByteBuffer;

import static org.lwjgl.bgfx.BGFX.*;

public class TransientVertexBuffer implements Disposable {
	
	private BGFXTransientVertexBuffer handle;
	public BGFXTransientVertexBuffer getHandle() { return handle; }
	
	private GraphicsDevice graphicsDevice;
	
	private VertexLayout layout;
	public VertexLayout getLayout() { return layout; }
	
	private int allocateSize;
	public int getAllocateSize() { return allocateSize; }
	public void setAllocateSize(int value) { allocateSize = value; }
	
	public ByteBuffer data() { return handle.data(); }
	
	public TransientVertexBuffer(int allocateSize, VertexLayout layout, GraphicsDevice graphicsDevice) {
		this.allocateSize = allocateSize;
		this.layout = layout;
		this.graphicsDevice = graphicsDevice;
		this.handle = BGFXTransientVertexBuffer.calloc();
	}
	
	public boolean canAllocate() {
		return bgfx_get_avail_transient_vertex_buffer(allocateSize, layout.internal()) == allocateSize;
	}
	
	public void allocate() {
		//if(!canAllocate()) return; // todo maybe log this
		bgfx_alloc_transient_vertex_buffer(handle, allocateSize, layout.internal());
	}
	
	@Override
	public void dispose() {
		handle.free();
	}
}

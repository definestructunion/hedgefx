package hedge.fx.graphics.buffers;

import hedge.fx.graphics.GraphicsDevice;
import hedge.fx.util.BGFXResource;
import hedge.fx.util.BufferUtils;
import org.lwjgl.bgfx.BGFXMemory;

import java.nio.ByteBuffer;

import static org.lwjgl.bgfx.BGFX.*;

public class VertexBuffer implements BGFXResource {
	
	public enum Type { Static, Dynamic, Transient }
	
	private short handle;
	public short getHandle() { return handle; }
	
	private GraphicsDevice graphicsDevice;
	
	private ByteBuffer vertices;
	public ByteBuffer getVertices() { return vertices; }
	
	/*public VertexBuffer(VertexLayout layout, GraphicsDevice graphicsDevice) {
		this.graphicsDevice = graphicsDevice;
		handle = bgfx_create_dynamic_vertex_buffer(0, layout.internal(), layout.getHandle());
	}*/
	
	public VertexBuffer(float[] data, VertexLayout layout, GraphicsDevice graphicsDevice) {
		this(BufferUtils.toByteBuffer(data), layout, graphicsDevice);
	}
	
	public VertexBuffer(ByteBuffer data, VertexLayout layout, GraphicsDevice graphicsDevice) {
		this.graphicsDevice = graphicsDevice;
		BGFXMemory buffer = bgfx_make_ref(data); // make_ref might cause issues
		//BGFXMemory buffer = bgfx_copy(data);
		handle = bgfx_create_dynamic_vertex_buffer_mem(buffer, layout.internal(), layout.getHandle());
		bgfx_set_dynamic_vertex_buffer(0, (short)0, 0, 0);
		vertices = data;
	}
	
	public void setVertices(float[] data) {
		setVertices(BufferUtils.toByteBuffer(data));
	}
	
	public void setVertices(ByteBuffer data) {
		BGFXMemory buffer = bgfx_make_ref(data); // make_ref might cause issues
		bgfx_update_dynamic_vertex_buffer(handle, 0, buffer);
	}
	
	public void updateVertices() {
		BGFXMemory buffer = bgfx_make_ref(vertices); // make_ref might cause issues
		bgfx_update_dynamic_vertex_buffer(handle, 0, buffer);
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_dynamic_vertex_buffer(handle);
	}
}

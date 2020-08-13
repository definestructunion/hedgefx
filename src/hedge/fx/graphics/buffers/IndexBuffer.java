package hedge.fx.graphics.buffers;

import hedge.fx.graphics.GraphicsDevice;
import hedge.fx.util.BGFXResource;
import hedge.fx.util.BufferUtils;
import org.lwjgl.bgfx.BGFXMemory;
import org.lwjgl.bgfx.BGFXTransientVertexBuffer;

import java.nio.ByteBuffer;

import static org.lwjgl.bgfx.BGFX.*;

public class IndexBuffer implements BGFXResource {
	
	private short handle;
	public short getHandle() { return handle; }
	
	private GraphicsDevice graphicsDevice;
	
	public IndexBuffer(short[] data, GraphicsDevice graphicsDevice) {
		this(BufferUtils.toByteBuffer(data), graphicsDevice);
	}
	
	public IndexBuffer(ByteBuffer data, GraphicsDevice graphicsDevice) {
		this.graphicsDevice = graphicsDevice;
		BGFXMemory buffer = bgfx_make_ref(data); // make_ref might cause issues
		//BGFXMemory buffer = bgfx_copy(data);
		handle = bgfx_create_dynamic_index_buffer_mem(buffer, BGFX_BUFFER_NONE);
		bgfx_set_dynamic_index_buffer((short)0, 0, 0);
	}
	
	public void setIndices(short[] data, GraphicsDevice graphicsDevice) {
		setIndices(BufferUtils.toByteBuffer(data));
	}
	
	public void setIndices(ByteBuffer data) {
		BGFXMemory buffer = bgfx_make_ref(data); // make_ref might cause issues
		bgfx_update_dynamic_index_buffer(handle, 0, buffer);
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_dynamic_index_buffer(handle);
	}
}

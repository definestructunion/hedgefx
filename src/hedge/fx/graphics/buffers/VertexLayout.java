package hedge.fx.graphics.buffers;

import hedge.fx.graphics.GraphicsDevice;
import hedge.fx.util.BGFXResource;
import org.lwjgl.bgfx.BGFXVertexLayout;

import static org.lwjgl.bgfx.BGFX.*;

public class VertexLayout implements BGFXResource {
	
	private short handle;
	public short getHandle() { return handle; }
	
	private BGFXVertexLayout internal;
	public BGFXVertexLayout internal() { return internal; }
	
	private GraphicsDevice graphicsDevice;
	
	public VertexLayout(VertexAttributes attributes, GraphicsDevice graphicsDevice) {
		
		internal = BGFXVertexLayout.calloc();
		bgfx_vertex_layout_begin(internal, graphicsDevice.getApi().value);
		
		for(VertexAttribute attrib : attributes.getAttributes()) {
			bgfx_vertex_layout_add(internal, attrib.getAttrib(), attrib.getCount(), attrib.getType(), attrib.isNormalized(), attrib.isAsInt());
		}
		
		bgfx_vertex_layout_end(internal);
		handle = bgfx_create_vertex_layout(internal);
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_vertex_layout(handle);
		internal.free(); // might be unnecessary
	}
}

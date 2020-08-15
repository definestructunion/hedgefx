package hedge.fx.graphics;

import hedge.fx.graphics.buffers.IndexBuffer;
import hedge.fx.graphics.buffers.VertexLayout;
import hedge.fx.util.Disposable;
import org.lwjgl.bgfx.BGFXTransientVertexBuffer;

import static org.lwjgl.bgfx.BGFX.*;

public class SpriteBatch implements Disposable {
	
	private int drawCalls = 0;
	
	private long frames = 0;
	private long totalDrawCalls = 0;
	// debug stuff
	public long getTotalDrawCalls() { return totalDrawCalls; }
	public void setTotalDrawCalls(long value) { totalDrawCalls = value; }
	
	private IndexBuffer ibo;
	private BGFXTransientVertexBuffer vbo;
	
	private GraphicsDevice graphicsDevice;
	private int index = 0;
	private short textureUniform;
	
	private ShaderProgram program;
	
	private VertexLayout vertexLayout;
	
	private int buffersSize = 128; // this number is low because there's a transient buffer limit
	
	public SpriteBatch(GraphicsDevice graphicsDevice, VertexLayout layout, ShaderProgram program) {
		this.vertexLayout = layout;
		this.program = program;
		this.graphicsDevice = graphicsDevice;
		ibo = createIndexBuffer(graphicsDevice);
		textureUniform = bgfx_create_uniform("texture", BGFX_UNIFORM_TYPE_VEC4, 1);
		vbo = BGFXTransientVertexBuffer.calloc();
	}
	
	private IndexBuffer createIndexBuffer(GraphicsDevice graphicsDevice) {
		short[] indices = new short[buffersSize];
		
		for(short i = 0; i < indices.length-6; i+=6) {
			indices[i + 0] = (short)(i + 0);
			indices[i + 1] = (short)(i + 1);
			indices[i + 2] = (short)(i + 2);
			indices[i + 3] = (short)(i + 2);
			indices[i + 4] = (short)(i + 3);
			indices[i + 5] = (short)(i + 0);
		}
		
		return new IndexBuffer(indices, graphicsDevice);
	}
	
	public void begin() {
		graphicsDevice.beginEncoder();
		
		bgfx_alloc_transient_vertex_buffer(vbo, buffersSize, vertexLayout.internal());
		//vbo.data().rewind();
		totalDrawCalls += drawCalls;
		drawCalls = 0;
		//vbo.data().flip();
		if(frames++ % 120 == 0) hasToldRecently = false;
	}
	
	public void end() {
		long encoder = graphicsDevice.getEncoder();
		
		bgfx_encoder_set_texture(encoder,0, textureUniform, texture, 0xFFFFFFFF);
		bgfx_encoder_set_transient_vertex_buffer(encoder,0, vbo, 0, index, vertexLayout.getHandle());
		bgfx_encoder_set_dynamic_index_buffer(encoder,ibo.getHandle(), 0, Short.MAX_VALUE - 1024);
		
		bgfx_submit(0, program.getHandle(), 0, false);
		
		graphicsDevice.endEncoder();
		index = 0;
	}
	
	private short texture;
	
	public void draw(short texture, float x, float y, float width, float height, float color) {
		if(!allowDrawing()) return;
		
		if((index >= buffersSize - (24 / 4) || (this.texture != texture))) {
			end();
			begin();
		}
		
		this.texture = texture;
		var buffer = vbo.data();
		buffer.position(index * vertexLayout.getAttributes().getStride());
		
		buffer.putFloat(x).putFloat(y).putFloat(0);
		buffer.putFloat(0.0f).putFloat(0.0f);
		buffer.putFloat(color);
		
		buffer.putFloat(x).putFloat(y+height).putFloat(0);
		buffer.putFloat(0.0f).putFloat(1.0f);
		buffer.putFloat(color);
		
		buffer.putFloat(x+width).putFloat(y+height).putFloat(0);
		buffer.putFloat(1.0f).putFloat(1.0f);
		buffer.putFloat(color);
		
		buffer.putFloat(x+width).putFloat(y).putFloat(0);
		buffer.putFloat(1.0f).putFloat(0.0f);
		buffer.putFloat(color);
		
		++drawCalls;
		index += 6;
	}
	
	@Override
	public void dispose() {
		vbo.free();
	}
	
	private boolean hasToldRecently;
	
	private boolean allowDrawing() {
		boolean allows = (bgfx_get_avail_transient_vertex_buffer(buffersSize, vertexLayout.internal()) != 0);
		if(!allows && !hasToldRecently) {
			System.out.println("[WARNING]: Not drawing any more sprites, buffers are full!");
			hasToldRecently = true;
		}
		return allows;
	}
}

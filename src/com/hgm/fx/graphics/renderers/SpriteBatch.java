package com.hgm.fx.graphics.renderers;

import com.hgm.fx.graphics.*;
import com.hgm.fx.graphics.buffers.*;
import com.hgm.fx.util.Disposable;

import static org.lwjgl.bgfx.BGFX.*;

public class SpriteBatch implements Disposable {
	
	private IndexBuffer ibo;
	private TransientVertexBuffer vbo;
	private GraphicsDevice graphicsDevice;
	private ShaderUniform textureUniform;
	private ShaderProgram program;
	private VertexLayout vertexLayout;
	
	private int index = 0;
	
	private int buffersSize = 144;
	
	public SpriteBatch(GraphicsDevice graphicsDevice, ShaderProgram program) {
		this.graphicsDevice = graphicsDevice;
		this.program = program;
		ibo = createIndexBuffer();
		textureUniform = new ShaderUniform("texture", BGFX_UNIFORM_TYPE_VEC4, 1);
		this.vertexLayout = createVertexLayout(createVertexAttributes());
		vbo = new TransientVertexBuffer(buffersSize, vertexLayout, graphicsDevice);
	}
	
	private IndexBuffer createIndexBuffer() {
		short[] indices = new short[buffersSize];
		
		for(short i = 0; i < indices.length-6; i+=6) {
			indices[i + 0] = (short)(i + 0);
			indices[i + 1] = (short)(i + 1);
			indices[i + 2] = (short)(i + 2);
			indices[i + 3] = (short)(i + 2);
			indices[i + 4] = (short)(i + 3);
			indices[i + 5] = (short)(i + 0);
		}
		
		return new IndexBuffer(indices);
	}
	
	private VertexAttributes createVertexAttributes() {
		return new VertexAttributes(
				new VertexAttribute(BGFX_ATTRIB_POSITION, 3, BGFX_ATTRIB_TYPE_FLOAT, false, false),
				new VertexAttribute(BGFX_ATTRIB_TEXCOORD0, 2, BGFX_ATTRIB_TYPE_FLOAT, true, false),
				new VertexAttribute(BGFX_ATTRIB_COLOR0, 4, BGFX_ATTRIB_TYPE_UINT8, true, false)
		);
	}
	
	private VertexLayout createVertexLayout(VertexAttributes attributes) {
		return new VertexLayout(attributes, graphicsDevice);
	}
	
	public void begin() {
		//if(!allowDrawing()) return;
		graphicsDevice.getEncoder().begin();
		vbo.allocate();
	}
	
	private void flush() {
		//if(!allowDrawing()) return;
		vbo.data().flip();
		
		Encoder encoder = graphicsDevice.getEncoder();
		
		encoder.setState();
		encoder.setTexture(texture, textureUniform);
		encoder.setTransientVertexBuffer(vbo, 0, buffersSize);
		encoder.setIndexBuffer(ibo, 0, buffersSize);
		encoder.submit(program);
		
		index = 0;
		encoder.end();
	}
	
	public void end() {
		//if(!allowDrawing()) return;
		flush();
		this.texture = null;
	}
	
	private Texture texture;
	
	public void draw(Texture texture, float x, float y, float width, float height, Color color) {
		draw(texture, x, y, width, height, 0.0f, 0.0f, 1.0f, 1.0f, color);
	}
	
	public void draw(Texture texture, float x, float y, float width, float height, float u0, float v0, float u1, float v1, Color color) {
		if(!allowDrawing()) return;
		
		if((!texture.equals(this.texture)) && (this.texture != null) ||
			(index >= buffersSize - (vertexLayout.getAttributes().getStride()))) {
			flush();
			begin();
		}
		
		if(!allowDrawing()) return;
		
		this.texture = texture;
		var buffer = vbo.data();
		buffer.position(index * vertexLayout.getAttributes().getStride());
		
		buffer.putFloat(x).putFloat(y).putFloat(0);
		buffer.putFloat(u0).putFloat(v0);
		buffer.putFloat(color.hex);
		
		buffer.putFloat(x).putFloat(y+height).putFloat(0);
		buffer.putFloat(u0).putFloat(v1);
		buffer.putFloat(color.hex);
		
		buffer.putFloat(x+width).putFloat(y+height).putFloat(0);
		buffer.putFloat(u1).putFloat(v1);
		buffer.putFloat(color.hex);
		
		buffer.putFloat(x+width).putFloat(y).putFloat(0);
		buffer.putFloat(u1).putFloat(v0);
		buffer.putFloat(color.hex);
		
		index += 6;
	}
	
	public void drawString(Font font, String text, int x, int y, Color color) {
		
		if(!allowDrawing()) return;
		
		float x0 = x;
		float y0 = y;
		
		for(int i = 0; i < text.length(); ++i) {
			
			char c = text.charAt(i);
			
			if(c == '\n') {
				y0 += font.getTexture().getHeight();
				x0 = x;
				continue;
			}
			
			var glyph = font.getGlyph(c);
			
			float width = glyph.width;
			float height = glyph.height;
			
			float textureWidth = font.getTexture().getWidth();
			float textureHeight = font.getTexture().getHeight();
			
			float u0 = (glyph.x) / textureWidth;
			float u1 = (glyph.x + glyph.width) / textureWidth;
			
			float v0 = (glyph.y) / textureHeight;
			float v1 = (glyph.y + glyph.height) / textureHeight;
			
			draw(texture, x0, y0, width, height, u0, v0, u1, v1, color);
			
			x0 += glyph.width + glyph.advance;
		}
	}
	
	@Override
	public void dispose() {
		vbo.dispose();
		vertexLayout.dispose();
		textureUniform.dispose();
		ibo.dispose();
	}
	
	private boolean allowDrawing() {
		boolean allows = index + 6 <= buffersSize && vbo.canAllocate();
		//boolean allows = (bgfx_get_avail_transient_vertex_buffer(buffersSize, vertexLayout.internal()) == buffersSize);
		if(!allows) {
			if(!vbo.canAllocate());
				//System.out.println("[WARNING]: Not drawing any more sprites, buffers are full!");
		}
		return allows;
	}
}

package com.hedgemen.fx.graphics.renderers;

import com.hedgemen.fx.graphics.*;
import com.hedgemen.fx.graphics.buffers.*;
import com.hedgemen.fx.math.Vector2;
import com.hedgemen.fx.Rectangle;

import static org.lwjgl.bgfx.BGFX.*;

public class SpriteBatch implements Renderer {
	
	private IndexBuffer ibo;
	private TransientVertexBuffer vbo;
	private GraphicsDevice graphicsDevice;
	private ShaderUniform textureUniform;
	private ShaderProgram program;
	private VertexLayout vertexLayout;
	private Texture texture;
	
	private int index = 0;
	private int buffersSize = 144;
	
	public SpriteBatch(GraphicsDevice graphicsDevice, ShaderProgram program) {
		this.graphicsDevice = graphicsDevice;
		this.program = program;
		this.ibo = createIndexBuffer();
		this.textureUniform = new ShaderUniform("texture", BGFX_UNIFORM_TYPE_VEC4, 1);
		this.vertexLayout = createVertexLayout(createVertexAttributes());
		this.vbo = new TransientVertexBuffer(buffersSize, vertexLayout, graphicsDevice);
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
	
	@Override
	public void begin() {
		graphicsDevice.getEncoder().begin();
		vbo.allocate();
	}
	
	@Override
	public void end() {
		flush();
		this.texture = null;
	}
	
	private void flush() {
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
	
	@Override
	public void draw(Texture texture, float x, float y, Color color) {
		draw(texture, x, y, texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(),
				texture.getHeight(), color, 0, 0, 0);
	}
	
	@Override
	public void draw(Texture texture, float x, float y, float width, float height, Color color) {
		draw(texture, x, y, width, height, 0, 0, texture.getWidth(), texture.getHeight(), color, 0, 0, 0);
	}
	
	@Override
	public void draw(Texture texture, Vector2 position, Color color) {
		draw(texture, position.x, position.y, texture.getWidth(), texture.getHeight(),
				0, 0, texture.getWidth(), texture.getHeight(), color, 0, 0, 0);
	}
	
	@Override
	public void draw(Texture texture, Vector2 position, Vector2 size, Color color) {
		draw(texture, position.x, position.y, size.x, size.y, 0, 0, texture.getWidth(),
				texture.getHeight(), color, 0, 0, 0);
	}
	
	@Override
	public void draw(Texture texture, Rectangle dimensions, Color color) {
		draw(texture, dimensions.x, dimensions.y, dimensions.width, dimensions.height,
				0, 0, texture.getWidth(), texture.getHeight(), color, 0, 0, 0);
	}
	
	@Override
	public void draw(Texture texture, Rectangle dimensions, Rectangle textureCoords, Color color) {
		draw(texture, dimensions.x, dimensions.y, dimensions.width, dimensions.height, textureCoords.x, textureCoords.y,
				textureCoords.width, textureCoords.height, color, 0, 0, 0);
	}
	
	@Override
	public void draw(Texture texture, Rectangle dimensions, Rectangle textureCoords, Color color, float rotation, float originX, float originY) {
		draw(texture, dimensions.x, dimensions.y, dimensions.width, dimensions.height, textureCoords.x, textureCoords.y,
				textureCoords.width, textureCoords.height, color, rotation, originX, originY);
	}
	
	@Override
	public void draw(Texture texture, Rectangle dimensions, Rectangle textureCoords, Color color, float rotation, Vector2 origin) {
		draw(texture, dimensions.x, dimensions.y, dimensions.width, dimensions.height, textureCoords.x, textureCoords.y,
				textureCoords.width, textureCoords.height, color, rotation, origin.x, origin.y);
	}
	
	@Override
	public void drawString(Font font, String message, float x, float y, Color color) {
		drawString(font, message, x, y, 0, color, 0, 0, 0);
	}
	
	@Override
	public void drawString(Font font, String message, Vector2 position, Color color) {
		drawString(font, message, position.x, position.y, 1.0f, color, 0, 0, 0);
	}
	
	@Override
	public void drawString(Font font, String message, float x, float y, Color color, float rotation, float originX, float originY) {
		drawString(font, message, x, y, 1.0f, color, rotation, originX, originY);
	}
	
	@Override
	public void drawString(Font font, String message, Vector2 position, Color color, float rotation, Vector2 origin) {
		drawString(font, message, position.x, position.y, 1.0f, color, rotation, origin.x, origin.y);
	}
	
	@Override
	public void drawString(Font font, String message, Vector2 position, float scale, Color color, float rotation, Vector2 origin) {
		drawString(font, message, position.x, position.y, scale, color, rotation, origin.x, origin.y);
	}
	
	@Override
	public void draw(Texture texture, float x, float y, float w, float h, float rx, float ry, float rw, float rh,
					 Color color, float rotation, float originX, float originY) {
		
	}
	
	@Override
	public void drawString(Font font, String message, float x, float y, float scale, Color color, float rotation, float originX, float originY) {
	
	}
	
	private void drawPrimitive(Texture texture, float x0, float y0, float x1, float y1, float u0, float v0, float u1, float v1, Color color) {
	
	}
	
	@Override
	public boolean canDrawMore() {
		return index + 6 <= buffersSize && vbo.canAllocate();
	}
	
	@Override
	public void dispose() {
		vbo.dispose();
		vertexLayout.dispose();
		textureUniform.dispose();
		ibo.dispose();
	}
}

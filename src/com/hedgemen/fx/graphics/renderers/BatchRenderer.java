package com.hedgemen.fx.graphics.renderers;

import com.hedgemen.fx.Rectangle;
import com.hedgemen.fx.graphics.*;
import com.hedgemen.fx.graphics.buffers.*;
import com.hedgemen.fx.math.Vector2;

import static org.lwjgl.bgfx.BGFX.*;
import static org.lwjgl.bgfx.BGFX.BGFX_ATTRIB_TYPE_UINT8;

public class BatchRenderer implements Renderer {
	
	private GraphicsDevice graphicsDevice;
	private IndexBuffer ibo;
	private TransientVertexBuffer vbo;
	private ShaderProgram shaderProgram;
	private VertexLayout vertexLayout;
	
	private int index = 0;
	private int buffersSize = 144;
	
	public BatchRenderer(ShaderProgram shaderProgram, GraphicsDevice graphicsDevice) {
		this.shaderProgram = shaderProgram;
		this.graphicsDevice = graphicsDevice;
		
		vertexLayout = createVertexLayout(createVertexAttributes());
		ibo = createIndexBuffer();
		vbo = createVertexBuffer();
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
	
	private TransientVertexBuffer createVertexBuffer() {
		return new TransientVertexBuffer(buffersSize, vertexLayout, graphicsDevice);
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
	}
	
	@Override
	public void end() {
		graphicsDevice.getEncoder().end();
	}
	
	@Override
	public boolean canDrawMore() {
		return vbo.canAllocate() && index <= buffersSize - vertexLayout.getAttributes().getStride();
	}
	
	@Override
	public void draw(Texture texture, float x, float y, float w, float h, float rx, float ry, float rw, float rh, Color color, float rotation, float originX, float originY) {
	
	}
	
	@Override
	public void drawString(Font font, String message, float x, float y, float scale, Color color, float rotation, float originX, float originY) {
	
	}
	
	@Override
	public void draw(Texture texture, Rectangle dimensions, Rectangle textureCoords, Color color, float rotation, Vector2 origin) {
		draw(texture,
				dimensions.x, dimensions.y, dimensions.width, dimensions.height,
				textureCoords.x, textureCoords.y, textureCoords.width, textureCoords.height,
				color,
				rotation,
				origin.x, origin.y);
	}
	
	@Override
	public void drawString(Font font, String message, Vector2 position, float scale, Color color, float rotation, Vector2 origin) {
		drawString(font,
				message,
				position.x, position.y,
				scale,
				color,
				rotation,
				origin.x,
				origin.y);
	}
	
	@Override
	public void dispose() {
	
	}
}

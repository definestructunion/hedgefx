package com.hedgemen.fx.graphics;

import com.hedgemen.fx.util.BGFXResource;

import java.nio.*;

import static org.lwjgl.bgfx.BGFX.*;

public class ShaderUniform implements BGFXResource {
	
	private short handle;
	public short getHandle() { return handle; }
	
	private String attributeName;
	public String getAttributeName() { return attributeName; }
	
	private int type;
	public int getType() { return type; }
	
	public ShaderUniform(String attributeName, int uniformType) {
		this(attributeName, uniformType, 1);
	}
	
	public ShaderUniform(String attributeName, int uniformType, int count) {
		this.attributeName = attributeName;
		this.type = uniformType;
		handle = bgfx_create_uniform(attributeName, type, count);
	}
	
	public void set(ByteBuffer buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(ShortBuffer buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(IntBuffer buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(LongBuffer buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(FloatBuffer buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(DoubleBuffer buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(short[] buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(int[] buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(long[] buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(float[] buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	public void set(double[] buffer, int count) {
		bgfx_set_uniform(handle, buffer, count);
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_uniform(handle);
	}
}

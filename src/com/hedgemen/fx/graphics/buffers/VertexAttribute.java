package com.hedgemen.fx.graphics.buffers;

import static org.lwjgl.bgfx.BGFX.*;

public class VertexAttribute {
	
	private int attrib;
	public int getAttrib() { return attrib; }
	
	private int count;
	public int getCount() { return count; }
	
	private int type;
	public int getType() { return type; }
	
	private boolean normalized;
	public boolean isNormalized() { return normalized; }
	
	private boolean asInt;
	public boolean isAsInt() { return asInt; }
	
	private int byteSize;
	public int getByteSize() { return byteSize; }
	
	public VertexAttribute(int attrib, int count, int type, boolean normalized, boolean asInt) {
		this.attrib = attrib;
		this.count = count;
		this.type = type;
		this.normalized = normalized;
		this.asInt = asInt;
		byteSize = calculateByteSize();
	}
	
	private int calculateByteSize() {
		switch (type) {
			case BGFX_ATTRIB_TYPE_FLOAT: return 4 * count;
			case BGFX_ATTRIB_TYPE_UINT8: return 1 * count;
			case BGFX_ATTRIB_TYPE_INT16: return 2 * count;
			//case BGFX_ATTRIB_TYPE_UINT10: return byteSize * count;
		}
		
		return 0;
	}
}

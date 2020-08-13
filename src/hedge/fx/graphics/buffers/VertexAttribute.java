package hedge.fx.graphics.buffers;

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
	
	public VertexAttribute(int attrib, int count, int type, boolean normalized, boolean asInt) {
		this.attrib = attrib;
		this.count = count;
		this.type = type;
		this.normalized = normalized;
		this.asInt = asInt;
	}
}

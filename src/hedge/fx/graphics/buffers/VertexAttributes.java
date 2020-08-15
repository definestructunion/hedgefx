package hedge.fx.graphics.buffers;

import java.util.Arrays;
import java.util.List;

public class VertexAttributes {
	
	private List<VertexAttribute> attributes;
	public List<VertexAttribute> getAttributes() { return attributes; }
	
	private int stride;
	public int getStride() { return stride; }
	
	public VertexAttributes(VertexAttribute... attributes) {
		this.attributes = Arrays.asList(attributes);
		stride = calculateStride();
	}
	
	private int calculateStride() {
		int stride = 0;
		for(var attribute : attributes) { stride += attribute.getByteSize(); }
		return stride;
	}
}

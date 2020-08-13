package hedge.fx.graphics.buffers;

import java.util.Arrays;
import java.util.List;

public class VertexAttributes {
	
	private List<VertexAttribute> attributes;
	public List<VertexAttribute> getAttributes() { return attributes; }
	
	public VertexAttributes(VertexAttribute... attributes) {
		this.attributes = Arrays.asList(attributes);
	}
}

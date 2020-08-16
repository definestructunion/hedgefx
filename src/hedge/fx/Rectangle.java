package hedge.fx;

import hedge.fx.util.copying.Copy;

public class Rectangle implements Copy<Rectangle> {
	
	@Override
	public Rectangle deepCopy() {
		return null;
	}
	
	@Override
	public Rectangle shallowCopy() {
		return null;
	}
}

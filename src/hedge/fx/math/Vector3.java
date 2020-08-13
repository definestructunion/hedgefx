package hedge.fx.math;

import hedge.fx.util.copying.ICopy;

public class Vector3 implements ICopy<Vector3>
{
	public final float x;
	public final float y;
	public final float z;
	
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3(Vector3 vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public Vector3 add(Vector3 vec) {
		return new Vector3(x + vec.x, y + vec.y, z + vec.z);
	}
	
	public Vector3 sub(Vector3 vec) {
		return new Vector3(x - vec.x, y - vec.y, z - vec.z);
	}
	
	public Vector3 mult(float value) {
		return new Vector3(x * value, y * value, z * value);
	}
	
	public Vector3 mult(Vector3 vec) {
		return new Vector3(x * vec.x, y * vec.y, z * vec.z);
	}
	
	public Vector3 div(Vector3 vec) {
		return new Vector3(x / vec.x, y / vec.y, z / vec.z);
	}
	
	public Vector3 set(Vector3 vec) {
		return new Vector3(vec.x, vec.y, vec.z);
	}
	
	public Vector3 dotProduct(Vector3 vec) {
		double k1 = (y * vec.z) - (z * vec.y);
		double k2 = (z * vec.x) - (x * vec.z);
		double k3 = (x * vec.y) - (y * vec.x);
		return new Vector3((float)k1, (float)k2, (float)k3);
	}
	
	public Vector3 rotate(Vector3 axis, float angle) {
		Vector3 v = new Vector3(this);
		return v.sub(axis.mult((axis.mult(v))).mult((float)Math.cos(angle))).add((axis.dotProduct(v).mult((float)Math.sin(angle)).add((axis.mult((axis.mult(v)))))));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		Vector3 vec = (Vector3)obj;
		return (x == vec.x && y == vec.y && z == vec.z);
	}
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = (hashCode * 397) ^ (int)x;
		hashCode = (hashCode * 397) ^ (int)y;
		hashCode = (hashCode * 397) ^ (int)z;
		return hashCode;
	}
	
	@Override
	public Vector3 deepCopy() {
		return new Vector3(x, y, z);
	}
	
	@Override
	public Vector3 shallowCopy() {
		return new Vector3(x, y, z);
	}
}
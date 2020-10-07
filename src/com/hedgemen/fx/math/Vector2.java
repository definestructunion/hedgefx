package com.hedgemen.fx.math;

import com.hedgemen.fx.util.copying.Copy;

public class Vector2 implements Copy<Vector2> {

	public float x, y;

	public Vector2() {
		x = 0.0f;
		y = 0.0f;
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2 add(Vector3 vec) {
		x += vec.x;
		y += vec.y;
		return this;
	}
	
	public Vector2 sub(Vector3 vec) {
		x -= vec.x;
		y -= vec.y;
		return this;
	}
	
	public Vector2 mult(float value) {
		x *= value;
		y *= value;
		return this;
	}
	
	public Vector2 mult(Vector3 vec) {
		x *= vec.x;
		y *= vec.y;
		return this;
	}
	
	public Vector2 div(Vector3 vec) {
		x /= vec.x;
		y /= vec.y;
		return this;
	}
	
	public Vector2 set(Vector3 vec) {
		x = vec.x;
		y = vec.y;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		Vector2 vec = (Vector2)obj;
		return (x == vec.x && y == vec.y);
	}
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = (hashCode * 397) ^ (int)x;
		hashCode = (hashCode * 397) ^ (int)y;
		return hashCode;
	}
	
	@Override
	public Vector2 deepCopy() {
		return new Vector2(x, y);
	}
	
	@Override
	public Vector2 shallowCopy() {
		return new Vector2(x, y);
	}
}

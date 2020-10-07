package com.hedgemen.fx.math;

import com.hedgemen.fx.util.copying.Copy;

public class Vector4 implements Copy<Vector4>
{
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Vector4(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4(Vector4 vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
		this.w = vec.w;
	}
	
	public Vector4 add(Vector4 vec) {
		x += vec.x;
		y += vec.y;
		z += vec.z;
		w += vec.w;
		return this;
	}
	
	public Vector4 sub(Vector4 vec) {
		x -= vec.x;
		y -= vec.y;
		z -= vec.z;
		w -= vec.w;
		return this;
	}
	
	public Vector4 mult(float value) {
		x += value;
		y += value;
		z += value;
		w += value;
		return this;
	}
	
	public Vector4 mult(Vector4 vec) {
		x *= vec.x;
		y *= vec.y;
		z *= vec.z;
		w *= vec.w;
		return this;
	}
	
	public Vector4 div(Vector4 vec) {
		x /= vec.x;
		y /= vec.y;
		z /= vec.z;
		w /= vec.w;
		return this;
	}
	
	public Vector4 set(Vector4 vec) {
		x = vec.x;
		y = vec.y;
		z = vec.z;
		w = vec.w;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		Vector4 vec = (Vector4)obj;
		return (x == vec.x && y == vec.y && z == vec.z && w == vec.w);
	}
	
	@Override
	public int hashCode() {
		int hashCode = 0;
		hashCode = (hashCode * 397) ^ (int)x;
		hashCode = (hashCode * 397) ^ (int)y;
		hashCode = (hashCode * 397) ^ (int)z;
		hashCode = (hashCode * 397) ^ (int)w;
		return hashCode;
	}
	
	@Override
	public Vector4 deepCopy() {
		return new Vector4(x, y, z, w);
	}
	
	@Override
	public Vector4 shallowCopy() {
		return new Vector4(x, y, z, w);
	}
}
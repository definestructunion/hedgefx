package com.hgm.fx.math;

import com.hgm.fx.util.BufferUtils;

import java.nio.ByteBuffer;

public class Matrix4
{
	public final float[] elements = new float[16];
	
	// todo
	private ByteBuffer buffer;
	public ByteBuffer getBuffer() {
		if(buffer == null)
			buffer = BufferUtils.toByteBuffer(elements);
		return buffer;
	}
	
	public Matrix4() {
		for(int i = 0; i < 16; ++i)
			elements[i] = 0;
	}
	
	public Matrix4(float diagonal) {
		for (int y = 0; y < 4; ++y)
			for (int x = 0; x < 4; ++x)
				elements[x + y * 4] = (x != y) ? 0 : diagonal;
	}
	
	public static Matrix4 identity() {
		return new Matrix4(1.0f);
	}
	
	public static Matrix4 ortho(float left, float right, float top, float bottom, float near, float far) {
		Matrix4 result = identity();
		
		result.elements[0 + 0 * 4] = 2.0f / (right - left);
		result.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		result.elements[2 + 2 * 4] = 2.0f / (near - far);
		
		result.elements[0 + 3 * 4] = (left + right) / (left - right);
		result.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		result.elements[2 + 3 * 4] = (far + near) / (far - near);
		
		return result;
	}
	
	public static Matrix4 trans(float x, float y, float z) {
		Matrix4 result = identity();
		
		result.elements[0 + 3 * 4] = x;
		result.elements[1 + 3 * 4] = y;
		result.elements[2 + 3 * 4] = z;
		
		return result;
	}
	
	public Vector3 mult(Vector3 other) {
		return new Vector3(
							   elements[0] * other.x + elements[4] * other.y + elements[8] * other.z + elements[12],
							   elements[1] * other.x + elements[5] * other.y + elements[9] * other.z + elements[13],
							   elements[2] * other.x + elements[6] * other.y + elements[10] * other.z + elements[14]
					   );
	}
	
	public Vector4 mult(Vector4 other) {
		return new Vector4(
							   elements[0] * other.x + elements[4] * other.y + elements[8] * other.z + elements[12] * other.w,
							   elements[1] * other.x + elements[5] * other.y + elements[9] * other.z + elements[13] * other.w,
							   elements[2] * other.x + elements[6] * other.y + elements[10] * other.z + elements[14] * other.w,
							   elements[3] * other.x + elements[7] * other.y + elements[11] * other.z + elements[15] * other.w
					   );
	}
	
	public Matrix4 mult(Matrix4 other) {
		float[] data = new float[4 * 4];
		
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				float sum = 0.0f;
				for (int e = 0; e < 4; e++) {
					sum += elements[x + e * 4] * other.elements[e + y * 4];
				}
				data[x + y * 4] = sum;
			}
		}
		
		for (int i = 0; i < data.length; i++) {
			elements[i] = data[i];
		}
		
		return this;
	}
	
	//public FloatBuffer toFloatBuffer()
	//{
	//	return BufferUtils.toFloatBuffer(elements);
	//}
}
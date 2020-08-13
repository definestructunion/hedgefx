package hedge.fx.util;

import java.nio.*;

public final class BufferUtils {
	
	public static FloatBuffer toFloatBuffer(float[] array) {
		FloatBuffer buffer = org.lwjgl.BufferUtils.createFloatBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer toIntBuffer(int[] array) {
		IntBuffer buffer = org.lwjgl.BufferUtils.createIntBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		return buffer;
	}
	
	public static ShortBuffer toShortBuffer(short[] array) {
		ShortBuffer buffer = org.lwjgl.BufferUtils.createShortBuffer(array.length);
		buffer.put(array);
		buffer.flip();
		return buffer;
	}
	
	public static ByteBuffer toByteBuffer(float[] array) {
		ByteBuffer buffer = org.lwjgl.BufferUtils.createByteBuffer(array.length * 4);
		for(float value : array) buffer.putFloat(value);
		buffer.flip();
		return buffer;
	}
	
	public static ByteBuffer toByteBuffer(short[] array) {
		ByteBuffer buffer = org.lwjgl.BufferUtils.createByteBuffer(array.length * 2);
		for(short value : array) buffer.putShort(value);
		buffer.flip();
		return buffer;
	}
	
	public static ByteBuffer createByteBuffer(int capacity) {
		return org.lwjgl.BufferUtils.createByteBuffer(capacity);
	}
	
	public static void disposeByteBuffer(ByteBuffer buffer) {
		buffer.clear();
	}
	
	public static void copy(float[] src, Buffer dst, int offset, int count) {
		if(dst instanceof ByteBuffer) {
			var byteBuffer = (ByteBuffer)dst;
			byteBuffer.position(offset);
			for(int i = 0; i < offset + count; ++i)
				byteBuffer.putFloat(src[i]);
			//for(float srcValue : src)
			//	byteBuffer.putFloat(srcValue);
		}
		else if(dst instanceof FloatBuffer) {
			var floatBuffer = (FloatBuffer)dst;
			floatBuffer.position(offset);
			for(int i = 0; i < offset + count; ++i)
				floatBuffer.put(src[i]);
			//for(float srcValue : src)
			//	floatBuffer.put(srcValue);
		}
		
		dst.rewind();
	}
	
	public static void copy(int[] src, Buffer dst, int offset, int count) {
		if(dst instanceof ByteBuffer) {
			var byteBuffer = (ByteBuffer)dst;
			byteBuffer.position(offset);
			for(int i = 0; i < offset + count; ++i)
				byteBuffer.putInt(src[i]);
			//for(float srcValue : src)
			//	byteBuffer.putFloat(srcValue);
		}
		else if(dst instanceof IntBuffer) {
			var intBuffer = (IntBuffer)dst;
			intBuffer.position(offset);
			for(int i = 0; i < offset + count; ++i)
				intBuffer.put(src[i]);
			//for(float srcValue : src)
			//	floatBuffer.put(srcValue);
		}
		
		dst.rewind();
	}
	
	public static void copy(short[] src, Buffer dst, int offset, int count) {
		if(dst instanceof ByteBuffer) {
			var byteBuffer = (ByteBuffer)dst;
			byteBuffer.position(offset);
			for(int i = 0; i < offset + count; ++i)
				byteBuffer.putShort(src[i]);
			//for(float srcValue : src)
			//	byteBuffer.putFloat(srcValue);
		}
		else if(dst instanceof ShortBuffer) {
			var shortBuffer = (ShortBuffer)dst;
			shortBuffer.position(offset);
			for(int i = 0; i < offset + count; ++i)
				shortBuffer.put(src[i]);
			//for(float srcValue : src)
			//	floatBuffer.put(srcValue);
		}
		
		dst.rewind();
	}
}

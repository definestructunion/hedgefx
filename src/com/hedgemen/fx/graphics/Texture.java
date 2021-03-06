package com.hedgemen.fx.graphics;

import com.hedgemen.fx.io.files.FileHandle;
import com.hedgemen.fx.util.BGFXResource;
import com.hedgemen.fx.util.tuples.Triplet;
import org.lwjgl.bgfx.BGFXMemory;

import java.nio.ByteBuffer;

import static org.lwjgl.bgfx.BGFX.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture implements BGFXResource {
	
	private short handle = 0;
	public short getHandle() { return handle; }
	
	private int width, height, bitsPerPixel;
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getBitsPerPixel() { return bitsPerPixel; }
	
	public Texture(short frameBufferHandle, int width, int height) {
		handle = bgfx_get_texture(frameBufferHandle, 0);
		this.width = width;
		this.height = height;
		bitsPerPixel = 4; // todo lol
	}
	
	public Texture(FileHandle file) {
		load(file);
	}
	
	Texture(int width, int height, ByteBuffer buffer) {
		this.width = width;
		this.height = height;
		this.bitsPerPixel = 4; //?
		
		BGFXMemory imageMem = bgfx_copy(buffer);
		
		handle = bgfx_create_texture_2d(this.width, this.height, false, 1, BGFX_TEXTURE_FORMAT_RGBA8,
				BGFX_SAMPLER_U_CLAMP | BGFX_SAMPLER_V_CLAMP |
				BGFX_SAMPLER_MIN_SHIFT | BGFX_SAMPLER_MAG_ANISOTROPIC, imageMem);
		
		/*handle = bgfx_create_texture_2d(this.width, this.height, false, 1, BGFX_TEXTURE_FORMAT_RGBA8,
				BGFX_SAMPLER_U_CLAMP | BGFX_SAMPLER_V_CLAMP |
						BGFX_SAMPLER_MIN_POINT | BGFX_SAMPLER_MAG_POINT, imageMem);*/
	}
	
	public static Triplet<ByteBuffer, Integer, Integer> loadData(FileHandle file) {
		int[] width = new int[1];
		int[] height = new int[1];
		int[] bitsPerPixel = new int[1];
		
		var buffer = stbi_load_from_memory(file.readByteBuffer(), width, height, bitsPerPixel, 4);
		return new Triplet<>(buffer, width[0], height[0]);
	}
	
	public static void freeData(Triplet<ByteBuffer, Integer, Integer> data) {
		stbi_image_free(data.getValue0());
	}
	
	private void load(FileHandle file) {
		int[] width = new int[1];
		int[] height = new int[1];
		int[] bitsPerPixel = new int[1];
		
		var buffer = stbi_load_from_memory(file.readByteBuffer(), width, height, bitsPerPixel, 4);
		this.width = width[0];
		this.height = height[0];
		this.bitsPerPixel = bitsPerPixel[0];
		
		BGFXMemory imageMem = bgfx_copy(buffer);
		
		handle = bgfx_create_texture_2d(this.width, this.height, false, 1, BGFX_TEXTURE_FORMAT_RGBA8,
				BGFX_SAMPLER_U_CLAMP | BGFX_SAMPLER_V_CLAMP |
						BGFX_SAMPLER_MIN_POINT | BGFX_SAMPLER_MAG_POINT, imageMem);
		
		stbi_image_free(buffer);
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_texture(handle);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Texture) {
			return handle == ((Texture) obj).handle;
		}
		
		return false;
	}
}

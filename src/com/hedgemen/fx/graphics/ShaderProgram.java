package com.hedgemen.fx.graphics;

import com.hedgemen.fx.io.files.FileHandle;
import com.hedgemen.fx.util.BGFXResource;
import org.lwjgl.bgfx.BGFXMemory;

import static org.lwjgl.bgfx.BGFX.*;

public class ShaderProgram implements BGFXResource {
	
	private short handle;
	public short getHandle() { return handle; }
	
	public ShaderProgram(FileHandle vertexFile, FileHandle fragmentFile) {
		
		BGFXMemory vertexMem;
		BGFXMemory fragmentMem;
		
		fragmentMem = bgfx_copy(fragmentFile.readByteBuffer());
		short fragmentShader = bgfx_create_shader(fragmentMem);
		
		vertexMem = bgfx_copy(vertexFile.readByteBuffer());
		short vertexShader = bgfx_create_shader(vertexMem);
		
		handle = bgfx_create_program(vertexShader, fragmentShader, true);
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_program(handle);
	}
}
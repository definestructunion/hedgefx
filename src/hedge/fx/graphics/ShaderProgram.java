package hedge.fx.graphics;

import hedge.fx.io.files.FileHandle;
import hedge.fx.util.BGFXResource;
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
		
		handle = bgfx_create_program(vertexShader, fragmentShader, false);
		
		bgfx_destroy_program(vertexShader);
		bgfx_destroy_program(fragmentShader);
	}
	
	@Override
	public void dispose() {
		bgfx_destroy_program(handle);
	}
}
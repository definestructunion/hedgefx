package com.hedgemen.fx.io.files;

import com.hedgemen.fx.util.tuples.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarShaderFolderHandle extends FileHandle {
	
	/*private*/ JarFile jarFile;
	private String shaderFolder;
	private HashMap<Pair<String, String>, JarFileHandle> shaderFiles;
	
	public JarShaderFolderHandle(FileHandle jarFile, String shaderFolder) {
		create(jarFile, shaderFolder);
	}
	
	private void create(FileHandle jarFile, String shaderFolder) {
		
		try {
			this.jarFile = new JarFile(jarFile.file);
		} catch(IOException e) { e.printStackTrace(); }
		
		this.shaderFolder = shaderFolder;
		file = jarFile.file;
		type = FileType.Jar;
		shaderFiles = new HashMap<>();
		gatherShaders();
	}
	
	private void gatherShaders() {
		var entries = jarFile.entries();
		
		for(JarEntry entry; entries.hasMoreElements() && (entry = entries.nextElement()) != null; ) {
			String entryName = entry.getRealName();
			
			if(entryName.startsWith(shaderFolder) && !entry.isDirectory()) {
				String entryFileName = entryName.substring(entryName.lastIndexOf('/') + 1);
				String entryBackendName = entryName.substring(shaderFolder.length() + 1).substring(0, entryName.indexOf('/'));
				
				var fileHandle = new JarFileHandle(this, entry);
				shaderFiles.put(new Pair<>(entryBackendName, entryFileName), fileHandle);
			}
		}
	}
	
	/*public InputStream readShader(String shaderName) {
		return readShader(shaderName, GameContext.get().graphicsDevice().getBackend());
	}
	
	public InputStream readShader(String shaderName, GraphicsAPI backend) {
		String backendName = backend.name;
		var pair = new Pair<String, String>(backendName, shaderName);
		
		var fileHandle = shaderFiles.get(pair);
		return fileHandle.read();
	}*/
}

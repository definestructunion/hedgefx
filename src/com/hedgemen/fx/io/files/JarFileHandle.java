package com.hedgemen.fx.io.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarFileHandle extends FileHandle {
	
	private static final String JarExtension;
	
	static {
		JarExtension = ".jar";
	}
	
	protected JarFile jarFile;
	protected JarEntry jarEntry;
	
	/**
	 * Creates a jar file handle from a single String, being jarfilepath:entryinsidejar.txt, the jar file path doesn't need to include .jar at the end
	 *
	 * @param jarFileEntryCompound example: mods/demomod:assets/exampletexture.png
	 */
	public JarFileHandle(String jarFileEntryCompound) {
		String[] jarFileEntryPieces = jarFileEntryCompound.split(":");
		
		if (jarFileEntryPieces.length != 2)
			throw new RuntimeException("\"" + jarFileEntryCompound + "\" must have 1 semicolon");
		
		create(new File(jarFileEntryPieces[0]), jarFileEntryPieces[1]);
	}
	
	public JarFileHandle(FileHandle jarFile, String jarFileEntry) {
		create(jarFile.file(), jarFileEntry);
	}
	
	public JarFileHandle(File jarFile, String jarFileEntry) {
		create(jarFile, jarFileEntry);
	}
	
	public JarFileHandle(JarShaderFolderHandle jarShaderFolder, JarEntry jarEntry) {
		jarFile = jarShaderFolder.jarFile;
		this.jarEntry = jarEntry;
		file = jarShaderFolder.file;
		type = jarShaderFolder.type;
	}
	
	private void create(File jarFile, String jarFileEntry) {
		try {
			this.jarFile = new JarFile(jarFile);
		} catch (IOException e) { e.printStackTrace(); }
		
		jarEntry = this.jarFile.getJarEntry(jarFileEntry);
		file = jarFile;
		type = FileType.Jar;
	}
	
	@Override
	public InputStream read() {
		try {
			return jarFile.getInputStream(jarEntry);
		} catch (IOException e) { e.printStackTrace(); }
		
		return null;
	}
}
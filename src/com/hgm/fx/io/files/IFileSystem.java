package com.hgm.fx.io.files;

public interface IFileSystem {
	String getExternalStoragePath();
	
	FileHandle classpath(String path);
	FileHandle internal(String path);
	FileHandle external(String path);
	FileHandle absolute(String path);
	FileHandle local(String path);
	FileHandle jar(String path);
}

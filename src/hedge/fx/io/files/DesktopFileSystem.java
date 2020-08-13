package hedge.fx.io.files;

import java.io.File;

public class DesktopFileSystem implements IFileSystem {
	String externalPath;
	String localPath;
	
	public DesktopFileSystem() {
		externalPath = System.getProperty("user.home") + File.separator;
		localPath = (new File("")).getAbsolutePath() + File.separator;
	}
	
	@Override
	public String getExternalStoragePath() {
		return externalPath;
	}
	
	@Override
	public FileHandle classpath(String path) {
		return new FileHandle(path, FileType.Classpath);
	}
	
	@Override
	public FileHandle internal(String path) {
		return new FileHandle(path, FileType.Internal);
	}
	
	@Override
	public FileHandle external(String path) {
		return new FileHandle(path, FileType.External);
	}
	
	@Override
	public FileHandle absolute(String path) {
		return new FileHandle(path, FileType.Absolute);
	}
	
	@Override
	public FileHandle local(String path) {
		return new FileHandle(path, FileType.Local);
	}
	
	@Override
	public FileHandle jar(String path) {
		return new FileHandle(path, FileType.Jar);
	}
}

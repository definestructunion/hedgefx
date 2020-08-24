package com.hgm.fx.app;

import com.hgm.fx.platform.PlatformAPI;
import com.hgm.fx.util.logging.ILogger;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Library;

import java.io.File;

public final class GameInitializer {
	
	private GameConfiguration config;
	public GameConfiguration getConfig() { return config; }
	
	private ILogger logger;
	public ILogger getLogger() { return logger; }
	
	private PlatformAPI platform;
	public PlatformAPI getPlatform() { return platform; }
	
	public GameInitializer(GameConfiguration config, ILogger logger) {
		this.config = config;
		this.logger = logger;
		this.platform = PlatformAPI.getPlatform();
		initializeNativeLibraries();
	}
	
	private void initializeNativeLibraries() {
		// switch to this to have no extractions
		//Configuration.LIBRARY_PATH.set(new File("runtime/hedgefx").getAbsolutePath());
		Configuration.SHARED_LIBRARY_EXTRACT_DIRECTORY.set(new File("runtime/hedgefx").getAbsolutePath());
		Configuration.SHARED_LIBRARY_EXTRACT_PATH.set(new File("runtime/hedgefx").getAbsolutePath());
		Library.initialize();
	}
}

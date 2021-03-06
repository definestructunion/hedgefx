package com.hedgemen.fx.app;

import com.hedgemen.fx.platform.PlatformAPI;
import com.hedgemen.fx.util.logging.Logger;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Library;

import java.io.File;

public final class GameInitializer {
	
	private GameConfiguration config;
	public GameConfiguration getConfig() { return config; }
	
	private Logger logger;
	public Logger getLogger() { return logger; }
	
	private PlatformAPI platform;
	public PlatformAPI getPlatform() { return platform; }
	
	public GameInitializer(GameConfiguration config, Logger logger) {
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

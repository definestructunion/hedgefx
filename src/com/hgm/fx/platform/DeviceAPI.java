package com.hgm.fx.platform;

import java.util.Arrays;

public enum DeviceAPI {
	
	Desktop(PlatformAPI.Windows, PlatformAPI.Linux, PlatformAPI.Unix, PlatformAPI.MacOS),
	Mobile(PlatformAPI.Android, PlatformAPI.iOS),
	Console(PlatformAPI.XBox, PlatformAPI.Playstation, PlatformAPI.Switch),
	Other();
	
	private final PlatformAPI[] apis;
	
	public PlatformAPI[] getApis() {
		return  Arrays.copyOf(apis, apis.length);
	}
	
	private DeviceAPI(PlatformAPI... apis) {
		this.apis = apis;
	}
}

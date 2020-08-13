package hedge.fx.platform;

import java.util.Arrays;

import static hedge.fx.platform.PlatformAPI.*;

public enum DeviceAPI {
	
	Desktop(Windows, Linux, Unix, MacOS),
	Mobile(Android, iOS),
	Console(XBox, Playstation, Switch),
	Other();
	
	private final PlatformAPI[] apis;
	
	public PlatformAPI[] getApis() {
		return  Arrays.copyOf(apis, apis.length);
	}
	
	private DeviceAPI(PlatformAPI... apis) {
		this.apis = apis;
	}
}

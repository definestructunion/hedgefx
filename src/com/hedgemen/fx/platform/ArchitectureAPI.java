package com.hedgemen.fx.platform;

public enum ArchitectureAPI {
	x86_64("x64"),
	x86("x86"),
	arm64("arm64"),
	arm86("arm86");
	
	public final String fileSuffix;
	
	private ArchitectureAPI(String fileSuffix) {
		this.fileSuffix = fileSuffix;
	}
	
	public static ArchitectureAPI detect() {
		String jvmArch = System.getProperty("os.arch").toLowerCase();
		
		if(jvmArch.equals(x86_64.fileSuffix)) return x86_64;
		if(jvmArch.equals(x86.fileSuffix)) return x86;
		if(jvmArch.equals(arm64.fileSuffix)) return arm64;
		if(jvmArch.equals(arm86.fileSuffix)) return arm86;
		
		throw new RuntimeException("Cannot determine JVM architecture");
	}
}

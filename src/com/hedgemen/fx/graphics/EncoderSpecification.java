package com.hedgemen.fx.graphics;

public class EncoderSpecification {
	
	private boolean multithreaded;
	public boolean isMultithreaded() { return multithreaded; }
	
	private long state;
	public long getState() { return state; }
	public void setState(long value) { state = value; }
	
	private boolean debug;
	public boolean isDebug() { return debug; }
	
	public EncoderSpecification(boolean multithreaded, boolean debug, long state) {
		this.multithreaded = multithreaded;
		this.debug = debug;
		this.state = state;
	}
}

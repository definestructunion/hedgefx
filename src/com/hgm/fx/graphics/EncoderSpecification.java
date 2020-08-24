package com.hgm.fx.graphics;

public class EncoderSpecification {
	
	private boolean multithreaded;
	public boolean isMultithreaded() { return multithreaded; }
	
	private long state;
	public long getState() { return state; }
	public void setState(long value) { state = value; }
	
	public EncoderSpecification(boolean multithreaded, long state) {
		this.multithreaded = multithreaded;
		this.state = state;
	}
}

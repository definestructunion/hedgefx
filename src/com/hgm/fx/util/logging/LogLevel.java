package com.hgm.fx.util.logging;

public enum LogLevel {
	
	Debug(2, "\u001B[42m"),
	Warn(3, "\u001B[43m"),
	Error(4, "\u001B[41m"),
	Critical(5,"\u001B[35m"),
	Off(5, "\u001B[42m");
	
	public static final String ANSI_RESET = "\u001B[0m";
	
	public final int level;
	public final String ansiColour;
	
	private LogLevel(int level, String ansiColour) {
		this.level = level;
		this.ansiColour = ansiColour;
	}
}
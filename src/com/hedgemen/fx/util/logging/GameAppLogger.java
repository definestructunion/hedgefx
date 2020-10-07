package com.hedgemen.fx.util.logging;

import com.hedgemen.fx.io.files.FileHandle;
import com.hedgemen.fx.math.Mathf;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameAppLogger implements Logger {
	
	private DateTimeFormatter dtf;
	private StringBuilder log;
	
	private String pattern;
	public void setPattern(String value) { pattern = value; }
	
	private boolean colorOutput;
	public void setColorOutput(boolean value) { colorOutput = value; }
	
	private LogLevel logLevel;
	public LogLevel getLogLevel() { return logLevel; }
	public void setLogLevel(LogLevel value) { logLevel = value; }
	
	public GameAppLogger() {
		this(LogLevel.Debug, "%S | %T | %M | %c | %m | %l", false);
	}
	
	public GameAppLogger(LogLevel logLevel, String pattern, boolean colorOutput) {
		dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		log = new StringBuilder();
		
		this.logLevel = logLevel;
		this.pattern = pattern;
		this.colorOutput = colorOutput;
	}
	
	@Override
	public void debug(String message) {
		add(LogLevel.Debug, message, 1);
	}
	
	@Override
	public void warn(String message) {
		add(LogLevel.Warn, message, 1);
	}
	
	@Override
	public void error(String message) {
		add(LogLevel.Error, message, 1);
	}
	
	@Override
	public void critical(String message) {
		add(LogLevel.Critical, message, 1);
	}
	
	@Override
	public void debug(String message, int traceSteps) {
		add(LogLevel.Debug, message, traceSteps + 1);
	}
	
	@Override
	public void warn(String message, int traceSteps) {
		add(LogLevel.Warn, message, traceSteps + 1);
	}
	
	@Override
	public void error(String message, int traceSteps) {
		add(LogLevel.Error, message, traceSteps + 1);
	}
	
	@Override
	public void critical(String message, int traceSteps) {
		add(LogLevel.Critical, message, traceSteps + 1);
	}
	
	@Override
	public void add(LogLevel logLevel, String message) {
		if(this.logLevel.level > logLevel.level)
			return;
		
		String formattedMessage = pattern;
		int baseTraceSteps = 2;
		StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[baseTraceSteps];
		
		formattedMessage = formattedMessage
								   .replace("%S", logLevel.toString())
								   .replace("%T", dtf.format(LocalDateTime.now()))
								   .replace("%c", stackTrace.getClassName())
								   .replace("%n", stackTrace.getFileName())
								   .replace("%m", stackTrace.getMethodName())
								   .replace("%l", Integer.toString(stackTrace.getLineNumber()))
								   .replace("%M", message);
		
		
		println(logLevel, formattedMessage);
		log.append(formattedMessage ).append('\n');
	}
	
	@Override
	public void add(LogLevel logLevel, String message, int traceSteps) {
		if(this.logLevel.level > logLevel.level)
			return;
		
		String formattedMessage = pattern;
		int baseTraceSteps = 2;
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		traceSteps = Mathf.clamp(baseTraceSteps + traceSteps, baseTraceSteps, stackTraces.length - 1);
		StackTraceElement stackTrace = stackTraces[traceSteps];
		
		formattedMessage = formattedMessage
								   .replace("%S", logLevel.toString())
								   .replace("%T", dtf.format(LocalDateTime.now()))
								   .replace("%c", stackTrace.getClassName())
								   .replace("%n", stackTrace.getFileName())
								   .replace("%m", stackTrace.getMethodName())
								   .replace("%l", Integer.toString(stackTrace.getLineNumber()))
								   .replace("%M", message);
		
		
		println(logLevel, formattedMessage);
		log.append(formattedMessage ).append('\n');
	}
	
	@Override
	public void write(FileHandle file) {
		file.writeString(log.toString(), false, "UTF-8");
	}
	
	private void println(LogLevel logLevel, String message) {
		if(colorOutput)
			System.out.println(logLevel.ansiColour + message + LogLevel.ANSI_RESET);
		else
			System.out.println(message);
	}
	
	private void print(LogLevel LogLevel, String message) {
		if(colorOutput)
			System.out.print(logLevel.ansiColour + message + com.hedgemen.fx.util.logging.LogLevel.ANSI_RESET);
		else
			System.out.print(message);
	}
}

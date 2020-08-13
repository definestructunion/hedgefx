package hedge.fx.util.logging;

import hedge.fx.io.files.FileHandle;

public interface ILogger {
	
	void setPattern(String value);
	void setColorOutput(boolean value);
	
	LogLevel getLogLevel();
	void setLogLevel(LogLevel value);
	
	void debug(String message);
	void warn(String message);
	void error(String message);
	void critical(String message);
	
	void debug(String message, int traceSteps);
	void warn(String message, int traceSteps);
	void error(String message, int traceSteps);
	void critical(String message, int traceSteps);
	
	void add(LogLevel logLevel, String message);
	void add(LogLevel logLevel, String message, int traceSteps);
	
	void write(FileHandle file);
}

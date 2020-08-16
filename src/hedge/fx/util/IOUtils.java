package hedge.fx.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static hedge.fx.util.BufferUtils.createByteBuffer;

public final class IOUtils {
	
	public static final int DEFAULT_BUFFER_SIZE = 4096;
	public static final byte[] EMPTY_BYTES = new byte[0];
	
	public static void closeQuietly(Closeable closeable) {
		try {
			closeable.close();
		} catch(Throwable throwable) { }
	}
	
	public static String inputStreamToString(InputStream stream) {
		return inputStreamToString(stream, StandardCharsets.UTF_8);
	}
	
	public static String inputStreamToString(InputStream stream, Charset charset) {
		try {
			final int bufferSize = 1024;
			final char[] buffer = new char[bufferSize];
			final StringBuilder out = new StringBuilder();
			Reader in = new InputStreamReader(stream, StandardCharsets.UTF_8);
			int charsRead;
			while((charsRead = in.read(buffer, 0, buffer.length)) > 0) {
				out.append(buffer, 0, charsRead);
			}
			return out.toString();
		} catch(IOException e) { e.printStackTrace(); }
		
		return "";
	}
}

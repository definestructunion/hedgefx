package com.hedgemen.fx.util;

public interface Disposable extends AutoCloseable {
	
	void dispose();
	
	@Override
	default void close() {
		dispose();
	}
}

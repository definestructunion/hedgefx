package com.hedgemen.fx.platform;

import java.net.URL;
import java.net.URLClassLoader;

public class HedgeClassLoader extends URLClassLoader {

	public HedgeClassLoader() {
		super(new URL[0], HedgeClassLoader.class.getClassLoader());
	}
	
	public HedgeClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
	}
	
	@Override
	public void addURL(URL url) {
		super.addURL(url);
	}
}

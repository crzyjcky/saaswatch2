package edu.sjsu.comp295b.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
	
	private static final Logger logger = LoggerFactory.getLogger(Config.class);
	private final String LIBRARY_PROPERTIES_FILE = "library.properties";
	private Properties libraryProperties = new Properties();
	private boolean isDebugEnabled;
	private int keepAliveInterval; // in seconds
	
	private List<IConfigListener> listeners = new ArrayList<IConfigListener>();
	
	public Config() throws IOException  {
		
		//logger.debug("path:" + new File(LIBRARY_PROPERTIES_FILE).getAbsolutePath());
		//Thread.currentThread().getContextClassLoader().getResourceAsStream("filename.properties");
		//libraryProperties.load(new FileInputStream(LIBRARY_PROPERTIES_FILE));
		libraryProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(LIBRARY_PROPERTIES_FILE));
		
		
		if (libraryProperties.getProperty("debugEnabled") == null) {
			
			// by default, set debug enabled = true
			setDebugEnabled(true);
		} else {
			
			isDebugEnabled = Boolean.parseBoolean(libraryProperties.getProperty("debugEnabled"));
		}
		
		logger.debug("config: " + isDebugEnabled);
		
		if (libraryProperties.getProperty("keepAliveInterval") == null || "0".equals(libraryProperties.getProperty("keepAliveInterval"))) {
			
			// by default, set keepAliveInterval = 10;
			setKeepAliveInterval(10);
		} else {
			
			keepAliveInterval = Integer.parseInt(libraryProperties.getProperty("keepAliveInterval"));
		}
	}

	public boolean isDebugEnabled() {

		return isDebugEnabled;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {
		
		libraryProperties.setProperty("debugEnabled", isDebugEnabled + "");
		
		try {
			
			libraryProperties.store(new FileOutputStream(LIBRARY_PROPERTIES_FILE), null);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		this.isDebugEnabled = isDebugEnabled;
	}

	public int getKeepAliveInterval() {
		
		logger.debug("getKeepAliveInterval:"+ keepAliveInterval);
		
		return keepAliveInterval;
	}

	public void setKeepAliveInterval(int keepAliveInterval) {
		
		libraryProperties.setProperty("keepAliveInterval", keepAliveInterval + "");
		
		try {
			
			libraryProperties.store(new FileOutputStream(LIBRARY_PROPERTIES_FILE), null);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		this.keepAliveInterval = keepAliveInterval;
		
		broadcast("onKeepAliveIntervalChange", keepAliveInterval);
	}	
	
	public void addListener(IConfigListener listener) {
		
		listeners.add(listener);
	}
	
	public void removeListener(IConfigListener listener) {
		
		listeners.remove(listener);
	}
	
	public void broadcast(String eventname, Object data) {
		
		if ("onKeepAliveIntervalChange".equals(eventname)) {
			
			for (IConfigListener listener : listeners) {
				
				listener.onKeepAliveIntervalChange(data);
			}
		}
	}
}

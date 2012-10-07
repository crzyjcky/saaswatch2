package edu.sjsu.comp295b.config;

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

public class LibraryConfig {
	
	private static final Logger logger = LoggerFactory.getLogger(LibraryConfig.class);
	private final String LIBRARY_PROPERTIES_FILE = "library.properties";
	private final int DEFAULT_KEEP_ALIVE_INTERVAL = 10; // 10 s
	private Properties libraryProperties = new Properties();
	private boolean isDebugEnabled;
	private int keepAliveInterval; // in seconds
	
	private ILibraryConfigListener listener;
	
	
	public LibraryConfig() throws IOException  {
		
		libraryProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(LIBRARY_PROPERTIES_FILE));
		
		if (libraryProperties.getProperty("library.debugEnabled") == null) {
			
			setDebugEnabled(true);
		} else {
			
			isDebugEnabled = Boolean.parseBoolean(libraryProperties.getProperty("library.debugEnabled"));
		}
		
		logger.debug("config: " + isDebugEnabled);
		
		if (libraryProperties.getProperty("library.keepAliveInterval") == null || "0".equals(libraryProperties.getProperty("library.keepAliveInterval"))) {
			
			// by default, set keepAliveInterval = 10;
			setKeepAliveInterval(DEFAULT_KEEP_ALIVE_INTERVAL);
		} else {
			
			keepAliveInterval = Integer.parseInt(libraryProperties.getProperty("library.keepAliveInterval"));
		}
	}

	public void saveConfig(Properties newConfig) {
		
		logger.debug("saveConfig.newConfig");
		
		for (Object key : newConfig.keySet()) {
			
			if (newConfig.get(key) != null) {
				
				if (!newConfig.get(key).equals(libraryProperties.get(key))) {
					
					if (key.equals("library.debugEnabled")) {
						
						setDebugEnabled(Boolean.parseBoolean((String) newConfig.get(key)));
					} else if (key.equals("library.keepAliveInterval")) {
						
						setKeepAliveInterval(Integer.parseInt((String) newConfig.get(key)));
					}
				}
			}
		}

		try {
			
			libraryProperties.store(new FileOutputStream(LIBRARY_PROPERTIES_FILE), null);
		} catch (FileNotFoundException e) {

			logger.debug("saveConfig", e);
		} catch (IOException e) {

			logger.debug("saveConfig", e);
		}
	}
	
	public boolean isDebugEnabled() {

		return isDebugEnabled;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {
		
		libraryProperties.setProperty("library.debugEnabled", isDebugEnabled + "");
		
		this.isDebugEnabled = isDebugEnabled;
	}

	public int getKeepAliveInterval() {
		
		logger.debug("getKeepAliveInterval:"+ keepAliveInterval);
		
		return keepAliveInterval;
	}

	public void setKeepAliveInterval(int keepAliveInterval) {
		
		this.keepAliveInterval = keepAliveInterval;
		
		listener.onKeepAliveIntervalChange(keepAliveInterval);
	}	
	
	public void setListener(ILibraryConfigListener listener) {
		
		this.listener = listener;
	}
}

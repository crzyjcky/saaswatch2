package edu.sjsu.comp295b.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Library;

public class DebugLogger {

	private static final Logger logger = LoggerFactory.getLogger(DebugLogger.class);
	private Library library;
	private String classname;
	
	public DebugLogger(Library library, String classname) {
		
		this.library = library;
		this.classname = classname;
	}
	
	public void debug(String debugString) {
		
		library.debug(System.currentTimeMillis() + "\t" + classname + " - " + debugString);
	}
	
}

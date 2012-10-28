package edu.sjsu.comp295b.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Library;
import edu.sjsu.comp295b.dto.DebugDTO;

public class DebugLogger {

	private static final Logger logger = LoggerFactory.getLogger(DebugLogger.class);
	private Library library;
	private String className;
	
	public DebugLogger(Library library, String className) {
		
		this.library = library;
		this.className = className;
	}
	
	public void debug(String debugString) {
		
		DebugDTO debugDTO = new DebugDTO();
		debugDTO.timestamp = System.currentTimeMillis();
		debugDTO.data = debugString;
		debugDTO.className = className;
		
		library.debug(debugDTO);
	}
	
}

package edu.sjsu.comp295b.servercontext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Library;

public class LibraryContextListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(LibraryContextListener.class);
	
	Library library;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {

		logger.debug("contextDestroyed");

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		logger.debug("contextInitialized");
		library = Library.getInstance();

	}

}

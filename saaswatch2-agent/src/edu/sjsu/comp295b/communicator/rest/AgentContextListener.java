package edu.sjsu.comp295b.communicator.rest;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Agent;

public class AgentContextListener implements ServletContextListener {

	private static final Logger logger = LoggerFactory.getLogger(AgentContextListener.class);
	
	private Agent agent;
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent contextEvent) {

		try {
			
			agent = Agent.getInstance();
		} catch (IOException e) {

			logger.debug("contextInitialized", e);
		}
		
		ServletContext context = contextEvent.getServletContext();
		context.setAttribute("agent", agent);
	}

}

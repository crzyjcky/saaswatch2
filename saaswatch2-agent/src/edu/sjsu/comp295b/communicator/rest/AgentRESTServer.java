package edu.sjsu.comp295b.communicator.rest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentRESTServer {
	
	private static final Logger logger = LoggerFactory.getLogger(AgentRESTServer.class);
	
	private static int DEFAULT_PORT = 8080;
	
	private Server server;

	public void start() throws Exception {
		
		start(DEFAULT_PORT);
	}
	
	public void start(int port) throws Exception {

		logger.info("start");
		
		server = new Server(port);
		
		/*
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		
		context.addServlet(new ServletHolder(new HelloServlet()), "/*");
		*/
		
		WebAppContext context = new WebAppContext();
		
        context.setDescriptor("./src/edu/sjsu/comp295b/communicator/rest/web.xml");
        context.setResourceBase("./src/edu/sjsu/comp295b/communicator/rest");
        context.setContextPath("/");
        context.setParentLoaderPriority(true);   

        server.setHandler(context);
		server.start();
	}
	
	public void stop() throws Exception {
		
		logger.info("stop");
		
		server.stop();
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		AgentRESTServer agentRESTServer = new AgentRESTServer();
		agentRESTServer.start(8088);
		
		logger.debug("after start");
		Thread.sleep(10000);
		
		//agentREST.stop();
	}
}

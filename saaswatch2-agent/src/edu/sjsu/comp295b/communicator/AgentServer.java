package edu.sjsu.comp295b.communicator;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Agent;

public class AgentServer implements AgentServerMBean {

	private static final Logger logger = LoggerFactory.getLogger(AgentServer.class);
	
	private Agent agent;
	
	public AgentServer(Agent agent) {
		
		this.agent = agent;
	}
	
	@Override
	public void saveConfig(Properties newConfig) {
		
		logger.debug("saveConfig: " + newConfig);
	}

	@Override
	public void setStatusReportInterval(int statusReportInterval) {
		
		logger.debug("setStatusReportInterval: " + statusReportInterval);
	}

	@Override
	public void setDataFlushInterval(int dataFlushInterval) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRESTEnabled(boolean isRESTEnabled) {
		// TODO Auto-generated method stub
		
		logger.debug("setRESTEnabled: " + isRESTEnabled);
	}

	@Override
	public void setDebugEnabled(boolean isDebugEnabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setKeepAliveInterval(int keepAliveInterval) {
		// TODO Auto-generated method stub
		
	}

}

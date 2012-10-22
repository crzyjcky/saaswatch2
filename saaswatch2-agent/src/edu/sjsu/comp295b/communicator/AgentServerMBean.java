package edu.sjsu.comp295b.communicator;

import java.util.Properties;

public interface AgentServerMBean {
	
	public void saveConfig(Properties newConfig);
	
	// report agent and library status to server
	public void setStatusReportInterval(int statusReportInterval);
	// data flush to serverl
	public void setDataFlushInterval(int dataFlushInterval);
	// enable agent rest interface
	public void setRESTEnabled(boolean isRESTEnabled);
	
	/*
	agent.appResourceProbingInterval=10
	agent.appConnectivityProbingInterval=10
	agent.appStatusProbingInterval=10
	agent.agentResourceProbingInterval=10
	agent.agentConnectivityProbingInterval=10
	agent.agentStatusProbingInterval=10
	*/
	
	// libaray
	public void setDebugEnabled(boolean isDebugEnabled);
	public void setKeepAliveInterval(int keepAliveInterval);

}

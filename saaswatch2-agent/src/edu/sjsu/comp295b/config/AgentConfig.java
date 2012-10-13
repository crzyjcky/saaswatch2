package edu.sjsu.comp295b.config;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.communicator.AgentLibraryCommunicator;

public class AgentConfig {

	private static final Logger logger = LoggerFactory.getLogger(AgentConfig.class);
	private final String AGENT_PROPERTIES_FILE = "agent.properties";
	private final String AGENT_PREFIX = "agent.";
	private final String LIBRARY_PREFIX = "library.";
	private final int DEFAULT_AGENT_STATUS_PROBING_INTERVAL = 10; // agent status probing is not allowed to turn off. 
	
	private Properties agentProperties = new Properties();
	private IAgentConfigListener listener;
	private AgentLibraryCommunicator agentLibraryCommunicator;
	
	// application probing
	private int appResourceProbingInterval;
	private int appConnectivityProbingInterval;
	private int appStatusProbingInterval;
	
	// agent probing
	private int agentResourceProbingInterval;
	private int agentConnectivityProbingInterval;
	private int agentStatusProbingInterval;
	
	// rest
	private boolean isAgentRESTEnabled;
	
	public AgentConfig(IAgentConfigListener listener, AgentLibraryCommunicator agentLibraryCommunicator) throws IOException {
		
		setListener(listener);
		
		this.agentLibraryCommunicator = agentLibraryCommunicator;
		
		agentProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(AGENT_PROPERTIES_FILE));
		
		setAppResourceProbingInterval(Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "appResourceProbingInterval")));
		setAppConnectivityProbingInterval(Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "appConnectivityProbingInterval")));
		setAgentResourceProbingInterval(Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "agentResourceProbingInterval")));
		setAgentConnectivityProbingInterval(Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "agentConnectivityProbingInterval")));
		setAgentConnectivityProbingInterval(Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "agentStatusProbingInterval")));
	
		// TODO: REST
		logger.debug("constructor");
	}
	
	public void saveConfig(Properties newConfig) {
		
		logger.debug("saveConfig.newConfig");
		// TODO: check each new properties.

		// TODO: store new properties.
		
		// TODO: check if library properties in new Config
		Properties libraryConfig = new Properties();
		for (Object key : newConfig.keySet()) {
			
			if (((String) key).startsWith(LIBRARY_PREFIX)) {
				
				libraryConfig.setProperty((String) key, newConfig.getProperty((String) key));
			}
		}
		
		logger.debug("saveConfig.libraryConfig");
		if (!libraryConfig.isEmpty()) {
			
			logger.debug("saveConfig.libraryConfig2");
			agentLibraryCommunicator.saveConfig(libraryConfig);
		}
		
		
	}

	public boolean isAppResourceProbingEnabled() {
		
		return appResourceProbingInterval > 0;
	}

	public boolean isAppConnectivityProbingEnabled() {
		
		return appConnectivityProbingInterval > 0;
	}

	public int getAppResourceProbingInterval() {
		
		return appResourceProbingInterval;
	}


	public void setAppResourceProbingInterval(int appResourceProbingInterval) {
		
		this.appResourceProbingInterval = appResourceProbingInterval;
		
		if (appResourceProbingInterval > 0) {
			
			listener.onAppResourceProbingEnabledChanged(true);
			listener.onAppResourceProbingIntervalChanged(appResourceProbingInterval);
		} else {
			
			listener.onAppResourceProbingEnabledChanged(false);
		}	
	}


	public int getAppConnectivityProbingInterval() {
		
		return appConnectivityProbingInterval;
	}

	public void setAppConnectivityProbingInterval(int appConnectivityProbingInterval) {
		
		this.appConnectivityProbingInterval = appConnectivityProbingInterval;
		
		if (appConnectivityProbingInterval > 0) {
			
			listener.onAppConnectivityProbingEnabledChanged(true);
			listener.onAppConnectivityProbingIntervalChanged(appConnectivityProbingInterval);
		} else {
			
			listener.onAppConnectivityProbingEnabledChanged(false);
		}
	}

	public boolean isAgentResourceProbingEnabled() {
		
		return agentResourceProbingInterval > 0;
	}

	public boolean isAgentConnectivityProbingEnabled() {
		
		return agentConnectivityProbingInterval > 0;
	}

	public int getAgentResourceProbingInterval() {
		
		return agentResourceProbingInterval;
	}

	public void setAgentResourceProbingInterval(int agentResourceProbingInterval) {
		
		this.agentResourceProbingInterval = agentResourceProbingInterval;
		
		if (agentResourceProbingInterval > 0) {
			
			listener.onAgentResourceProbingEnabledChanged(true);
			listener.onAgentResourceProbingIntervalChanged(agentResourceProbingInterval);
		} else {
			
			listener.onAgentResourceProbingEnabledChanged(false);
		}
	}


	public int getAgentConnectivityProbingInterval() {
		
		return agentConnectivityProbingInterval;
	}


	public void setAgentConnectivityProbingInterval(
			int agentConnectivityProbingInterval) {

		this.agentConnectivityProbingInterval = agentConnectivityProbingInterval;
		
		if (agentConnectivityProbingInterval > 0) {
			
			listener.onAgentConnectivityProbingEnabledChanged(true);
			listener.onAgentConnectivityProbingIntervalChanged(agentConnectivityProbingInterval);
		} else {
			
			listener.onAgentConnectivityProbingEnabledChanged(false);
		}
	}

	public int getAgentStatusProbingInterval() {
		
		return agentStatusProbingInterval;
	}

	public void setAgentStatusProbingInterval(int agentStatusProbingInterval) {
		
		this.agentStatusProbingInterval = agentStatusProbingInterval;
		
		if (agentStatusProbingInterval > 0) {
			
			listener.onAgentStatusProbingIntervalChanged(agentStatusProbingInterval);
		} else {
			
			// agent status probing is not allowed to turn off.
			listener.onAgentStatusProbingIntervalChanged(DEFAULT_AGENT_STATUS_PROBING_INTERVAL);
		}
	}
	
	public boolean isRESTEnabled() {
		
		return isAgentRESTEnabled;
	}
	
	public void setRESTEnabled(boolean isRESTEnabled) {
		
		this.isAgentRESTEnabled = isRESTEnabled;
		listener.onAgentRESTEnabledChanged(isRESTEnabled);
	}
	
	public void setListener(IAgentConfigListener listener) {
		
		this.listener = listener;
	}
}

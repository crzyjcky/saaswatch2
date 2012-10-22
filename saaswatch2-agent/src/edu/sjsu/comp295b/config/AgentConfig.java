package edu.sjsu.comp295b.config;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.communicator.AgentLibraryClient;

public class AgentConfig {

	private static final Logger logger = LoggerFactory.getLogger(AgentConfig.class);
	private final String AGENT_PROPERTIES_FILE = "agent.properties";
	public static final String AGENT_PREFIX = "agent.";
	public static final String LIBRARY_PREFIX = "library.";
	private final int DEFAULT_AGENT_STATUS_PROBING_INTERVAL = 10; // agent status probing is not allowed to turn off. 
	
	private Properties agentProperties = new Properties();
	private IAgentConfigListener listener;
	private AgentLibraryClient agentLibraryClient;
	
	// application probing
	private int appMemoryProbingInterval;
	private int appOSProbingInterval;
	private int appResourceProbingInterval;
	private int appConnectivityProbingInterval;
	
	// agent jmx
	private String agentJMXServiceURL;
	private int agentJMXReconnectInterval;
	
	// agent probing
	private int agentMemoryProbingInterval;
	private int agentOSProbingInterval;
	private int agentResourceProbingInterval;
	private int agentConnectivityProbingInterval;
	private int agentStatusProbingInterval;
	
	private int connectionProbingInterval;
	private List<String> connectionURLs;
	
	// rest
	private boolean isAgentRESTEnabled;
	
	public AgentConfig(IAgentConfigListener listener) throws IOException {
		
		this.listener = listener;
		
		agentProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(AGENT_PROPERTIES_FILE));
		
		agentJMXReconnectInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "jmxReconnectInterval"));
		
		appMemoryProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "appMemoryProbingInterval"));
		appOSProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "appOSProbingInterval"));
		agentMemoryProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "agentMemoryProbingInterval"));
		agentOSProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "agentOSProbingInterval"));
		appResourceProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "appResourceProbingInterval"));
		appConnectivityProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "appConnectivityProbingInterval"));
		agentResourceProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "agentResourceProbingInterval"));
		agentConnectivityProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "agentConnectivityProbingInterval"));
		agentConnectivityProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "agentStatusProbingInterval"));
		agentJMXServiceURL = agentProperties.getProperty(AGENT_PREFIX + "jmxServiceURL");
		connectionProbingInterval = Integer.parseInt(agentProperties.getProperty(AGENT_PREFIX + "connectionProbingInterval"));
		connectionURLs = Arrays.asList(agentProperties.getProperty(AGENT_PREFIX + "connectionURLs").split(","));
		
		// TODO: REST
		logger.debug("constructor");
	}
	
	public void saveConfig(Properties newConfig) {
		
		logger.debug("saveConfig.newConfig");
		
		try {
			
			agentProperties.store(new FileOutputStream(AGENT_PROPERTIES_FILE),null);
		} catch (FileNotFoundException e) {
			
			logger.debug("saveConfig", e);;
		} catch (IOException e) {

			logger.debug("saveConfig", e);
		}
		
		listener.onAgentSaveConfig(newConfig);
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

	public String getAgentJMXServiceURL() {
		
		return agentJMXServiceURL;
	}

	public void setAgentJMXServiceURL(String agentJMXServiceURL) {
		
		this.agentJMXServiceURL = agentJMXServiceURL;
	}

	public AgentLibraryClient getAgentLibraryClient() {
		
		return agentLibraryClient;
	}

	public void setAgentLibraryClient(AgentLibraryClient agentLibraryClient) {
		
		this.agentLibraryClient = agentLibraryClient;
	}

	public int getAgentJMXReconnectInterval() {
		
		return agentJMXReconnectInterval;
	}

	public void setAgentJMXReconnectInterval(
			int agentJMXReconnectInterval) {
		
		this.agentJMXReconnectInterval = agentJMXReconnectInterval;
	}

	public int getAppMemoryProbingInterval() {
		
		return appMemoryProbingInterval;
	}

	public void setAppMemoryProbingInterval(int appMemoryProbingInterval) {
		
		this.appMemoryProbingInterval = appMemoryProbingInterval;
	}

	public int getAppOSProbingInterval() {
		return appOSProbingInterval;
	}

	public void setAppOSProbingInterval(int appOSProbingInterval) {
		this.appOSProbingInterval = appOSProbingInterval;
	}

	public void setAgentOSProbingInterval(int agentOSProbingInterval) {
		this.agentOSProbingInterval = agentOSProbingInterval;
	}

	public int getAgentOSProbingInterval() {
		return agentOSProbingInterval;
	}

	public void setAgentMemoryProbingInterval(int agentMemoryProbingInterval) {
		this.agentMemoryProbingInterval = agentMemoryProbingInterval;
	}

	public int getAgentMemoryProbingInterval() {
		return agentMemoryProbingInterval;
	}

	public int getConnectionProbingInterval() {
		
		return connectionProbingInterval;
	}

	public void setConnectionProbingInterval(int connectionProbingInterval) {
		this.connectionProbingInterval = connectionProbingInterval;
	}

	public List<String> getConnectionURLs() {
		
		return new ArrayList<String>(connectionURLs);
	}

	/*
	public void setConnectionURLs(List<String> connectionURLs) {
		this.connectionURLs = connectionURLs;
	}
	*/
}

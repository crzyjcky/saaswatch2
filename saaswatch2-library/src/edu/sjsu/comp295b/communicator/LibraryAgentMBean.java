package edu.sjsu.comp295b.communicator;

import java.util.Properties;

public interface LibraryAgentMBean {
	
	public void saveConfig(Properties newConfig);
	
	// allow direct method invocation for debugging purposes
	public void setDebugEnabled(boolean isDebugEnabled);
	public void setKeepAliveInterval(int keepAliveInterval);
}

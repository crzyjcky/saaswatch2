package edu.sjsu.comp295b.config;

import java.util.Properties;

public interface IAgentConfigListener {

	// application probing
	public void onAppResourceProbingEnabledChanged(Boolean isEnabled);
	public void onAppConnectivityProbingEnabledChanged(Boolean isEnabled);
	public void onAppResourceProbingIntervalChanged(int newInterval);
	public void onAppConnectivityProbingIntervalChanged(int newInterval);
	
	// agent probing
	public void onAgentResourceProbingEnabledChanged(Boolean isEnabled);
	public void onAgentConnectivityProbingEnabledChanged(Boolean isEnabled);
	public void onAgentResourceProbingIntervalChanged(int newInterval);
	public void onAgentConnectivityProbingIntervalChanged(int newInterval);
	public void onAgentStatusProbingIntervalChanged(int newInterval);
	public void onAgentSaveConfig(Properties newConfig);
	
	// rest
	public void onAgentRESTEnabledChanged(boolean isEnabled);
}

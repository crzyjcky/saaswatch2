package edu.sjsu.comp295b.communicator;

import java.util.Properties;

import javax.management.MBeanServerConnection;

public interface IAgentLibraryClientListener {

	public void onConnected(MBeanServerConnection mBeanServerConnection);
	public void onDisconnected();
}

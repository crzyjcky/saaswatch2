package edu.sjsu.comp295b.communicator;

import javax.management.MBeanServerConnection;

import edu.sjsu.comp295b.dto.DebugDTO;

public interface IAgentLibraryClientListener {

	public void onConnected(MBeanServerConnection mBeanServerConnection);
	public void onDisconnected();
	public void onDebug(DebugDTO debugDTO);
}

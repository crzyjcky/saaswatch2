package edu.sjsu.comp295b.communicator;

public interface IAgentLibraryCommunicatorListener {

	public void onLibraryDebug(String data);
	public void onLibraryKeepAlive();
}

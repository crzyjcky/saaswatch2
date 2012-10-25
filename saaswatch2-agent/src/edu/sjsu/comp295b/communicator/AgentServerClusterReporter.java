package edu.sjsu.comp295b.communicator;

import org.jgroups.JChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentServerClusterReporter {
	
	private static final Logger logger = LoggerFactory.getLogger(AgentServerClusterReporter.class);
	
	private JChannel channel;
	
	public void connect() throws Exception {
		
	    channel = new JChannel();
	    channel.connect("agentCluster");
	}
	
	public void close() {
		
		channel.close();
	}

	public static void main(String[] args) {
		
		AgentServerClusterReporter manager = new AgentServerClusterReporter();
		try {
			manager.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

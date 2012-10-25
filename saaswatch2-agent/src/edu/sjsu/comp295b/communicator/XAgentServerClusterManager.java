package edu.sjsu.comp295b.communicator;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.ChannelListener;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XAgentServerClusterManager implements ChannelListener, Receiver {
	
	private static final Logger logger = LoggerFactory.getLogger(XAgentServerClusterManager.class);
	
	private static int counter;
	
	private JChannel channel;
	
	public void connect() throws Exception {
		
	    channel = new JChannel();
	    channel.addChannelListener(this);
	    channel.setReceiver(this);
	    channel.connect("agentCluster");
	}
	
	public void close() {
		
		channel.close();
	}

	@Override
	public void channelClosed(Channel arg0) {
		// TODO Auto-generated method stub
		logger.info("channelClosed");
	}

	@Override
	public void channelConnected(Channel arg0) {
		// TODO Auto-generated method stub
		logger.info("channelConnected");
	}

	@Override
	public void channelDisconnected(Channel arg0) {
		// TODO Auto-generated method stub
		logger.info("channelDisconnected");
	}

	@Override
	public void getState(OutputStream arg0) throws Exception {
		// TODO Auto-generated method stub
		logger.info("getState");
	}

	@Override
	public void receive(Message arg0) {
		// TODO Auto-generated method stub
		logger.info("receive");
	}

	@Override
	public void setState(InputStream arg0) throws Exception {
		// TODO Auto-generated method stub
		logger.info("setState");
	}

	@Override
	public void block() {
		// TODO Auto-generated method stub
		logger.info("block");
	}

	@Override
	public void suspect(Address arg0) {
		// TODO Auto-generated method stub
		logger.info("suspect");
	}

	@Override
	public void unblock() {
		// TODO Auto-generated method stub
		logger.info("unblock");
	}

	@Override
	public void viewAccepted(View view) {
		// TODO Auto-generated method stub
		logger.info("viewAccepted: " + view.getViewId());
		
		List<Address> addresses = view.getMembers();
		int len = addresses.size();
		
		for (int i = 0; i < len; i++) {
			
			logger.info("address[" + i + "]:" + addresses.get(i).toString());
			
			Message message = new Message(addresses.get(i), i);
			try {
				channel.send(message);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
	}
	
	public static void main(String[] args) {
		
		XAgentServerClusterManager manager = new XAgentServerClusterManager();
		try {
			manager.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

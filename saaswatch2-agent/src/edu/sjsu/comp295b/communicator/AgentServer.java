package edu.sjsu.comp295b.communicator;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Agent;
import edu.sjsu.comp295b.IAgentListener;

public class AgentServer extends NotificationBroadcasterSupport implements AgentServerMBean, IAgentListener {

	private static final Logger logger = LoggerFactory.getLogger(AgentServer.class);
	
	private Agent agent;
	private long sequenceNumber;
	
	public AgentServer(Agent agent) {
		
		this.agent = agent;
		agent.setListener(this);
	}
	
	@Override
	public void saveConfig(Properties newConfig) {
		
		logger.debug("saveConfig: " + newConfig);
	}

	@Override
	public void setStatusReportInterval(int statusReportInterval) {
		
		logger.debug("setStatusReportInterval: " + statusReportInterval);
	}

	@Override
	public void setDataFlushInterval(int dataFlushInterval) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setRESTEnabled(boolean isRESTEnabled) {
		// TODO Auto-generated method stub
		
		logger.debug("setRESTEnabled: " + isRESTEnabled);
	}

	@Override
	public void setDebugEnabled(boolean isDebugEnabled) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setKeepAliveInterval(int keepAliveInterval) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDataUpload(List<String[]> dataList) {
		
		Notification notification = new Notification("onDataUpload", this,
				sequenceNumber++, System.currentTimeMillis(), null);
		notification.setUserData(dataList);

		sendNotification(notification);
	}

}

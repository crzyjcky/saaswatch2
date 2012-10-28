package edu.sjsu.comp295b.communicator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectionNotification;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.dto.DebugDTO;

public class AgentLibraryClient implements NotificationListener {

	private static final Logger logger = LoggerFactory
			.getLogger(AgentLibraryClient.class);

	private final String LIBRARY_AGENT_NAME_OBJECT_NAME = "saaswatch2:type=LibraryAgentCommunicator";
	private final String EVENT_DEBUG = "onDebug";

	private IAgentLibraryClientListener listener;

	private LibraryAgentMBean mBeanProxy;
	private MBeanServerConnection mBeanServerConnection;

	private JMXConnector jmxc;

	public AgentLibraryClient() {
	}

	public void connect(String jmxServiceURL) {

		JMXServiceURL url;
		try {
			url = new JMXServiceURL(jmxServiceURL);
			jmxc = JMXConnectorFactory.connect(url, null);
			jmxc.addConnectionNotificationListener(this, null, null);
			mBeanServerConnection = jmxc.getMBeanServerConnection();
			
			ObjectName mbeanName = new ObjectName(LIBRARY_AGENT_NAME_OBJECT_NAME);
			mBeanServerConnection.addNotificationListener(mbeanName, this, null, null);
			
			listener.onConnected(mBeanServerConnection);
		} catch (Exception e) {

			logger.debug("connect", e);
			listener.onDisconnected();
		}
	}

	public void setListener(IAgentLibraryClientListener listener) {

		this.listener = listener;
	}

	public void saveConfig(Properties newConfig) {

		mBeanProxy.saveConfig(newConfig);
	}

	@Override
	public void handleNotification(Notification notification, Object handback) {

		if (JMXConnectionNotification.CLOSED.equals(notification.getType())) {

			logger.debug("handleNotification.closed");
			listener.onDisconnected();
		} else if (JMXConnectionNotification.FAILED.equals(notification
				.getType())) {

			logger.debug("handleNotification.failed");
		} else if (JMXConnectionNotification.OPENED.equals(notification
				.getType())) {

			logger.debug("handleNotification.opened");
			listener.onConnected(mBeanServerConnection);
			
		} else if (EVENT_DEBUG.equals(notification.getType())) {

			ObjectMapper mapper = new ObjectMapper();
			DebugDTO debugDTO = null;
			
			try {
				debugDTO = mapper.readValue(notification.getMessage(), DebugDTO.class);
			} catch (JsonParseException e) {
				
				logger.debug("handleNotification", e);
			} catch (JsonMappingException e) {

				logger.debug("handleNotification", e);
			} catch (IOException e) {

				logger.debug("handleNotification", e);
			}
			
		
			listener.onDebug(debugDTO);
		} else {

			logger.debug("handleNotification.else");
		}
	}
}

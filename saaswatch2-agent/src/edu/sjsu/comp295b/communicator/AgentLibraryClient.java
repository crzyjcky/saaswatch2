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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AgentLibraryClient implements NotificationListener {

	private static final Logger logger = LoggerFactory
			.getLogger(AgentLibraryClient.class);

	private final String EVENT_CONNECTED = "onConnected";
	private final String EVENT_DISCONNECTED = "disconnected";
	
	private List<IAgentLibraryClientListener> listeners = new ArrayList<IAgentLibraryClientListener>();
	
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
			
			broadcastEvent(EVENT_CONNECTED);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.debug("connect", e);
			broadcastEvent(EVENT_DISCONNECTED);
		} 
		

		
		/*
		mBeanServerConnection = jmxc.getMBeanServerConnection();

		ObjectName mbeanName = new ObjectName(
				"saaswatch:type=LibraryAgentCommunicator");

		mBeanProxy = JMX.newMBeanProxy(mBeanServerConnection, mbeanName,
				LibraryAgentMBean.class, true);

		mBeanServerConnection.addNotificationListener(mbeanName, this, null,
				null);

		mBeanProxy.setDebugEnabled(true);
		*/

	}

	public void addListener(IAgentLibraryClientListener listener) {
		
		listeners.add(listener);
	}
	
	public void removeListener(IAgentLibraryClientListener listener) {
		
		listeners.remove(listener);
	}

	public void saveConfig(Properties newConfig) {

		mBeanProxy.saveConfig(newConfig);
	}

	@Override
	public void handleNotification(Notification notification, Object handback) {

		logger.debug("Notification received! " + notification.getMessage()
				+ ": " + notification.getType());
		
		if (JMXConnectionNotification.CLOSED.equals(notification.getType())) {
			
			logger.debug("handleNotification.closed");	
			broadcastEvent(EVENT_DISCONNECTED);
		} else if (JMXConnectionNotification.FAILED.equals(notification.getType())) {
			
			logger.debug("handleNotification.failed");	
			//broadcastEvent(EVENT_DISCONNECTED);
		} else if (JMXConnectionNotification.OPENED.equals(notification.getType())) {
			
			logger.debug("handleNotification.opened");	
			broadcastEvent(EVENT_CONNECTED);
		} else {
			
			logger.debug("handleNotification.else");	
		}
		
		
	}
	
	public void broadcastEvent(String eventName) {
		
		if (EVENT_CONNECTED.equals(eventName)) {
			
			for (IAgentLibraryClientListener listener : listeners) {
				
				listener.onConnected(mBeanServerConnection);
			}
		} else if (EVENT_DISCONNECTED.equals(eventName)) {
			
			for (IAgentLibraryClientListener listener : listeners) {
				
				listener.onDisconnected();
			}			
		}
	}
}

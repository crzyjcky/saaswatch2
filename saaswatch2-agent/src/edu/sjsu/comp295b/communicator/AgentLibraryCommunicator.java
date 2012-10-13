package edu.sjsu.comp295b.communicator;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Agent;

public class AgentLibraryCommunicator implements NotificationListener{

	private static final Logger logger = LoggerFactory.getLogger(AgentLibraryCommunicator.class);
	
	private IAgentLibraryCommunicatorListener listener;
	private LibraryAgentMBean mBeanProxy;
	private MBeanServerConnection mBeanServerConnection;
	
	public AgentLibraryCommunicator() {
		
        JMXServiceURL url;

		try {
			
			url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9010/jmxrmi");
			JMXConnector jmxc = JMXConnectorFactory.connect(url, null);
			mBeanServerConnection = jmxc.getMBeanServerConnection();
			
			ObjectName mbeanName = new ObjectName("saaswatch:type=LibraryAgentCommunicator");
			
	        mBeanProxy =
	            JMX.newMBeanProxy(mBeanServerConnection, mbeanName, LibraryAgentMBean.class, true);
	        logger.debug("done");
	        mBeanServerConnection.addNotificationListener(mbeanName, this, null, null);
	        
	        mBeanProxy.setDebugEnabled(true);

		} catch (MalformedURLException e) {
			
			logger.debug("constructor", e);
		} catch (IOException e) {

			logger.debug("constructor", e);
		} catch (MalformedObjectNameException e) {

			logger.debug("constructor", e);
		} catch (NullPointerException e) {

			logger.debug("constructor", e);
		} catch (InstanceNotFoundException e) {

			logger.debug("constructor", e);
		}	
	}
	
	public void setListener(IAgentLibraryCommunicatorListener listener) {
		
		this.listener = listener;
	}
	
	public void saveConfig(Properties newConfig) {
		
		mBeanProxy.saveConfig(newConfig);
	}
	
	@Override
	public void handleNotification(Notification notification, Object handback) {
		
		logger.debug("Notification received!");
	}
	
	public MBeanServerConnection getMBeanServerConnection() {
		
		return mBeanServerConnection;
	}
}

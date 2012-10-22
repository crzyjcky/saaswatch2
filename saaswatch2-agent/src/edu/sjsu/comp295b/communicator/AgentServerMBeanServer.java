package edu.sjsu.comp295b.communicator;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Agent;

public class AgentServerMBeanServer {

	private static final Logger logger = LoggerFactory.getLogger(AgentServerMBeanServer.class);
	
	public AgentServerMBeanServer(Agent agent) {
		
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name;
		
		try {
			
			name = new ObjectName("saaswatch:type=AgentServerCommunicator");
			AgentServer mbean = new AgentServer(agent);
			mbs.registerMBean(mbean, name);
		} catch (MalformedObjectNameException e) {

			logger.debug("constructor", e);
		} catch (NullPointerException e) {

			logger.debug("constructor", e);
		} catch (InstanceAlreadyExistsException e) {

			logger.debug("constructor", e);
		} catch (MBeanRegistrationException e) {

			logger.debug("constructor", e);
		} catch (NotCompliantMBeanException e) {

			logger.debug("constructor", e);
		}

	}
}

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

import edu.sjsu.comp295b.Library;

public class LibraryAgentMBeanServer {

	private static final Logger logger = LoggerFactory.getLogger(LibraryAgentMBeanServer.class);
	
	public LibraryAgentMBeanServer(Library library) throws InstanceAlreadyExistsException, MBeanRegistrationException, NotCompliantMBeanException, MalformedObjectNameException, NullPointerException {
		
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		ObjectName name = new ObjectName("saaswatch2:type=LibraryAgentCommunicator");
		LibraryAgent mbean = new LibraryAgent(library);
		mbs.registerMBean(mbean, name);
	}
}

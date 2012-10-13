package edu.sjsu.comp295b.probe;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.dto.MemoryDTO;

public class AgentMemoryProbe {

	private static final Logger logger = LoggerFactory
			.getLogger(AgentMemoryProbe.class);
	
	MemoryMXBean mxBean = ManagementFactory.getMemoryMXBean();
	
	public AgentMemoryProbe() {

	}

	public MemoryDTO probe() {

		MemoryUsage heapMemoryUsage = mxBean.getHeapMemoryUsage();
		MemoryUsage nonHeapMemoryUsage = mxBean.getNonHeapMemoryUsage();
		
		MemoryDTO memoryDTO = new MemoryDTO();
		
		memoryDTO.heapCommitted = heapMemoryUsage.getCommitted();
		memoryDTO.heapInit = heapMemoryUsage.getInit();
		memoryDTO.heapMax = heapMemoryUsage.getMax();
		memoryDTO.heapUsed = heapMemoryUsage.getUsed();
		memoryDTO.nonHeapCommitted = nonHeapMemoryUsage.getCommitted();
		memoryDTO.nonHeapInit = nonHeapMemoryUsage.getInit();
		memoryDTO.nonHeapMax = nonHeapMemoryUsage.getMax();
		memoryDTO.nonHeapUsed = nonHeapMemoryUsage.getUsed();
		
		return memoryDTO;
	}
}

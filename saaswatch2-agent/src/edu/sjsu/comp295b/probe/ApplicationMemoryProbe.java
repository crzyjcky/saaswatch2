package edu.sjsu.comp295b.probe;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import javax.management.MBeanServerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.dto.MemoryDTO;

public class ApplicationMemoryProbe {

	private static final Logger logger = LoggerFactory
			.getLogger(ApplicationMemoryProbe.class);

	private MemoryMXBean mxBean;
	
	public void connect(MBeanServerConnection mBeanServerConnection) {
	
		try {

			mxBean = ManagementFactory.newPlatformMXBeanProxy(
					mBeanServerConnection,
					ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
		} catch (IOException e) {

			logger.debug("connect", e);
		}
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
		
		memoryDTO.timestamp = System.currentTimeMillis();
		
		return memoryDTO;
	}
	
}

package edu.sjsu.comp295b.probe;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.dto.OSDTO;

public class AgentOperatingSystemProbe {

	private static final Logger logger = LoggerFactory
			.getLogger(AgentOperatingSystemProbe.class);
	
	OperatingSystemMXBean mxBean = ManagementFactory.getOperatingSystemMXBean();
	
	public OSDTO probe() {

		OSDTO osDTO = new OSDTO();

		osDTO.arch = mxBean.getArch();
		osDTO.availableProcessors = mxBean.getAvailableProcessors();
		osDTO.name = mxBean.getName();
		osDTO.systemLoadAverage = mxBean.getSystemLoadAverage();
		osDTO.version = mxBean.getSystemLoadAverage();
		
		osDTO.timestamp = System.currentTimeMillis();
		
		return osDTO;
	}
}

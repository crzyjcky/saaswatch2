package edu.sjsu.comp295b.probe;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import javax.management.MBeanServerConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.dto.OSDTO;

public class ApplicationOperatingSystemProbe {

	private static final Logger logger = LoggerFactory
			.getLogger(ApplicationOperatingSystemProbe.class);

	private OperatingSystemMXBean mxBean;

	public void connect(MBeanServerConnection mBeanServerConnection) {

		try {

			mxBean = ManagementFactory.newPlatformMXBeanProxy(
					mBeanServerConnection,
					ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
		} catch (IOException e) {

			logger.debug("connect", e);
		}
	}

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

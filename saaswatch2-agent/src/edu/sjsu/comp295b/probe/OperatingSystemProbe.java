package edu.sjsu.comp295b.probe;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.net.MalformedURLException;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.dto.MemoryDTO;
import edu.sjsu.comp295b.dto.OSDTO;

public class OperatingSystemProbe {

	private static final Logger logger = LoggerFactory
			.getLogger(OperatingSystemProbe.class);

	private MBeanServerConnection mBeanServerConnection;
	private OperatingSystemMXBean mxBean;

	public OperatingSystemProbe(MBeanServerConnection mBeanServerConnection) {

		this.mBeanServerConnection = mBeanServerConnection;

		try {

			mxBean = ManagementFactory.newPlatformMXBeanProxy(
					mBeanServerConnection,
					ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
		} catch (IOException e) {

			logger.debug("constructor", e);
		}
	}

	public OSDTO probe() {

		OSDTO osDTO = new OSDTO();

		osDTO.arch = mxBean.getArch();
		osDTO.availableProcessors = mxBean.getAvailableProcessors();
		osDTO.name = mxBean.getName();
		osDTO.systemLoadAverage = mxBean.getSystemLoadAverage();
		osDTO.version = mxBean.getSystemLoadAverage();
		
		return osDTO;
	}
}

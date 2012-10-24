package edu.sjsu.comp295b;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.communicator.AgentLibraryClient;
import edu.sjsu.comp295b.communicator.AgentServerMBeanServer;
import edu.sjsu.comp295b.communicator.IAgentLibraryClientListener;
import edu.sjsu.comp295b.config.AgentConfig;
import edu.sjsu.comp295b.config.IAgentConfigListener;
import edu.sjsu.comp295b.dto.ConnectionDTO;
import edu.sjsu.comp295b.dto.MemoryDTO;
import edu.sjsu.comp295b.dto.OSDTO;
import edu.sjsu.comp295b.probe.AgentMemoryProbe;
import edu.sjsu.comp295b.probe.AgentOperatingSystemProbe;
import edu.sjsu.comp295b.probe.ApplicationMemoryProbe;
import edu.sjsu.comp295b.probe.ApplicationOperatingSystemProbe;
import edu.sjsu.comp295b.probe.ConnectionProbe;
import edu.sjsu.comp295b.util.Buffer;
import edu.sjsu.comp295b.util.ClassInspector;

public class Agent implements IAgentLibraryClientListener, IAgentConfigListener {

	private static final Logger logger = LoggerFactory.getLogger(Agent.class);
	private final boolean DAEMON_FLAG = false;

	private AgentLibraryClient agentLibraryClient;
	private AgentConfig agentConfig;
	private ApplicationMemoryProbe applicationMemoryProbe = new ApplicationMemoryProbe();
	private AgentMemoryProbe agentMemoryProbe = new AgentMemoryProbe();;
	private ApplicationOperatingSystemProbe applicationOperatingSystemProbe = new ApplicationOperatingSystemProbe();
	private AgentOperatingSystemProbe agentOperatingSystemProbe = new AgentOperatingSystemProbe();
	private ConnectionProbe connectionProbe = new ConnectionProbe();

	private Timer jmxReconnectTimer = new Timer(DAEMON_FLAG);
	private JMXReconnectTimerTask jmxReconnectTimerTask = new JMXReconnectTimerTask();
	private Timer appMemoryProbingTimer = new Timer(DAEMON_FLAG);
	private AppMemoryProbingTimerTask appMemoryProbingTimerTask = new AppMemoryProbingTimerTask();
	private Timer agentMemoryProbingTimer = new Timer(DAEMON_FLAG);
	private AgentMemoryProbingTimerTask agentMemoryProbingTimerTask = new AgentMemoryProbingTimerTask();
	private Timer appOSProbingTimer = new Timer(DAEMON_FLAG);
	private AppOSProbingTimerTask appOSProbingTimerTask = new AppOSProbingTimerTask();
	private Timer agentOSProbingTimer = new Timer(DAEMON_FLAG);
	private AgentOSProbingTimerTask agentOSProbingTimerTask = new AgentOSProbingTimerTask();
	private Timer connectionProbingTimer = new Timer(DAEMON_FLAG);
	private ConnectionProbingTimerTask connectionProbingTimerTask = new ConnectionProbingTimerTask();
	
	private Timer agentServerDataUploadTimer = new Timer(DAEMON_FLAG);
	private AgentServerDataUploadTimerTask agentServerDataUploadTimerTask = new AgentServerDataUploadTimerTask();
	
	private IAgentListener listener;
	private Buffer buffer = new Buffer();

	public Agent() throws IOException {

		logger.debug("constructor.start");

		agentConfig = new AgentConfig(this);

		agentLibraryClient = new AgentLibraryClient();
		agentLibraryClient.addListener(this);
		
		AgentServerMBeanServer agentServerMBeanServer = new AgentServerMBeanServer(this);
		logger.debug("constructor.end");
	}

	public void start() {
		
		try {

			agentLibraryClient.connect(agentConfig.getAgentJMXServiceURL());
		} catch (Exception e) {

			logger.debug("constructor", e);
			onDisconnected();
		}
		
		startAgentMemoryProbingJobScheduling();
		startAgentOperatingSystemProbingJobScheduling();
		startConnectionProbingJobScheduling();
		
		startAgentServerDataUploadJobScheduling();
	}
	
	public void saveConfig(Properties newConfig) {

		logger.debug("saveConfig.newConfig");

		agentConfig.saveConfig(newConfig);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws SchedulerException
	 */
	public static void main(String[] args) throws IOException,
			SchedulerException {

		logger.debug("main.start");
		Agent agent = new Agent();
		logger.debug("main.agent start");
		agent.start();

		// agent.scheduleApplicationMemoryProbingJob();
		//agent.scheduleAgentMemoryProbingJob();
		//agent.scheduleOperatingSystemProbingJob();

		Properties newConfig = new Properties();
		newConfig.setProperty("library.debugEnabled", "true");
		// agent.saveConfig(newConfig);

		logger.debug("main.newConfig start");

		logger.debug("main.end");

	}

	private class AppMemoryProbingTimerTask extends TimerTask {

		@Override
		public void run() {

			MemoryDTO memoryDTO = applicationMemoryProbe.probe();
			logger.debug("AppMemoryProbingTimerTask");
			ClassInspector.inspect(memoryDTO);
			
			buffer.addData("appMemory", memoryDTO);
		}
	}

	private class AgentMemoryProbingTimerTask extends TimerTask {

		@Override
		public void run() {

			MemoryDTO memoryDTO = agentMemoryProbe.probe();
			logger.debug("AgentMemoryProbingTimerTask");
			ClassInspector.inspect(memoryDTO);
			
			buffer.addData("agentMemory", memoryDTO);
		}
	}

	private class JMXReconnectTimerTask extends TimerTask {

		@Override
		public void run() {
			
				logger.debug("JMXReconnectTimerTask");
				agentLibraryClient.connect(agentConfig.getAgentJMXServiceURL());
		
		}

	}

	private class AppOSProbingTimerTask extends TimerTask {

		@Override
		public void run() {

			OSDTO osDTO = applicationOperatingSystemProbe.probe();
			logger.debug("AppOSProbingTimerTask");
			ClassInspector.inspect(osDTO);
			
			buffer.addData("appOS", osDTO);
		}
	}
	
	private class AgentOSProbingTimerTask extends TimerTask {

		@Override
		public void run() {

			OSDTO osDTO = agentOperatingSystemProbe.probe();
			logger.debug("AgentOSProbingTimerTask");
			ClassInspector.inspect(osDTO);
			
			buffer.addData("agentOS", osDTO);
		}
	}
	
	private class ConnectionProbingTimerTask extends TimerTask {

		@Override
		public void run() {
			
			List<ConnectionDTO> connectionDTOs = connectionProbe.probe(agentConfig.getConnectionURLs());
			
			for (ConnectionDTO connectionDTO : connectionDTOs) {
				
				logger.debug("ConnectionProbingTimerTask");
				ClassInspector.inspect(connectionDTO);
				
				buffer.addData("connection", connectionDTO);
			}
			
		}
	}
	
	private class AgentServerDataUploadTimerTask extends TimerTask {

		@Override
		public void run() {
			logger.debug("AgentServerDataUploadTimerTask");
			
			List<String[]> dataList = buffer.retrieveAndClear();
			
			listener.onDataUpload(dataList);
			
			
		}
		
	}

	public void setAppMemoryProbingInterval(int interval) {

		// appResourceProbingInterval
	}

	@Override
	public void onAppResourceProbingEnabledChanged(Boolean isEnabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAppConnectivityProbingEnabledChanged(Boolean isEnabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAppResourceProbingIntervalChanged(int newInterval) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAppConnectivityProbingIntervalChanged(int newInterval) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAgentResourceProbingEnabledChanged(Boolean isEnabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAgentConnectivityProbingEnabledChanged(Boolean isEnabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAgentResourceProbingIntervalChanged(int newInterval) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAgentConnectivityProbingIntervalChanged(int newInterval) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAgentStatusProbingIntervalChanged(int newInterval) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAgentRESTEnabledChanged(boolean isEnabled) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAgentSaveConfig(Properties newConfig) {

		logger.debug("saveConfig.libraryConfig");

		Properties libraryConfig = new Properties();
		for (Object key : newConfig.keySet()) {

			if (((String) key).startsWith(AgentConfig.LIBRARY_PREFIX)) {

				libraryConfig.setProperty((String) key,
						newConfig.getProperty((String) key));
			}
		}

		if (!libraryConfig.isEmpty()) {

			logger.debug("saveConfig.libraryConfig2");
			agentLibraryClient.saveConfig(libraryConfig);
		}
	}

	@Override
	public void onConnected(MBeanServerConnection mBeanServerConnection) {
		
		logger.debug("onConnected");
		stopJMXReconnectJobScheduling();
		
		startApplicationMemoryProbingJobScheduling(mBeanServerConnection);
	}

	@Override
	public void onDisconnected() {
		
		logger.debug("onDisconnected");
		startJMXReconnectJobScheduling();
		
		stopApplicationMemoryProbingJobScheduling();
	}

	private void startJMXReconnectJobScheduling() {
		
		jmxReconnectTimer.cancel();
		jmxReconnectTimer = new Timer(DAEMON_FLAG);
		jmxReconnectTimerTask = new JMXReconnectTimerTask();
		jmxReconnectTimer.scheduleAtFixedRate(jmxReconnectTimerTask, agentConfig.getAgentJMXReconnectInterval(), agentConfig.getAgentJMXReconnectInterval());
	}
	
	private void stopJMXReconnectJobScheduling() {
		
		jmxReconnectTimer.cancel();
	}
	
	private void startApplicationMemoryProbingJobScheduling(MBeanServerConnection mBeanServerConnection) {
		
		appMemoryProbingTimer.cancel();
		appMemoryProbingTimer = new Timer(DAEMON_FLAG);
		appMemoryProbingTimerTask = new AppMemoryProbingTimerTask();
		
		applicationMemoryProbe.connect(mBeanServerConnection);
		
		appMemoryProbingTimer.scheduleAtFixedRate(appMemoryProbingTimerTask, 0, agentConfig.getAppMemoryProbingInterval());
	}
	
	private void stopApplicationMemoryProbingJobScheduling() {
		
		appMemoryProbingTimer.cancel();
	}
	
	private void startAgentMemoryProbingJobScheduling() {
		
		agentMemoryProbingTimer.cancel();
		agentMemoryProbingTimer = new Timer(DAEMON_FLAG);
		agentMemoryProbingTimerTask = new AgentMemoryProbingTimerTask();
		
		agentMemoryProbingTimer.scheduleAtFixedRate(agentMemoryProbingTimerTask, 0, agentConfig.getAgentMemoryProbingInterval());
	}
	
	private void startAgentOperatingSystemProbingJobScheduling() {
		
		agentOSProbingTimer.cancel();
		agentOSProbingTimer = new Timer(DAEMON_FLAG);
		agentOSProbingTimerTask = new AgentOSProbingTimerTask();
		
		agentOSProbingTimer.scheduleAtFixedRate(agentOSProbingTimerTask, 0, agentConfig.getAgentOSProbingInterval());
	}	
	
	private void startConnectionProbingJobScheduling() {
		
		connectionProbingTimer.cancel();
		connectionProbingTimer = new Timer(DAEMON_FLAG);
		connectionProbingTimerTask = new ConnectionProbingTimerTask();
		
		connectionProbingTimer.scheduleAtFixedRate(connectionProbingTimerTask, 0, agentConfig.getConnectionProbingInterval());
	}
	
	private void startAgentServerDataUploadJobScheduling() {
		
		agentServerDataUploadTimer.cancel();
		agentServerDataUploadTimer = new Timer(DAEMON_FLAG);
		agentServerDataUploadTimerTask = new AgentServerDataUploadTimerTask();
		
		agentServerDataUploadTimer.scheduleAtFixedRate(agentServerDataUploadTimerTask, 0, agentConfig.getAgentServerDataUploadInterval());
	}

	public IAgentListener getListener() {
		return listener;
	}

	public void setListener(IAgentListener listener) {
		this.listener = listener;
	}
	
}
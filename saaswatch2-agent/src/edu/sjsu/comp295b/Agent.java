package edu.sjsu.comp295b;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.MBeanServerConnection;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.communicator.AgentLibraryCommunicator;
import edu.sjsu.comp295b.communicator.IAgentLibraryCommunicatorListener;
import edu.sjsu.comp295b.config.AgentConfig;
import edu.sjsu.comp295b.config.IAgentConfigListener;
import edu.sjsu.comp295b.dto.MemoryDTO;
import edu.sjsu.comp295b.dto.OSDTO;
import edu.sjsu.comp295b.helper.ClassInspector;
import edu.sjsu.comp295b.probe.AgentMemoryProbe;
import edu.sjsu.comp295b.probe.ApplicationMemoryProbe;
import edu.sjsu.comp295b.probe.OperatingSystemProbe;

public class Agent implements IAgentLibraryCommunicatorListener, IAgentConfigListener {

	private static final Logger logger = LoggerFactory.getLogger(Agent.class);
	
	private AgentLibraryCommunicator agentLibraryCommunicator;
	private MBeanServerConnection mBeanServerConnection;
	private AgentConfig agentConfig;
	private ApplicationMemoryProbe applicationMemoryProbe;
	private AgentMemoryProbe agentMemoryProbe;
	private OperatingSystemProbe operatingSystemProbe;
	
	private Timer appMemoryProbingTimer = new Timer();
	private AppMemoryProbingTimerTask appMemoryProbingTimerTask = new AppMemoryProbingTimerTask();
	private Timer agentMemoryProbingTimer = new Timer();
	private AgentMemoryProbingTimerTask agentMemoryProbingTimerTask = new AgentMemoryProbingTimerTask();
	private Timer osProbingTimer = new Timer();
	private OSProbingTimerTask osProbingTimerTask = new OSProbingTimerTask();
	
	public Agent() throws IOException {
		
		logger.debug("constructor.start");
		agentLibraryCommunicator = new AgentLibraryCommunicator();
		mBeanServerConnection = agentLibraryCommunicator.getMBeanServerConnection();
		
		logger.debug("constructor.start2");
		agentLibraryCommunicator.setListener(this);
		
		agentConfig = new AgentConfig(this, agentLibraryCommunicator);
		//agentConfig.setListener(this);
		
		applicationMemoryProbe = new ApplicationMemoryProbe(mBeanServerConnection);
		agentMemoryProbe = new AgentMemoryProbe();
		operatingSystemProbe = new OperatingSystemProbe(mBeanServerConnection);
		
		logger.debug("constructor.end");
	}
	
	public void saveConfig(Properties newConfig) {
		
		logger.debug("saveConfig.newConfig");

		agentConfig.saveConfig(newConfig);
	}
	
	@Override
	public void onLibraryDebug(String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLibraryKeepAlive() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param args
	 * @throws IOException 
	 * @throws SchedulerException 
	 */
	public static void main(String[] args) throws IOException, SchedulerException {
		
		logger.debug("main.start");
		Agent agent = new Agent();
		logger.debug("main.agent start");
		
		agent.scheduleApplicationMemoryProbingJob();
		agent.scheduleAgentMemoryProbingJob();
		agent.scheduleOperatingSystemProbingJob();
		
		Properties newConfig = new Properties();
		newConfig.setProperty("library.debugEnabled", "true");
		agent.saveConfig(newConfig);
		
		logger.debug("main.newConfig start");
		
		
		logger.debug("main.end");

	}

	void scheduleApplicationMemoryProbingJob() {
		
		appMemoryProbingTimer.scheduleAtFixedRate(appMemoryProbingTimerTask, 0, 10000);
	}
	
	void scheduleAgentMemoryProbingJob() {
		
		agentMemoryProbingTimer.scheduleAtFixedRate(agentMemoryProbingTimerTask, 0, 10000);
	}
	
	void scheduleOperatingSystemProbingJob() {
		
		osProbingTimer.scheduleAtFixedRate(osProbingTimerTask, 0, 10000);
	}
	
	private class AppMemoryProbingTimerTask extends TimerTask {

		@Override
		public void run() {
			
			MemoryDTO memoryDTO = applicationMemoryProbe.probe();
			logger.debug("AppMemoryProbingTimerTask");
			ClassInspector.inspect(memoryDTO);
		}
	}

	private class AgentMemoryProbingTimerTask extends TimerTask {

		@Override
		public void run() {
			
			MemoryDTO memoryDTO = agentMemoryProbe.probe();
			logger.debug("AgentMemoryProbingTimerTask");
			ClassInspector.inspect(memoryDTO);
		}
	}
	
	private class OSProbingTimerTask extends TimerTask {

		@Override
		public void run() {
			
			OSDTO osDTO = operatingSystemProbe.probe();
			logger.debug("OSProbingTimerTask");
			ClassInspector.inspect(osDTO);
		}
	}
	
	public void setAppMemoryProbingInterval(int interval) {
		
		//appResourceProbingInterval
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
	
}	
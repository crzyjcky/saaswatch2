package edu.sjsu.comp295b;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.communicator.LibraryAgentMBeanServer;
import edu.sjsu.comp295b.config.LibraryConfig;
import edu.sjsu.comp295b.config.ILibraryConfigListener;
import edu.sjsu.comp295b.dto.DebugDTO;
import edu.sjsu.comp295b.job.KeepAliveJob;
import edu.sjsu.comp295b.logger.DebugLogger;

public class Library implements ILibraryConfigListener {

	private static final Logger logger = LoggerFactory.getLogger(Library.class);

	private final String KEEP_ALIVE_JOB = "keepAliveJob";
	private final String KEEP_ALIVE_TRIGGER = "keepAliveTrigger";

	private static Library instance;
	private LibraryConfig libraryConfig;
	private List<ILibraryListener> listeners = new ArrayList<ILibraryListener>();
	private Scheduler scheduler;

	private Library() {

		try {
			
			libraryConfig = new LibraryConfig();
		} catch (FileNotFoundException e) {

			logger.debug("constructor", e);
		} catch (IOException e) {

			logger.debug("constructor", e);
		}
		libraryConfig.setListener(this);

		try {
			
			LibraryAgentMBeanServer mbs = new LibraryAgentMBeanServer(this);
		} catch (InstanceAlreadyExistsException e) {

			logger.debug("constructor", e);
		} catch (MBeanRegistrationException e) {

			logger.debug("constructor", e);
		} catch (NotCompliantMBeanException e) {

			logger.debug("constructor", e);
		} catch (MalformedObjectNameException e) {

			logger.debug("constructor", e);
		} catch (NullPointerException e) {

			logger.debug("constructor", e);
		}
	}

	 synchronized public static Library getInstance() {

		if (instance == null) {

			instance = new Library();
			/*
			try {
				instance.scheduleKeepAliveJob();
			} catch (SchedulerException e) {

				e.printStackTrace();
			}*/
		}

		return instance;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {

		logger.debug("isEnabled:" + isDebugEnabled);

		libraryConfig.setDebugEnabled(isDebugEnabled);
	}

	public void setKeepAliveInterval(int keepAliveInterval) {

		libraryConfig.setKeepAliveInterval(keepAliveInterval);
	}

	public void debug(DebugDTO debugDTO) {

		if (libraryConfig.isDebugEnabled()) {
			
			broadcast("onDebug", debugDTO);
		}
	}

	public void scheduleKeepAliveJob() throws SchedulerException {

		scheduler = StdSchedulerFactory.getDefaultScheduler();
		scheduler.start();

		JobDetail job = JobBuilder.newJob(KeepAliveJob.class)
				.withIdentity(KEEP_ALIVE_JOB).build();

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity(KEEP_ALIVE_TRIGGER)
				.startNow()
				.withSchedule(
						SimpleScheduleBuilder
								.simpleSchedule()
								.withIntervalInSeconds(
										libraryConfig.getKeepAliveInterval())
								.repeatForever()).build();

		scheduler.scheduleJob(job, trigger);
	}

	public void addListener(ILibraryListener listener) {

		listeners.add(listener);
	}

	public void removeListener(ILibraryListener listener) {

		listeners.remove(listener);
	}

	public void broadcast(String eventname, Object data) {

		if ("onDebug".equals(eventname)) {

			for (ILibraryListener listener : listeners) {

				listener.onDebug(data);
			}

		} else if ("onKeepAlive".equals(eventname)) {

			for (ILibraryListener listener : listeners) {

				listener.onKeepAlive();
			}
		}
	}

	// we can have multiple debuglogger instances, which share one library
	// instance
	public DebugLogger getDebugLogger(String classname) {

		return new DebugLogger(this, classname);
	}

	public DebugLogger getDebugLogger(Class clazz) {

		return new DebugLogger(this, clazz.toString());
	}

	@Override
	public void onKeepAliveIntervalChange(Object data) {

		logger.debug("keepAliveIntervalChange:" + data);
		try {
			
			// retrieve the trigger
			Trigger oldTrigger = scheduler.getTrigger(TriggerKey.triggerKey(KEEP_ALIVE_TRIGGER));
			
			// obtain a builder that would produce the trigger
			TriggerBuilder oldTriggerBuilder = oldTrigger.getTriggerBuilder();
			
			Trigger newTrigger = oldTriggerBuilder
				.startNow()
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
				    .withIntervalInSeconds(libraryConfig.getKeepAliveInterval())
				    .repeatForever())
				    .build();

		    scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void saveConfig(Properties newConfig) {
		
		libraryConfig.saveConfig(newConfig);
	}
	
	/**
	 * @param args
	 * @throws NullPointerException
	 * @throws MalformedObjectNameException
	 * @throws NotCompliantMBeanException
	 * @throws MBeanRegistrationException
	 * @throws InstanceAlreadyExistsException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws SchedulerException
	 */
	public static void main(String[] args)
			throws InstanceAlreadyExistsException, MBeanRegistrationException,
			NotCompliantMBeanException, MalformedObjectNameException,
			NullPointerException, FileNotFoundException, IOException,
			SchedulerException {

		Library library = Library.getInstance();
	
		// remove onKeepAlive notification
		// library.scheduleKeepAliveJob();

		/*
		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/

		DebugLogger debugLogger = library.getDebugLogger(Library.class);
		debugLogger.debug("hello world");
		
		int len = 100;
		for (int i = 0; i < len; i++) {
			
			try {
				Thread.sleep(2000);
				
				debugLogger.debug("hello world: " + i);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("Waiting forever...");
		
	}

}

package edu.sjsu.comp295b;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
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
import edu.sjsu.comp295b.helper.Config;
import edu.sjsu.comp295b.helper.IConfigListener;
import edu.sjsu.comp295b.job.KeepAliveJob;
import edu.sjsu.comp295b.logger.DebugLogger;

public class Library implements IConfigListener {

	private static final Logger logger = LoggerFactory.getLogger(Library.class);

	private final String KEEP_ALIVE_JOB = "keepAliveJob";
	private final String KEEP_ALIVE_TRIGGER = "keepAliveTrigger";

	private static Library instance;
	private Config config;
	private List<ILibraryListener> listeners = new ArrayList<ILibraryListener>();
	private Scheduler scheduler;

	private Library() {

		try {
			config = new Config();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		config.addListener(this);

		try {
			LibraryAgentMBeanServer mbs = new LibraryAgentMBeanServer(this);
		} catch (InstanceAlreadyExistsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Library getInstance() {

		if (instance == null) {

			instance = new Library();
			
			try {
				instance.scheduleKeepAliveJob();
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return instance;
	}

	public void setDebugEnabled(boolean isDebugEnabled) {

		logger.debug("isEnabled:" + isDebugEnabled);

		config.setDebugEnabled(isDebugEnabled);
	}

	public void setKeepAliveInterval(int keepAliveInterval) {

		config.setKeepAliveInterval(keepAliveInterval);
	}

	public void debug(String debugString) {

		if (config.isDebugEnabled()) {

			broadcast("onDebug", debugString);
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
										config.getKeepAliveInterval())
								.repeatForever()).build();

		scheduler.scheduleJob(job, trigger);
	}

	public void addListener(ILibraryListener listener) {

		listeners.add(listener);
	}

	public void removeListener(ILibraryListener listener) {

		listeners.remove(listener);
	}

	public void broadcast(String eventname, String data) {

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
				    .withIntervalInSeconds(config.getKeepAliveInterval())
				    .repeatForever())
				    .build();

		    scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);

		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		library.scheduleKeepAliveJob();

		library.setKeepAliveInterval(2);
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		library.setKeepAliveInterval(10);

		DebugLogger debugLogger = library.getDebugLogger(Library.class);
		debugLogger.debug("hello world");

		System.out.println("Waiting forever...");
		
	}

}

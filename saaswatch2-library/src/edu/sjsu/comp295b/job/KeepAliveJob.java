package edu.sjsu.comp295b.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Library;

public class KeepAliveJob implements Job{

	private static final Logger logger = LoggerFactory.getLogger(KeepAliveJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		
		Library.getInstance().broadcast("onKeepAlive", null);
	}

}

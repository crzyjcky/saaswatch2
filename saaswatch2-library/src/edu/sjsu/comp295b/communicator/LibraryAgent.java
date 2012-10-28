package edu.sjsu.comp295b.communicator;

import java.io.IOException;
import java.util.Properties;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.ILibraryListener;
import edu.sjsu.comp295b.Library;

public class LibraryAgent extends NotificationBroadcasterSupport
		implements
			LibraryAgentMBean,
			ILibraryListener {

	private static final Logger logger = LoggerFactory
			.getLogger(LibraryAgent.class);

	private Library library;
	private long sequenceNumber;

	public LibraryAgent(Library library) {
		this.library = library;
		
		library.addListener(this);
	}

	@Override
	public void setDebugEnabled(boolean isDebugEnabled) {

		library.setDebugEnabled(isDebugEnabled);
	}

	@Override
	public void onDebug(Object data) {

		logger.debug("onDebug");
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		
		try {
			
			json = mapper.writeValueAsString(data);
		} catch (JsonGenerationException e) {

			logger.debug("onDebug", e);
		} catch (JsonMappingException e) {

			logger.debug("onDebug", e);
		} catch (IOException e) {

			logger.debug("onDebug", e);
		}
		
		Notification notification = new Notification("onDebug", this,
				sequenceNumber++, System.currentTimeMillis(), json);

		sendNotification(notification);
	}

	@Override
	public void onKeepAlive() {
		
		logger.debug("onKeepAlive");
		Notification notification = new Notification("onKeepAlive", this,
				sequenceNumber++, System.currentTimeMillis(), null);

		sendNotification(notification);
	}

	@Override
	public void setKeepAliveInterval(int keepAliveInterval) {
		
		library.setKeepAliveInterval(keepAliveInterval);
	}

	@Override
	public void saveConfig(Properties newConfig) {

		logger.debug("saveConfig");
		library.saveConfig(newConfig);
	}
}

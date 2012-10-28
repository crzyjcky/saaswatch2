package edu.sjsu.comp295b.communicator.rest.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

import edu.sjsu.comp295b.communicator.rest.service.v0.DebugResource;


public class DebugApplication extends Application
{
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public DebugApplication()
	{
		singletons.add(new DebugResource());
		singletons.add(new JacksonJaxbJsonProvider());
	}

	@Override
	public Set<Class<?>> getClasses()
	{
		return empty;
	}

	@Override
	public Set<Object> getSingletons()
	{
		return singletons;
	}
}

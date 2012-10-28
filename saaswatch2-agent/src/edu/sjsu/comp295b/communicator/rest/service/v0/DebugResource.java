package edu.sjsu.comp295b.communicator.rest.service.v0;

import java.util.concurrent.BlockingQueue;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.sjsu.comp295b.Agent;
import edu.sjsu.comp295b.dto.DebugDTO;
import edu.sjsu.comp295b.dto.MemoryDTO;

@Path("/v0/agent")
public class DebugResource
{
	private static final Logger logger = LoggerFactory.getLogger(DebugResource.class);
	
	@GET
	@Path("/debugAsync")
	@Produces({"application/json", "application/xml"})
	public void getDebugAsync(@Context final ServletContext servletContext, @Suspend final AsynchronousResponse asyncResponse) {
		
		final Agent agent = (Agent) servletContext.getAttribute("agent");
		
		Thread t = new Thread() {
			
			@Override
			public void run() {
				
				BlockingQueue<DebugDTO> debugMessageQueue = agent.getDebugMessageQueue();
				DebugDTO debugDTO = null;
				try {
					
					// using producer-consumer pattern
					debugDTO = debugMessageQueue.take();
				} catch (InterruptedException e) {

					logger.debug("getDebugAsync", e);
				}

				Response response = Response.ok(debugDTO).type(MediaType.APPLICATION_JSON).build();
		
				asyncResponse.setResponse(response);
			}
		};
		
		t.start();
	}
	
	@GET
	@Path("/debug")
	//@Consumes({"application/json", "application/xml"})
	@Produces({"application/json", "application/xml"})
	public Response getDebugData() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MemoryDTO memoryDTO = new MemoryDTO();
		return Response.ok().entity(memoryDTO).build();
	}

	
	@GET
	@Path("/asyncDebug0")
	@Produces({"application/json", "application/xml"})
	public void getAsyncDebugData(@Suspend final AsynchronousResponse response) {
		
		Thread t = new Thread() {
			
			@Override
			public void run() {
				MemoryDTO memoryDTO = new MemoryDTO();
				Response jaxrs = Response.ok("fjhgj").type(MediaType.TEXT_PLAIN).build();
				//response.setResponse(jaxrs);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//response.setResponse(jaxrs);
				
				Response jaxrs2 = Response.status(202).entity("fds").type(MediaType.TEXT_PLAIN).build();
				
				response.setResponse(jaxrs2);
				//memoryDTO.heapCommitted = 1000;
				//jaxrs = Response.ok(memoryDTO).type(MediaType.APPLICATION_JSON).build();
				//response.setResponse(jaxrs);
				
			}
		};
		
		t.start();
	}
	
	@GET
	@Path("/context")
	@Produces({"application/json", "application/xml"})
	public Response getContextData(@Context ServletContext servletContext ) {
		
		Agent agent = (Agent) servletContext.getAttribute("agent");
		
		logger.debug("agent: " + agent);
		
		return Response.ok(agent.testContext()).type(MediaType.TEXT_PLAIN).build();
	}
	
	@GET
	@Path("/context2")
	@Produces({"application/json", "application/xml"})
	public void getContextData2(@Context final ServletContext servletContext, @Suspend final AsynchronousResponse response) {
		
		Thread t = new Thread() {
			
			@Override
			public void run() {
				MemoryDTO memoryDTO = new MemoryDTO();
				
				Agent agent = (Agent) servletContext.getAttribute("agent");
				Response jaxrs = Response.ok(agent.testContext()).type(MediaType.TEXT_PLAIN).build();
				//response.setResponse(jaxrs);
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//response.setResponse(jaxrs);
				

				response.setResponse(jaxrs);
				//memoryDTO.heapCommitted = 1000;
				//jaxrs = Response.ok(memoryDTO).type(MediaType.APPLICATION_JSON).build();
				//response.setResponse(jaxrs);
				
			}
		};
		
		t.start();
	}
}

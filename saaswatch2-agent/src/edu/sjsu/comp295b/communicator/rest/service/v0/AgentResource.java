package edu.sjsu.comp295b.communicator.rest.service.v0;

import java.util.List;
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
import edu.sjsu.comp295b.dto.ConnectionDTO;
import edu.sjsu.comp295b.dto.DebugDTO;
import edu.sjsu.comp295b.dto.MemoryDTO;
import edu.sjsu.comp295b.dto.OSDTO;

@Path("/v0/agent")
public class AgentResource
{
	private static final Logger logger = LoggerFactory.getLogger(AgentResource.class);
	
	@GET
	@Path("/debugAsync")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public void getDebugDTOAsync(@Context final ServletContext servletContext, @Suspend final AsynchronousResponse asyncResponse) {
		
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
	@Path("/connections")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getConnectionDTOs(@Context final ServletContext servletContext) {
		
		Agent agent = (Agent) servletContext.getAttribute("agent");
		List<ConnectionDTO> connectionDTOs = agent.getCurrentConnectionDTOs();
		
		return Response.ok(connectionDTOs).build();
	}
	
	@GET
	@Path("/applicationMemory")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getApplicationMemoryDTO(@Context final ServletContext servletContext) {
		
		Agent agent = (Agent) servletContext.getAttribute("agent");
		MemoryDTO memoryDTO = agent.getCurrentApplicationMemoryDTO();
		
		return Response.ok(memoryDTO).build();
	}
	
	@GET
	@Path("/agentMemory")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getAgentMemoryDTO(@Context final ServletContext servletContext) {
		
		Agent agent = (Agent) servletContext.getAttribute("agent");
		MemoryDTO memoryDTO = agent.getCurrentAgentMemoryDTO();
		
		return Response.ok(memoryDTO).build();
	}
	
	@GET
	@Path("/applicationOS")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getApplicationOSDTO(@Context ServletContext servletContext) {
		
		Agent agent = (Agent) servletContext.getAttribute("agent");
		OSDTO osDTO = agent.getCurrentApplicationOSDTO();
		logger.debug("agent.getCurrentApplicationOSDTO(): " + agent.getCurrentApplicationOSDTO());
		
		return Response.ok(osDTO).build();
	}
	
	@GET
	@Path("/agentOS")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getAgentOSDTO(@Context final ServletContext servletContext) {
		
		Agent agent = (Agent) servletContext.getAttribute("agent");
		OSDTO osDTO = agent.getCurrentAgentOSDTO();
		
		return Response.ok(osDTO).build();
	}

}

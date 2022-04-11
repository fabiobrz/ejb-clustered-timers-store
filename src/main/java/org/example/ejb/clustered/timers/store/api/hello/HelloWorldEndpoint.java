package org.example.ejb.clustered.timers.store.api.hello;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

@Path("/hello")
public class HelloWorldEndpoint {

	private static Logger log = Logger.getLogger(HelloWorldEndpoint.class.getName());

	@GET
	@Produces("text/plain")
	public Response doGet() {
		log.debug("HelloWorldEndpoint.doGet called");
		return Response.ok("Hello from the EJB Timer Execution Store Bootable jar!").build();
	}
}

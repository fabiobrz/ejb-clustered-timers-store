package org.example.ejb.timer.expiration.store.api.timer;

import org.example.ejb.timer.expiration.store.model.TimerExpirationDAOImpl;
import org.example.ejb.timer.expiration.store.model.TimerExpirationEntity;

import javax.inject.Inject;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.Instant;
import java.util.List;

/**
 * Expose a Timer service REST APIs.
 */
@Path("/timer")
public class TimerManagementEndpoint {
	@Inject
	private UserTransaction tx;

	@Inject
	private TimerExpirationDAOImpl timerExpirationDAO;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTimerExpiration(@Context UriInfo info, TimerExpirationEntity data) {
		TimerExpirationEntity timerExpiration;
		try {
			tx.begin();
			try {
				timerExpiration = timerExpirationDAO.createTimerExpiration(data);
				tx.commit();
			} catch (RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
				tx.rollback();
				throw new WebApplicationException("Transaction management error", ex);
			}
		} catch (NotSupportedException | SystemException ex) {
			throw new WebApplicationException("Transaction management error", ex);
		}
		String rawPath = String.format("%s/id/%s", info.getAbsolutePath().getRawPath(), timerExpiration.getId().toString());
		UriBuilder uriBuilder = info.getAbsolutePathBuilder().replacePath(rawPath);
		URI uri = uriBuilder.build();
		return Response.created(uri).build();
	}

	@GET
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExpiration(@PathParam("id") Long id) {
		try {
			tx.begin();
			try {
				final TimerExpirationEntity timerExpiration = timerExpirationDAO.getTimerExpiration(id);
				tx.commit();
				return Response.ok(timerExpiration).build();
			} catch (RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
				tx.rollback();
				throw new WebApplicationException("Transaction management error", ex);
			}
		} catch (NotSupportedException | SystemException ex) {
			throw new WebApplicationException("Transaction management error", ex);
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExpirations() {
		try {
			tx.begin();
			try {
				final List<TimerExpirationEntity> timerExpirations = timerExpirationDAO.getAll();
				tx.commit();
				return Response.ok(timerExpirations).build();
			} catch (RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
				tx.rollback();
				throw new WebApplicationException("Transaction management error", ex);
			}
		} catch (NotSupportedException | SystemException ex) {
			throw new WebApplicationException("Transaction management error", ex);
		}
	}

	@GET
	@Path("/range")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExpirations(@QueryParam("from") Instant from, @QueryParam("to") Instant to) {
		try {
			tx.begin();
			try {
				final List<TimerExpirationEntity> timerExpirations = timerExpirationDAO.getTimerExpirations(from, to);
				tx.commit();
				return Response.ok(timerExpirations).build();
			} catch (RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
				tx.rollback();
				throw new WebApplicationException("Transaction management error", ex);
			}
		} catch (NotSupportedException | SystemException ex) {
			throw new WebApplicationException("Transaction management error", ex);
		}
	}

	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response deleteTimerExpirations() {
		try {
			tx.begin();
			try {
				final int affectedTimerExpirations = timerExpirationDAO.deleteAll();
				tx.commit();
				return Response.ok(affectedTimerExpirations).build();
			} catch (RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
				tx.rollback();
				throw new WebApplicationException("Transaction management error", ex);
			}
		} catch (NotSupportedException | SystemException ex) {
			throw new WebApplicationException("Transaction management error", ex);
		}
	}
}

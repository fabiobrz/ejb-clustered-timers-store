package org.example.ejb.clustered.timers.store.api.timer;

import org.example.ejb.clustered.timers.store.model.TimerExecutionDAOImpl;
import org.example.ejb.clustered.timers.store.model.TimerExecutionEntity;

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
	private TimerExecutionDAOImpl timerExecutionDao;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response createTimerExecution(@Context UriInfo info, TimerExecutionEntity data) {
		TimerExecutionEntity timerExecution;
		try {
			tx.begin();
			try {
				timerExecution = timerExecutionDao.createTimerExecution(data);
				tx.commit();
			} catch (RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
				tx.rollback();
				throw new WebApplicationException("Transaction management error", ex);
			}
		} catch (NotSupportedException | SystemException ex) {
			throw new WebApplicationException("Transaction management error", ex);
		}
		String rawPath = String.format("%s/id/%s", info.getAbsolutePath().getRawPath(), timerExecution.getId().toString());
		UriBuilder uriBuilder = info.getAbsolutePathBuilder().replacePath(rawPath);
		URI uri = uriBuilder.build();
		return Response.created(uri).build();
	}

	@GET
	@Path("/id/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getTimerExecution(@PathParam("id") Long id) {
		try {
			tx.begin();
			try {
				final TimerExecutionEntity timerExecution = timerExecutionDao.getTimerExecution(id);
				tx.commit();
				return Response.ok(timerExecution).build();
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
	public Response getTimerExecutions() {
		try {
			tx.begin();
			try {
				final List<TimerExecutionEntity> timerExecutions = timerExecutionDao.getAll();
				tx.commit();
				return Response.ok(timerExecutions).build();
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
	public Response getTimerExecutions(@QueryParam("from") Instant from, @QueryParam("to") Instant to) {
		try {
			tx.begin();
			try {
				final List<TimerExecutionEntity> timerExecutions = timerExecutionDao.getTimerExecutions(from, to);
				tx.commit();
				return Response.ok(timerExecutions).build();
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
	public Response deleteTimerExecutions() {
		try {
			tx.begin();
			try {
				final int affectedTimerExecutions = timerExecutionDao.deleteAll();
				tx.commit();
				return Response.ok(affectedTimerExecutions).build();
			} catch (RollbackException | HeuristicMixedException | HeuristicRollbackException ex) {
				tx.rollback();
				throw new WebApplicationException("Transaction management error", ex);
			}
		} catch (NotSupportedException | SystemException ex) {
			throw new WebApplicationException("Transaction management error", ex);
		}
	}
}

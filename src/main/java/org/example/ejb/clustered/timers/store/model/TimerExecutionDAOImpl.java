package org.example.ejb.clustered.timers.store.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;

/**
 * Provides functionality for manipulation timer executions using the persistence context.
 */
@RequestScoped

public class TimerExecutionDAOImpl implements TimerExecutionDAO {

	@PersistenceContext(type = PersistenceContextType.EXTENDED, unitName = "primary")
	private EntityManager em;

	EntityManager getEM() {
		//        if(em == null) {
		//             EntityManagerFactory emf = Persistence.createEntityManagerFactory("primary");
		//             em=emf.createEntityManager();
		//        }
		return em;
	}

	@Override
	public TimerExecutionEntity createTimerExecution(TimerExecutionEntity timerExecution) {
		if (!getEM().contains(timerExecution)) {
			timerExecution = getEM().merge(timerExecution);
		}
		getEM().persist(timerExecution);

		return timerExecution;
	}

	@Override
	public List<TimerExecutionEntity> getAll() {
		final TypedQuery<TimerExecutionEntity> query = querySelectAllTimerExecutions();
		return query.getResultList();
	}

	@Override
	public TimerExecutionEntity getTimerExecution(Long id) {
		final TypedQuery<TimerExecutionEntity> query = querySelectTimerExecution(id);
		return query.getSingleResult();
	}

	@Override
	public List<TimerExecutionEntity> getTimerExecutions(Instant from, Instant to) {
		final TypedQuery<TimerExecutionEntity> query = querySelectTimerExecutions(from, to);
		return query.getResultList();
	}

	@Override
	public List<TimerExecutionEntity> getRange(int offset, int count) {
		final TypedQuery<TimerExecutionEntity> query = querySelectAllTimerExecutions();
		query.setMaxResults(count);
		query.setFirstResult(offset);
		return query.getResultList();
	}

	@Override
	public void deleteTimerExecution(Long id) {
		TimerExecutionEntity timerExecution = getTimerExecution(id);
		if (!getEM().contains(timerExecution)) {
			timerExecution = getEM().merge(timerExecution);
		}
		getEM().remove(timerExecution);
	}

	@Override
	public int deleteAll() {
		final Query query = queryDeleteAllTimerExecutions();
		return query.executeUpdate();
	}

	private TypedQuery<TimerExecutionEntity> querySelectAllTimerExecutions() {
		return getEM().createQuery(String.format("SELECT t FROM %s t", TimerExecutionEntity.class.getSimpleName()),
				TimerExecutionEntity.class);
	}

	private TypedQuery<TimerExecutionEntity> querySelectTimerExecution(Long id) {
		return getEM().createQuery(
				String.format("SELECT t FROM %s t WHERE t.id = ?1", TimerExecutionEntity.class.getSimpleName()),
				TimerExecutionEntity.class)
				.setParameter(1, id);
	}

	private TypedQuery<TimerExecutionEntity> querySelectTimerExecutions(Instant from, Instant to) {
		return getEM().createQuery(
				String.format("SELECT t FROM %s t WHERE t.timestamp >= ?1 and t.timestamp <= ?2",
						TimerExecutionEntity.class.getSimpleName()),
				TimerExecutionEntity.class)
				.setParameter(1, from)
				.setParameter(2, to);
	}

	private Query queryDeleteAllTimerExecutions() {
		return getEM().createQuery(String.format("DELETE FROM %s", TimerExecutionEntity.class.getSimpleName()));
	}
}

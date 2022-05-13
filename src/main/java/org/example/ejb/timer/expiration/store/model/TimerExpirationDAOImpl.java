package org.example.ejb.timer.expiration.store.model;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;

/**
 * Provides functionality for manipulation timer expirations using the persistence context.
 */
@RequestScoped

public class TimerExpirationDAOImpl implements TimerExpirationDAO {

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
	public TimerExpirationEntity createTimerExpiration(TimerExpirationEntity timerExpiration) {
		if (!getEM().contains(timerExpiration)) {
			timerExpiration = getEM().merge(timerExpiration);
		}
		getEM().persist(timerExpiration);

		return timerExpiration;
	}

	@Override
	public List<TimerExpirationEntity> getAll() {
		final TypedQuery<TimerExpirationEntity> query = querySelectAllTimerExpirations();
		return query.getResultList();
	}

	@Override
	public TimerExpirationEntity getTimerExpiration(Long id) {
		final TypedQuery<TimerExpirationEntity> query = querySelectTimerExpiration(id);
		return query.getSingleResult();
	}

	@Override
	public List<TimerExpirationEntity> getTimerExpirations(Instant from, Instant to) {
		final TypedQuery<TimerExpirationEntity> query = querySelectTimerExpirations(from, to);
		return query.getResultList();
	}

	@Override
	public List<TimerExpirationEntity> getRange(int offset, int count) {
		final TypedQuery<TimerExpirationEntity> query = querySelectAllTimerExpirations();
		query.setMaxResults(count);
		query.setFirstResult(offset);
		return query.getResultList();
	}

	@Override
	public void deleteTimerExpiration(Long id) {
		TimerExpirationEntity timerExpiration = getTimerExpiration(id);
		if (!getEM().contains(timerExpiration)) {
			timerExpiration = getEM().merge(timerExpiration);
		}
		getEM().remove(timerExpiration);
	}

	@Override
	public int deleteAll() {
		final Query query = queryDeleteAllTimerExpirations();
		return query.executeUpdate();
	}

	private TypedQuery<TimerExpirationEntity> querySelectAllTimerExpirations() {
		return getEM().createQuery(String.format("SELECT t FROM %s t", TimerExpirationEntity.class.getSimpleName()),
				TimerExpirationEntity.class);
	}

	private TypedQuery<TimerExpirationEntity> querySelectTimerExpiration(Long id) {
		return getEM().createQuery(
				String.format("SELECT t FROM %s t WHERE t.id = ?1", TimerExpirationEntity.class.getSimpleName()),
				TimerExpirationEntity.class)
				.setParameter(1, id);
	}

	private TypedQuery<TimerExpirationEntity> querySelectTimerExpirations(Instant from, Instant to) {
		return getEM().createQuery(
				String.format("SELECT t FROM %s t WHERE t.timestamp >= ?1 and t.timestamp <= ?2",
						TimerExpirationEntity.class.getSimpleName()),
				TimerExpirationEntity.class)
				.setParameter(1, from)
				.setParameter(2, to);
	}

	private Query queryDeleteAllTimerExpirations() {
		return getEM().createQuery(String.format("DELETE FROM %s", TimerExpirationEntity.class.getSimpleName()));
	}
}

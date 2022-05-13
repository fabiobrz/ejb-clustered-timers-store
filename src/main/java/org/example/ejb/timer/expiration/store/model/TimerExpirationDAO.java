package org.example.ejb.timer.expiration.store.model;

import java.time.Instant;
import java.util.List;

/**
 * Basic operations for manipulation of timer expirations
 */
public interface TimerExpirationDAO {

	TimerExpirationEntity createTimerExpiration(TimerExpirationEntity timerExpiration);

	List<TimerExpirationEntity> getAll();

	TimerExpirationEntity getTimerExpiration(Long id);

	List<TimerExpirationEntity> getTimerExpirations(Instant from, Instant to);

	List<TimerExpirationEntity> getRange(int offset, int count);

	void deleteTimerExpiration(Long id);

	int deleteAll();
}
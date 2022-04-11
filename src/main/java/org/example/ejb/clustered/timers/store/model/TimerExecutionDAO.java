package org.example.ejb.clustered.timers.store.model;

import java.time.Instant;
import java.util.List;

/**
 * Basic operations for manipulation of timer executions
 */
public interface TimerExecutionDAO {

	TimerExecutionEntity createTimerExecution(TimerExecutionEntity timerExecution);

	List<TimerExecutionEntity> getAll();

	TimerExecutionEntity getTimerExecution(Long id);

	List<TimerExecutionEntity> getTimerExecutions(Instant from, Instant to);

	List<TimerExecutionEntity> getRange(int offset, int count);

	void deleteTimerExecution(Long id);

	int deleteAll();
}
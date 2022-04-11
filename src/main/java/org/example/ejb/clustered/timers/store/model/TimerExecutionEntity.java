package org.example.ejb.clustered.timers.store.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * User's timer execution entity which is marked up with JPA annotations.
 */
@Entity
public class TimerExecutionEntity implements Serializable {

	private static final long serialVersionUID = 4410988609879455828L;

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;
	private String executor;
	private String name;
	private String info;
	private Instant timestamp;

	public TimerExecutionEntity() {
		this(null, null, null, null);
	}

	public TimerExecutionEntity(String name, String executor, String info, Instant timestamp) {
		super();
		this.executor = executor;
		this.name = name;
		this.info = info;
		this.timestamp = timestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getExecutor() {
		return executor;
	}

	
	public void setExecutor(String executor) {
		this.executor = executor;
	}

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public String getInfo() {
		return info;
	}

	
	public void setInfo(String info) {
		this.info = info;
	}

	
	public Instant getTimestamp() {
		return timestamp;
	}

	
	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		TimerExecutionEntity that = (TimerExecutionEntity) o;
		return executor.equals(that.executor) && name.equals(that.name) && info.equals(that.info)
				&& timestamp.equals(that.timestamp);
	}

	
	public int hashCode() {
		return Objects.hash(executor, name, info, timestamp);
	}

	
	public String toString() {
		return "TimerExecutionEntity{" +
				"id=" + id +
				", executor='" + executor + '\'' +
				", name='" + name + '\'' +
				", info='" + info + '\'' +
				", timestamp=" + timestamp +
				'}';
	}
}

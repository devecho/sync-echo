package de.devsurf.echo.sync.jobs.persistence;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.common.collect.Lists;

import de.devsurf.echo.sync.persistence.FieldEntity;

@Entity
@Table(name = "jobs")
@NamedQueries(@NamedQuery(name = "jobs.findAll", query = "SELECT p FROM JobEntity p"))
public class JobEntity {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;

	@Column(name = "user", nullable = false)
	private long user;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "description", length = 256)
	private String description;

	@Column(name = "enabled", nullable = false)
	private boolean enabled;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "source")
	private JobTargetEntity source;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "target")
	private JobTargetEntity target;

	@ElementCollection
	@CollectionTable(name = "job_fields", joinColumns = @JoinColumn(name = "job_field_id"))
	@Column(name = "job_field")
	private List<FieldEntity> fields;

	public JobEntity() {
		fields = Lists.newArrayListWithExpectedSize(3);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public List<FieldEntity> getFields() {
		return fields;
	}

	public void setFields(List<FieldEntity> fields) {
		this.fields = fields;
	}

	public JobTargetEntity getSource() {
		return source;
	}

	public void setSource(JobTargetEntity source) {
		this.source = source;
	}

	public JobTargetEntity getTarget() {
		return target;
	}

	public void setTarget(JobTargetEntity target) {
		this.target = target;
	}
}

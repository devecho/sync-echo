package de.devsurf.echo.sync.jobs.persistence;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.google.common.collect.Lists;

import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.persistence.FieldEntity;

@Entity
@Table(name = "job_targets")
public class JobTargetEntity {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	@OneToOne
	@JoinColumn(name = "job_target_link", nullable = false)
	private LinkEntity link;

	@ElementCollection
	@CollectionTable(name = "job_target_fields", joinColumns = @JoinColumn(name = "job_target_field_id"))
	@Column(name = "job_target_field")
	private List<FieldEntity> fields;

	public JobTargetEntity() {
		fields = Lists.newArrayListWithExpectedSize(3);
	}

	public LinkEntity getLink() {
		return link;
	}

	public void setLink(LinkEntity link) {
		this.link = link;
	}

	public List<FieldEntity> getFields() {
		return fields;
	}

	public void setFields(List<FieldEntity> fields) {
		this.fields = fields;
	}
}
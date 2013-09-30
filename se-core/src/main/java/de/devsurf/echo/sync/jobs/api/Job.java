package de.devsurf.echo.sync.jobs.api;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.jobs.transport.JobPojo;

@JsonPropertyOrder({ "type", "id", "name", "description", "enabled", "source", "target", "data" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = JobPojo.class, name = "job") })
@JsonTypeName("job")
@JsonSerialize(include = Inclusion.NON_NULL)
public interface Job {

	public long getId();

	public String getName();

	public String getDescription();
	
	public boolean isEnabled();

	public JobSource getSource();

	public JobTarget getTarget();

	public List<Field> getData();

}
package de.devsurf.echo.sync.jobs.api;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;

import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.jobs.transport.JobPojo;

@JsonPropertyOrder({ "type", "id", "name", "description", "source", "target", "data" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = JobPojo.class, name = "job") })
@JsonTypeName("job")
public interface Job {

	public String getId();

	public String getName();

	public String getDescription();
	
	public boolean isActive();

	public String getSource();

	public String getTarget();

	public List<Field> getData();

}
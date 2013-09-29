package de.devsurf.echo.sync.jobs.api;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;

import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.jobs.transport.JobPojo;
import de.devsurf.echo.sync.links.api.Link;

@JsonPropertyOrder({ "type", "link", "data" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = JobPojo.class, name = "source") })
@JsonTypeName("source")
public interface JobSource {

	public Link getLink();

	public List<Field> getData();
	
}
package de.devsurf.echo.sync.links.api;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;

import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.links.transport.LinkPojo;

@JsonPropertyOrder({ "type", "id", "provider", "data" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = LinkPojo.class, name = "link") })
@JsonTypeName("link")
public interface Link {

	public long getId();

	public long getProvider();

	public List<Field> getData();
}
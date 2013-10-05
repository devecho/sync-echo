package de.devsurf.echo.sync.providers.api;

import java.net.URI;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import de.devsurf.echo.sync.providers.transport.ProviderPojo;

@JsonPropertyOrder({ "type", "id", "name", "url", "version" })
@JsonSerialize(include = Inclusion.ALWAYS)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = ProviderPojo.class, name = "provider") })
@JsonTypeName("provider")
public interface Provider {

	public long getId();

	public String getName();

	@JsonProperty("url")
	public URI getWebsite();

	public String getVersion();

	public ProviderAuthentication getAuth();

}
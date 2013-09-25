package de.devsurf.echo.sync.providers.api;

import java.net.URI;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import de.devsurf.echo.frameworks.rs.api.Typed;

@JsonPropertyOrder({ "type", "id", "name", "url", "version" })
@JsonSerialize(include = Inclusion.ALWAYS)
public interface Provider extends Typed {

	public long getId();

	public String getName();

	@JsonProperty("url")
	public URI getWebsite();

	public String getVersion();

	public ProviderAuthentication getAuth();

}
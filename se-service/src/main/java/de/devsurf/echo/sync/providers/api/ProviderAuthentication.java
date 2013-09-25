package de.devsurf.echo.sync.providers.api;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonPropertyOrder({ "mode", "data" })
@JsonSerialize(include = Inclusion.NON_EMPTY)
public interface ProviderAuthentication {

	public String getMode();

	public List<ProviderAuthenticationField> getData();

}
package de.devsurf.echo.sync.providers.api;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;


@JsonPropertyOrder({ "name", "type", "value" })
@JsonSerialize(include = Inclusion.NON_NULL)
public interface ProviderAuthenticationField {

	public ProviderAuthenticationFieldType getType();

	public String getName();

	public String getValue();

}
package de.devsurf.echo.sync.providers.api;

import java.util.List;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;

@JsonPropertyOrder({ "type", "mode", "data" })
@JsonSerialize(include = Inclusion.NON_EMPTY)
public interface ProviderAuthentication extends Typed {
	
	public static final Type TYPE = new Type() {
		@Override
		public String value() {
			return "authentication";
		}
	};

	public String getMode();

	public List<ProviderAuthenticationField> getData();

}
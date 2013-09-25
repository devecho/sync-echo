package de.devsurf.echo.sync.links.api;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;

@JsonPropertyOrder({ "type", "id", "provider", "data" })
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Link extends Typed {
	
	public static final Type TYPE = new Type() {
		@Override
		public String value() {
			return "link";
		}
	};

	public long getId();

	public long getProvider();

	public List<ProviderAuthenticationField> getData();

}
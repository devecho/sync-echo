package de.devsurf.echo.sync.providers.transport;

import java.net.URI;

import org.codehaus.jackson.annotate.JsonProperty;

import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.api.ProviderAuthentication;

public class ProviderPojo implements Provider {
	private long id;
	
	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	@JsonProperty("url")
	public URI getWebsite() {
		return null;
	}

	@Override
	public String getVersion() {
		return null;
	}

	@Override
	public ProviderAuthentication getAuth() {
		return null;
	}

}

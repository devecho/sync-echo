package de.devsurf.echo.sync.providers.transport;


import java.net.URI;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.api.ProviderAuthentication;


public class ProviderPojo implements Provider {
	private Long id;
	private String name;
	private URI url;
	private String version = "all";
	private ProviderAuthentication auth;

	@Override
	public Type type() {
		return TYPE;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public URI getWebsite() {
		return url;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public ProviderAuthentication getAuth() {
		return auth;
	}
}
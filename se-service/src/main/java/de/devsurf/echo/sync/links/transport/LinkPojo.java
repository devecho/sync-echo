package de.devsurf.echo.sync.links.transport;

import java.util.List;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;

public class LinkPojo implements Link {
	private long id;
	private long provider;
	private List<ProviderAuthenticationField> data;
	
	@Override
	public Type type() {
		return TYPE;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getProvider() {
		return provider;
	}

	public void setProvider(long provider) {
		this.provider = provider;
	}

	@Override
	public List<ProviderAuthenticationField> getData() {
		return data;
	}

	public void setData(List<ProviderAuthenticationField> data) {
		this.data = data;
	}
}
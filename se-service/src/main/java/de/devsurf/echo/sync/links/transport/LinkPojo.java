package de.devsurf.echo.sync.links.transport;

import java.util.List;

import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.links.api.Link;

public class LinkPojo implements Link {
	private long id;
	private long provider;
	private List<Field> data;
	
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
	public List<Field> getData() {
		return data;
	}

	public void setData(List<Field> data) {
		this.data = data;
	}
}
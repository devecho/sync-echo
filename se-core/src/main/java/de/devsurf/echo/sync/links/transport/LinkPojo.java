package de.devsurf.echo.sync.links.transport;

import java.util.List;

import com.google.common.collect.Lists;

import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.providers.api.Provider;

public class LinkPojo implements Link {
	private long id;
	private Provider provider;
	private List<Field> data;
	
	public LinkPojo() {
		data = Lists.newArrayListWithExpectedSize(3);
	}
	
	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
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
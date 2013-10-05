package de.devsurf.echo.sync.jobs.transport;

import java.util.List;

import com.google.common.collect.Lists;

import de.devsurf.echo.sync.api.Field;

public class AbstractJobLinkPojo {
	private long link;
	private List<Field> data;
	
	public AbstractJobLinkPojo() {
		data = Lists.newArrayListWithExpectedSize(3);
	}

	public long getLink() {
		return link;
	}

	public List<Field> getData() {
		return data;
	}
}
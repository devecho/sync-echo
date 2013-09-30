package de.devsurf.echo.sync.jobs.transport;

import java.util.List;

import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.jobs.api.Job;
import de.devsurf.echo.sync.jobs.api.JobSource;
import de.devsurf.echo.sync.jobs.api.JobTarget;

public class JobPojo implements Job {
	private long id;
	private String name;
	private String description;
	private boolean enabledd;
	private List<Field> data;
	private JobSource source;
	private JobTarget target;
	
	public JobPojo() {
//		data = Lists.newArrayListWithExpectedSize(3);
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
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
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public boolean isEnabled() {
		return enabledd;
	}

	public void setEnabled(boolean enabled) {
		this.enabledd = enabled;
	}

	@Override
	public JobSource getSource() {
		return source;
	}

	public void setSource(JobSource source) {
		this.source = source;
	}

	@Override
	public JobTarget getTarget() {
		return target;
	}	

	public void setTarget(JobTarget target) {
		this.target = target;
	}
	
	@Override
	public List<Field> getData() {
		return data;
	}

	public void setData(List<Field> data) {
		this.data = data;
	}
}
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
	private boolean active;
	private List<Field> data;
//	private String source;
//	private String target;

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
	public boolean isActive() {
		return active;
	}

	@Override
	public List<Field> getData() {
		return data;
	}

	@Override
	public JobSource getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JobTarget getTarget() {
		// TODO Auto-generated method stub
		return null;
	}	
}
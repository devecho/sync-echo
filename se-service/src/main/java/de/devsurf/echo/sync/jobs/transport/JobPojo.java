package de.devsurf.echo.sync.jobs.transport;

import java.util.List;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.jobs.api.Job;

public class JobPojo implements Job {
	private String id;
	private String name;
	private String description;
	private String source;
	private String target;

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTarget() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Field> getData() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
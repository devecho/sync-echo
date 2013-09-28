package de.devsurf.echo.sync.transport;

import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.api.FieldType;

public class FieldPojo implements Field {
	private String name;
	private FieldType kind;
	private String value;
	
	@Override
	public FieldType getKind() {
		return kind;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
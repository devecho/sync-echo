package de.devsurf.echo.sync.persistence;


import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;

import de.devsurf.echo.sync.api.FieldType;


@Embeddable
@MappedSuperclass
public class FieldEntity {
	private String name;
	private FieldType type;
	private String value;

	public FieldType getType() {
		return type;
	}
	
	public void setType(FieldType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
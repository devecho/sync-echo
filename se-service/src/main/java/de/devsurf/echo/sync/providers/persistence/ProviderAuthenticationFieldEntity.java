package de.devsurf.echo.sync.providers.persistence;


import javax.persistence.Embeddable;

import de.devsurf.echo.sync.providers.api.ProviderAuthenticationFieldType;


@Embeddable
public class ProviderAuthenticationFieldEntity {
	private String name;
	private ProviderAuthenticationFieldType type;
	private String value;
	
	public ProviderAuthenticationFieldType getType() {
		return type;
	}
	
	public void setType(ProviderAuthenticationFieldType type) {
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
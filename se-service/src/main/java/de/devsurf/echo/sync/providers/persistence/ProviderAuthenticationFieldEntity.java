package de.devsurf.echo.sync.providers.persistence;


import javax.persistence.Embeddable;

import de.devsurf.echo.sync.persistence.FieldEntity;


@Embeddable
public class ProviderAuthenticationFieldEntity extends FieldEntity {
	private boolean optional;
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOptional() {
		return optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}
}
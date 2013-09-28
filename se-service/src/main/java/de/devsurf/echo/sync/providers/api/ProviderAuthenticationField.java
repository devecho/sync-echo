package de.devsurf.echo.sync.providers.api;

import de.devsurf.echo.sync.api.Field;

public interface ProviderAuthenticationField extends Field {
	public String getDescription();
	
	public boolean isOptional();
}

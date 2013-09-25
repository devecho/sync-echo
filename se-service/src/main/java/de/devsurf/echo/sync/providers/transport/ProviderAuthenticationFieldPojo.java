package de.devsurf.echo.sync.providers.transport;

import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationFieldType;

public class ProviderAuthenticationFieldPojo implements ProviderAuthenticationField {
	private String name;
	private ProviderAuthenticationFieldType type;
	private String value;
	
	@Override
	public ProviderAuthenticationFieldType getType() {
		return type;
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
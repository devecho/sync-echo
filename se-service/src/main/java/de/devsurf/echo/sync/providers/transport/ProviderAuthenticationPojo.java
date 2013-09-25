package de.devsurf.echo.sync.providers.transport;

import java.util.List;

import com.google.common.collect.Lists;

import de.devsurf.echo.sync.providers.api.ProviderAuthentication;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;

public class ProviderAuthenticationPojo implements ProviderAuthentication {
	private String mode;
	private List<ProviderAuthenticationField> data;
	
	public ProviderAuthenticationPojo() {
		mode = "unknown";
		data = Lists.newArrayListWithExpectedSize(0);
	}
	
	public ProviderAuthenticationPojo(String mode) {
		this.mode = mode;
	}

	@Override
	public String getMode() {
		return mode;
	}
	
	@Override
	public List<ProviderAuthenticationField> getData() {
		return data;
	}
}
package de.devsurf.echo.sync.providers.converter;

import java.net.URI;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import de.devsurf.common.lang.converter.Converter;
import de.devsurf.common.lang.converter.InfoConverter;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.api.ProviderAuthentication;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;

@InfoConverter
public class ProviderInfoConverter implements Converter<ProviderEntity, Provider> {	
	@Override
	public Provider convertTo(final ProviderEntity source) {
		return new Provider() {
			private ProviderEntity delegate = source;

			@Override
			public long getId() {
				return delegate.getId();
			}

			@Override
			public String getName() {
				return delegate.getName();
			}

			@Override
			@JsonSerialize(include = Inclusion.NON_EMPTY)
			public String getVersion() {
				return null;
			}

			@Override
			@JsonSerialize(include = Inclusion.NON_EMPTY)
			public URI getWebsite() {
				return null;
			}

			@Override
			@JsonSerialize(include = Inclusion.NON_EMPTY)
			public ProviderAuthentication getAuth() {
				return null;
			}
		};
	}
}

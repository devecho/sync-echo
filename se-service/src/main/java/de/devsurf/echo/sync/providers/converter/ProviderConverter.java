package de.devsurf.echo.sync.providers.converter;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;

import de.devsurf.common.lang.converter.Converter;
import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.api.ProviderAuthentication;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;

public class ProviderConverter implements Converter<ProviderEntity, Provider> {
	@Inject
	private Converter<List<ProviderAuthenticationFieldEntity>, List<ProviderAuthenticationField>> fieldConverter;
	
	@Override
	public Provider convertTo(final ProviderEntity source) {
		return new Provider() {
			private ProviderEntity delegate = source;

			@Override
			public String getVersion() {
				return delegate.getVersion();
			}

			@Override
			public URI getWebsite() {
				return delegate.getWebsite();
			}

			@Override
			public String getName() {
				return delegate.getName();
			}

			@Override
			public long getId() {
				return delegate.getId();
			}

			@Override
			public ProviderAuthentication getAuth() {
				return new ProviderAuthentication() {
					private ProviderAuthenticationEntity delegate = source
							.getAuthentication();

					@Override
					public String getMode() {
						return delegate.getType();
					}
					
					@Override
					public Type type() {
						return TYPE;
					}

					@Override
					public List<ProviderAuthenticationField> getData() {
						return fieldConverter.convertTo(delegate.getFields());
					}
				};
			}
		};
	}
}

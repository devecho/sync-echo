package de.devsurf.echo.sync.providers.converter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.api.ProviderAuthentication;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationFieldType;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;
import de.devsurf.echo.sync.providers.transport.ProviderPojo;

public class ProviderConverter implements Converter<ProviderEntity, Provider> {
	@Override
	public Provider convert(final ProviderEntity source) {
		return new Provider() {
			private ProviderEntity delegate = source;

			@Override
			@JsonProperty("type")
			public Type type() {
				return ProviderPojo.TYPE;
			}

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
					public List<ProviderAuthenticationField> getData() {
						List<ProviderAuthenticationField> fields = new ArrayList<>();
						List<ProviderAuthenticationFieldEntity> authFields = delegate
								.getFields();
						if (authFields != null) {
							for (final ProviderAuthenticationFieldEntity field : authFields) {
								ProviderAuthenticationField authField = new ProviderAuthenticationField() {
									private ProviderAuthenticationFieldEntity delegate = field;

									@Override
									public String getValue() {
										return delegate.getValue();
									}

									@Override
									public ProviderAuthenticationFieldType getType() {
										return delegate.getType();
									}

									@Override
									public String getName() {
										return delegate.getName();
									}
								};
								fields.add(authField);
							}
						}

						return fields;
					}
				};
			}
		};
	}
}

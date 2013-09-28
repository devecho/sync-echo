package de.devsurf.echo.sync.providers.converter;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.sync.api.FieldType;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.api.ProviderAuthentication;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;

public class ProviderConverter implements Converter<ProviderEntity, Provider> {
	@Override
	public Provider convertTo(final ProviderEntity source) {
		return new Provider() {
			private ProviderEntity delegate = source;

			@Override
			public Type type() {
				return TYPE;
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
					public Type type() {
						return TYPE;
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
									public FieldType getKind() {
										return delegate.getType();
									}

									@Override
									public String getName() {
										return delegate.getName();
									}
									
									@Override
									public String getDescription() {
										return delegate.getDescription();
									}
									
									public boolean isOptional() {
										return delegate.isOptional();
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

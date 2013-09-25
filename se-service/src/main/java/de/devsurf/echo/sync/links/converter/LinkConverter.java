package de.devsurf.echo.sync.links.converter;

import java.util.ArrayList;
import java.util.List;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationFieldType;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;

public class LinkConverter implements Converter<LinkEntity, Link> {
	
	@Override
	public Link convert(final LinkEntity source) {
		return new Link() {
			private LinkEntity delegate = source;
			@Override
			public Type type() {
				return TYPE;
			}
			
			@Override
			public long getId() {
				return delegate.getId();
			}
			
			@Override
			public long getProvider() {
				return delegate.getProvider();
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
}

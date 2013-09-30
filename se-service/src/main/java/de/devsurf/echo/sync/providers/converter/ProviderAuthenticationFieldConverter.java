package de.devsurf.echo.sync.providers.converter;

import java.util.ArrayList;
import java.util.List;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.sync.api.FieldType;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;

public class ProviderAuthenticationFieldConverter
		implements
		Converter<List<ProviderAuthenticationFieldEntity>, List<ProviderAuthenticationField>> {

	@Override
	public List<ProviderAuthenticationField> convertTo(
			final List<ProviderAuthenticationFieldEntity> entities) {
		List<ProviderAuthenticationField> fields = new ArrayList<>();
		if (entities != null) {
			for (final ProviderAuthenticationFieldEntity entity : entities) {
				ProviderAuthenticationField field = new ProviderAuthenticationField() {
					private ProviderAuthenticationFieldEntity delegate = entity;

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
				fields.add(field);
			}
		}

		return fields;
	}
}

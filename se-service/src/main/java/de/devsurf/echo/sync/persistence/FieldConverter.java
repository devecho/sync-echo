package de.devsurf.echo.sync.persistence;

import java.util.ArrayList;
import java.util.List;

import de.devsurf.common.lang.converter.TwoWayConverter;
import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.api.FieldType;

public class FieldConverter implements TwoWayConverter<List<FieldEntity>, List<Field>> {

	@Override
	public List<FieldEntity> convertFrom(List<Field> fields) {
		List<FieldEntity> entities = new ArrayList<>();
		for (Field field : fields) {
			FieldEntity entity = new FieldEntity();
			entity.setName(field.getName());
			entity.setValue(field.getValue());
			entities.add(entity);
		}
		
		return entities;
	}

	@Override
	public List<Field> convertTo(final List<FieldEntity> entities) {
		List<Field> fields = new ArrayList<>();
		if (entities != null) {
			for (final FieldEntity entity : entities) {
				Field field = new Field() {
					private FieldEntity delegate = entity;

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
				};
				fields.add(field);
			}
		}
		return fields;
	}
}

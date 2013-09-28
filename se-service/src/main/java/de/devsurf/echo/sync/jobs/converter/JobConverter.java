package de.devsurf.echo.sync.jobs.converter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.codehaus.jackson.annotate.JsonIgnore;

import de.devsurf.echo.frameworks.rs.api.TwoWayConverter;
import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.api.FieldType;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.persistence.FieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderPersistency;

public class JobConverter implements TwoWayConverter<LinkEntity, Link> {
	@Inject
	private ProviderPersistency providers;
	
	@Override
	public LinkEntity convertFrom(Link source) {
		LinkEntity linkEntity = new LinkEntity();
		if(source.getId() != 0) {
			linkEntity.setId(source.getId());
		}
		ProviderEntity provider = providers.find(source.getProvider());
		linkEntity.setProvider(provider);
		
		linkEntity.setUser(1);//TODO change to user object and make link converter aware of context
		
		List<Field> fields = source.getData();
		List<FieldEntity> entities = new ArrayList<>();
		for(Field field : fields) {
			FieldEntity entity = new FieldEntity();
			entity.setName(field.getName());
			entity.setValue(field.getValue());
			entities.add(entity);
		}
		linkEntity.setFields(entities);
		
		return linkEntity;
	}
	
	@Override
	public Link convertTo(final LinkEntity source) {
		return new Link() {
			private LinkEntity delegate = source;
			
			@Override
			public long getId() {
				return delegate.getId();
			}
			
			@Override
			public long getProvider() {
				return delegate.getProvider().getId();
			}
			
			@Override
			public List<Field> getData() {
				List<Field> fields = new ArrayList<>();
				List<FieldEntity> authFields = delegate
						.getFields();
				if (authFields != null) {
					for (final FieldEntity field : authFields) {
						Field authField = new Field() {
							private FieldEntity delegate = field;

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
						fields.add(authField);
					}
				}

				return fields;
			}
		};
	}
}

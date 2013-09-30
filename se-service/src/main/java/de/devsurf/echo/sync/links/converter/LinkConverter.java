package de.devsurf.echo.sync.links.converter;

import java.util.List;

import javax.inject.Inject;

import de.devsurf.echo.frameworks.rs.api.TwoWayConverter;
import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.persistence.FieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderPersistency;

public class LinkConverter implements TwoWayConverter<LinkEntity, Link> {
	@Inject
	private ProviderPersistency providers;
	
	@Inject
	private TwoWayConverter<List<FieldEntity>, List<Field>> fieldConverter;
	
	@Override
	public LinkEntity convertFrom(Link source) {
		LinkEntity linkEntity = new LinkEntity();
		if(source.getId() != 0) {
			linkEntity.setId(source.getId());
		}
		ProviderEntity provider = providers.find(source.getProvider());
		linkEntity.setProvider(provider);
		
		linkEntity.setUser(1);//TODO change to user object and make link converter aware of context

		linkEntity.setFields(fieldConverter.convertFrom(source.getData()));
		
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
				return fieldConverter.convertTo(delegate.getFields());
			}
		};
	}
}

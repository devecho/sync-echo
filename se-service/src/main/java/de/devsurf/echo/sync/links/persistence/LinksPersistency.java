package de.devsurf.echo.sync.links.persistence;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class LinksPersistency {
	@Inject
	private EntityManager manager;
	
	public LinkEntity find(Long id) {
		return manager.find(LinkEntity.class, id);
	}
	
	public List<LinkEntity> findAll() {
		TypedQuery<LinkEntity> query = manager.createNamedQuery("findAll", LinkEntity.class);
		return query.getResultList();
	}
}

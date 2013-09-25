package de.devsurf.echo.sync.providers.persistence;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class ProviderPersistency {
	@Inject
	private EntityManager manager;
	
	public ProviderEntity find(Long id) {
		return manager.find(ProviderEntity.class, id);
	}
	
	public List<ProviderEntity> findAll() {
		TypedQuery<ProviderEntity> query = manager.createNamedQuery("findAll", ProviderEntity.class);
		return query.getResultList();
	}
}

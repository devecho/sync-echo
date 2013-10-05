package de.devsurf.echo.sync.providers.persistence;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;


public class ProviderPersistency {
	@Inject
	private EntityManager manager;
	
	public ProviderEntity get(long id) {
		//return manager.find(ProviderEntity.class, id);
		return manager.find(ProviderEntity.class, id);
	}
	
	public ProviderEntity info(long id) {
		TypedQuery<ProviderEntity> query = manager.createQuery("select p.id, p.name from ProviderEntity p where p.id = :id", ProviderEntity.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}
	
	public List<ProviderEntity> findAll() {
		TypedQuery<ProviderEntity> query = manager.createNamedQuery("providers.findAll", ProviderEntity.class);
		return query.getResultList();
	}
}

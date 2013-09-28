package de.devsurf.echo.sync.links.persistence;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import de.devsurf.echo.sync.persistence.ItemAlreadyExistsException;


public class LinksPersistency {
	@Inject
	private EntityManager manager;
	
	public LinkEntity merge(LinkEntity link) throws ItemAlreadyExistsException {
		EntityTransaction transaction = manager.getTransaction();
		try {
			transaction.begin();
			link = manager.merge(link);
			transaction.commit();			
		} catch(EntityExistsException ex) {
			throw new ItemAlreadyExistsException(link.getId(), "link", ex);
		}
		return link;
	}
	
	public LinkEntity persist(LinkEntity link) throws ItemAlreadyExistsException {
		EntityTransaction transaction = manager.getTransaction();
		try {
			transaction.begin();
			manager.persist(link);
			transaction.commit();			
		} catch(EntityExistsException ex) {
			throw new ItemAlreadyExistsException(link.getId(), "link", ex);
		}
		return link;
	}
	
	public void delete(LinkEntity link) {
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.remove(link);
		transaction.commit();
	}
	
	public LinkEntity find(long id) {
		return manager.find(LinkEntity.class, id);
	}
	
	public List<LinkEntity> findAll() {
		TypedQuery<LinkEntity> query = manager.createNamedQuery("links.findAll", LinkEntity.class);
		return query.getResultList();
	}
}

package de.devsurf.echo.sync.jobs.persistence;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import de.devsurf.echo.sync.persistence.ItemAlreadyExistsException;


public class JobsPersistency {
	@Inject
	private EntityManager manager;
	
	public JobEntity merge(JobEntity link) throws ItemAlreadyExistsException {
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
	
	public JobEntity persist(JobEntity link) throws ItemAlreadyExistsException {
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
	
	public void delete(JobEntity link) {
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.remove(link);
		transaction.commit();
	}
	
	public JobEntity find(long id) {
		return manager.find(JobEntity.class, id);
	}
	
	public List<JobEntity> findAll() {
		TypedQuery<JobEntity> query = manager.createNamedQuery("links.findAll", JobEntity.class);
		return query.getResultList();
	}
}

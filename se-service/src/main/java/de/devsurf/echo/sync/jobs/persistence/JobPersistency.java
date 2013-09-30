package de.devsurf.echo.sync.jobs.persistence;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

import de.devsurf.echo.sync.persistence.ItemAlreadyExistsException;


public class JobPersistency {
	@Inject
	private EntityManager manager;
	
	public JobEntity merge(JobEntity job) throws ItemAlreadyExistsException {
		EntityTransaction transaction = manager.getTransaction();
		try {
			transaction.begin();
			job = manager.merge(job);
			transaction.commit();			
		} catch(EntityExistsException ex) {
			throw new ItemAlreadyExistsException(job.getId(), "job", ex);
		}
		return job;
	}
	
	public JobEntity persist(JobEntity job) throws ItemAlreadyExistsException {
		EntityTransaction transaction = manager.getTransaction();
		try {
			transaction.begin();
			manager.persist(job);
			transaction.commit();			
		} catch(EntityExistsException ex) {
			throw new ItemAlreadyExistsException(job.getId(), "job", ex);
		}
		return job;
	}
	
	public void delete(JobEntity job) {
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.remove(job);
		transaction.commit();
	}
	
	public JobEntity find(long id) {
		return manager.find(JobEntity.class, id);
	}
	
	public List<JobEntity> findAll() {
		TypedQuery<JobEntity> query = manager.createNamedQuery("jobs.findAll", JobEntity.class);
		return query.getResultList();
	}
}

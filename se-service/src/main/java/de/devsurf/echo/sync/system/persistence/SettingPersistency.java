package de.devsurf.echo.sync.system.persistence;

import javax.inject.Inject;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import de.devsurf.echo.sync.persistence.ItemAlreadyExistsException;

public class SettingPersistency {
	@Inject
	private EntityManager manager;
	
	public SettingEntity get(String id) {
		return manager.find(SettingEntity.class, id);
	}
	
	public SettingEntity persist(SettingEntity setting) throws ItemAlreadyExistsException {
		EntityTransaction transaction = manager.getTransaction();
		try {
			transaction.begin();
			manager.persist(setting);
			transaction.commit();			
		} catch(EntityExistsException ex) {
			throw new ItemAlreadyExistsException(setting.getId(), "setting", ex);
		}
		return setting;
	}
	
	public void delete(SettingEntity link) {
		EntityTransaction transaction = manager.getTransaction();
		transaction.begin();
		manager.remove(link);
		transaction.commit();
	}
}

package de.devsurf.echo.sync.persistence;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistencyProvider implements Provider<EntityManager> {
	private static final EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("se-persistence");

	@Override
	public EntityManager get() {
		return emf.createEntityManager();
	}
}

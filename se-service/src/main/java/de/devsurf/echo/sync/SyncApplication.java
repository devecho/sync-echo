package de.devsurf.echo.sync;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.ApplicationPath;

import org.glassfish.hk2.api.ServiceLocator;

import com.google.inject.Provider;

import de.devsurf.echo.frameworks.rs.service.startup.guice.GuicyfiedApplication;

@ApplicationPath("/")
public class SyncApplication extends GuicyfiedApplication {
	@Inject
	public SyncApplication(ServiceLocator serviceLocator) {
		super(serviceLocator);
	}
	
	@Override
	public void shutdown() {
		Provider<EntityManager> provider = injector.getProvider(EntityManager.class);
		provider.get().close();
	}
}

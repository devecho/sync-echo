package de.devsurf.echo.sync.system;

import java.net.URI;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import de.devsurf.echo.sync.providers.api.ProviderAuthenticationFieldType;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;

public class Setup {
	@Inject
	private EntityManager manager;
	
	public String doIt() throws Exception {
		ProviderAuthenticationEntity basicAuth = new ProviderAuthenticationEntity("basic");
		ProviderAuthenticationFieldEntity username = new ProviderAuthenticationFieldEntity();
		username.setName("username");
		username.setType(ProviderAuthenticationFieldType.TEXT);
		basicAuth.getFields().add(username);
		
		ProviderAuthenticationFieldEntity password = new ProviderAuthenticationFieldEntity();
		password.setName("password");
		password.setType(ProviderAuthenticationFieldType.PASSWORD);
		basicAuth.getFields().add(password);
		
		ProviderAuthenticationFieldEntity url = new ProviderAuthenticationFieldEntity();
		url.setName("url");
		url.setType(ProviderAuthenticationFieldType.URL);
		basicAuth.getFields().add(url);
		
		ProviderEntity fnsProvider = new ProviderEntity();
		fnsProvider.setName("fileNshare");
		fnsProvider.setWebsite(new URI("https://www.filenshare.com"));
		fnsProvider.setVersion("all");
		fnsProvider.setAuthentication(basicAuth);
		
		EntityTransaction transaction = manager.getTransaction();
		try{
			transaction.begin();
//			manager.persist(basicAuth);
			manager.persist(fnsProvider);
			transaction.commit();
		} finally {
			manager.close();
		}
		
		return "done";
	}
}

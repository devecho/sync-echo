package de.devsurf.echo.sync.system;

import java.net.URI;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import de.devsurf.echo.sync.api.FieldType;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;

public class Setup {
	@Inject
	private EntityManager manager;
	
	//FIXME
	public static boolean DONE = false;
	
	public String doIt() throws Exception {
		if(DONE) {
			return "already done";
		}
		
		ProviderAuthenticationEntity basicAuth = new ProviderAuthenticationEntity("basic");
		ProviderAuthenticationFieldEntity username = new ProviderAuthenticationFieldEntity();
		username.setName("username");
		username.setType(FieldType.TEXT);
		basicAuth.getFields().add(username);
		
		ProviderAuthenticationFieldEntity password = new ProviderAuthenticationFieldEntity();
		password.setName("password");
		password.setType(FieldType.PASSWORD);
		basicAuth.getFields().add(password);
		
		ProviderAuthenticationFieldEntity url = new ProviderAuthenticationFieldEntity();
		url.setName("url");
		url.setType(FieldType.URL);
		basicAuth.getFields().add(url);
		
		ProviderAuthenticationFieldEntity version = new ProviderAuthenticationFieldEntity();
		version.setName("version");
		version.setType(FieldType.TEXT);
		version.setOptional(true);
		basicAuth.getFields().add(version);
		
		ProviderEntity fnsProvider = new ProviderEntity();
		fnsProvider.setName("fileNshare");
		fnsProvider.setWebsite(new URI("https://www.filenshare.com"));
		fnsProvider.setVersion("all");
		fnsProvider.setAuthentication(basicAuth);
		
		EntityTransaction transaction = manager.getTransaction();
		try{
			transaction.begin();
			manager.persist(fnsProvider);
			transaction.commit();
		} finally {
			manager.close();
		}
		
		DONE = true;
		return "done";
	}
}

package de.devsurf.echo.sync.system;

import java.net.URI;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import de.devsurf.echo.sync.api.FieldType;
import de.devsurf.echo.sync.jobs.persistence.JobEntity;
import de.devsurf.echo.sync.jobs.persistence.JobTargetEntity;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
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
		transaction.begin();
		manager.persist(fnsProvider);
		transaction.commit();
		System.out.println("id: "+fnsProvider.getId());
		transaction.begin();
		
		LinkEntity link = new LinkEntity();
		link.getFields().add(username);
		link.getFields().add(password);
		link.getFields().add(url);
		link.getFields().add(version);
		link.setProvider(fnsProvider);
		
		manager.persist(link);
		transaction.commit();
		transaction.begin();
		
		JobEntity job = new JobEntity();
		job.setEnabled(true);
		job.setDescription("Copy");
		job.setName("fileNshare copier");
		
		JobTargetEntity source = new JobTargetEntity();
		source.setLink(link);
		job.setSource(source);
		
		JobTargetEntity target = new JobTargetEntity();
		target.setLink(link);
		job.setTarget(target);
		
		manager.persist(job);
		transaction.commit();

		manager.close();
		
		DONE = true;
		return "done";
	}
}

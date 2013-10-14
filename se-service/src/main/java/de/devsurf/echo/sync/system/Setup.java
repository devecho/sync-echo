package de.devsurf.echo.sync.system;

import java.net.URI;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import de.devsurf.echo.sync.api.FieldType;
import de.devsurf.echo.sync.jobs.persistence.JobEntity;
import de.devsurf.echo.sync.jobs.persistence.JobTargetEntity;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.persistence.FieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;
import de.devsurf.echo.sync.system.persistence.SettingPersistency;

public class Setup {
	@Inject
	private EntityManager manager;
	
	@Inject
	private SettingPersistency settings;
	
	//FIXME
	public static boolean DONE = false;
	
	public String doIt() throws Exception {
		if(DONE) {
			return "already done";
		}
		
		ProviderAuthenticationEntity basicAuth = new ProviderAuthenticationEntity("basic");
		ProviderAuthenticationFieldEntity usernameDescriptor = new ProviderAuthenticationFieldEntity();
		usernameDescriptor.setName("username");
		usernameDescriptor.setType(FieldType.TEXT);
		basicAuth.getFields().add(usernameDescriptor);
		
		ProviderAuthenticationFieldEntity passwordDescriptor = new ProviderAuthenticationFieldEntity();
		passwordDescriptor.setName("password");
		passwordDescriptor.setType(FieldType.PASSWORD);
		basicAuth.getFields().add(passwordDescriptor);
		
		ProviderAuthenticationFieldEntity urlDescriptor = new ProviderAuthenticationFieldEntity();
		urlDescriptor.setName("url");
		urlDescriptor.setType(FieldType.URL);
		basicAuth.getFields().add(urlDescriptor);
		
		ProviderAuthenticationFieldEntity versionDescriptor = new ProviderAuthenticationFieldEntity();
		versionDescriptor.setName("version");
		versionDescriptor.setType(FieldType.TEXT);
		versionDescriptor.setOptional(true);
		basicAuth.getFields().add(versionDescriptor);
		
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
		
		FieldEntity usernameField = new ProviderAuthenticationFieldEntity();
		usernameField.setName("username");
		usernameField.setValue("daniel.manzke@googlemail.com");
		FieldEntity passwordField = new ProviderAuthenticationFieldEntity();
		passwordField.setName("password");
		passwordField.setValue("1.0");
		FieldEntity urlField = new ProviderAuthenticationFieldEntity();
		urlField.setName("url");
		urlField.setValue("http://github.com/devecho");
		FieldEntity versionField = new ProviderAuthenticationFieldEntity();
		versionField.setName("version");
		versionField.setValue("1.0");
		

		LinkEntity link = new LinkEntity();
		link.getFields().add(usernameField);
		link.getFields().add(passwordField);
		link.getFields().add(urlField);
		link.getFields().add(versionField);
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

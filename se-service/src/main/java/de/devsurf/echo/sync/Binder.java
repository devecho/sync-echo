package de.devsurf.echo.sync;

import java.lang.reflect.Type;
import java.util.EnumSet;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.google.inject.name.Named;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.frameworks.rs.api.Converter.InfoConverter;
import de.devsurf.echo.frameworks.rs.api.InstallableModule;
import de.devsurf.echo.frameworks.rs.api.TwoWayConverter;
import de.devsurf.echo.frameworks.rs.system.api.Framework;
import de.devsurf.echo.frameworks.rs.system.api.GenericBinder;
import de.devsurf.echo.frameworks.rs.system.api.ResourceBinder;
import de.devsurf.echo.frameworks.rs.system.api.TypeLiteralBuilder;
import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.jobs.api.Job;
import de.devsurf.echo.sync.jobs.api.JobSource;
import de.devsurf.echo.sync.jobs.api.JobTarget;
import de.devsurf.echo.sync.jobs.converter.JobConverter;
import de.devsurf.echo.sync.jobs.converter.JobSourceConverter;
import de.devsurf.echo.sync.jobs.converter.JobTargetConverter;
import de.devsurf.echo.sync.jobs.persistence.JobEntity;
import de.devsurf.echo.sync.jobs.persistence.JobTargetEntity;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.links.converter.LinkConverter;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.persistence.FieldConverter;
import de.devsurf.echo.sync.persistence.FieldEntity;
import de.devsurf.echo.sync.persistence.PersistencyProvider;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;
import de.devsurf.echo.sync.providers.converter.ProviderAuthenticationFieldConverter;
import de.devsurf.echo.sync.providers.converter.ProviderConverter;
import de.devsurf.echo.sync.providers.converter.ProviderInfoConverter;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;
//import de.devsurf.echo.sync.security.api.SecuredStore;
//import de.devsurf.echo.sync.security.internal.ServerSecuredStore;
import de.devsurf.echo.sync.system.Setup;
import de.devsurf.echo.sync.users.persistence.SecureRandomProvider;
import de.devsurf.echo.sync.users.persistence.SecureRandomProvider.SecureRandom;
import de.devsurf.echo.sync.utils.Mailing.MailConfiguration;

public class Binder implements InstallableModule {
	@Inject
	private ResourceBinder binder;

	@Inject
	private GenericBinder genericBinder;

	@Inject
	private TypeLiteralBuilder literalBuilder;
	
	@Inject
	@Named("email.username")
	private String username;
	
	@Inject
	@Named("email.password")
	private String password;

	@Override
	public void install(Framework framework) {
		MailConfiguration google = MailConfiguration.google();
		google.setUsername(username);
		google.setPassword(password);
		
		genericBinder.bindClass(MailConfiguration.class).to(google).install(framework);
		
//		genericBinder.bindClass(SecuredStore.class)
//				.to(ServerSecuredStore.class).asSingleton().install(framework);

		genericBinder.bindClass(Setup.class).install(framework);
		genericBinder.bindClass(EntityManager.class)
				.toProvider(PersistencyProvider.class)/* .asSingleton() */
				.install(framework);

		genericBinder.bindClass(String.class).annotatedWith(SecureRandom.class)
				.toProvider(SecureRandomProvider.class)/* .asSingleton() */
				.install(framework);

		Type fieldList = literalBuilder.fromRawType(List.class)
				.withType(Field.class).build();
		Type fieldEntityList = literalBuilder.fromRawType(List.class)
				.withType(FieldEntity.class).build();
		Type fieldType = literalBuilder.fromRawType(TwoWayConverter.class)
				.withType(fieldEntityList, fieldList).build();
		genericBinder.bindType(fieldType).to(FieldConverter.class)
				.install(framework);

		Type providerType = literalBuilder.fromRawType(Converter.class)
				.withType(ProviderEntity.class, Provider.class).build();
		genericBinder.bindType(providerType).to(ProviderConverter.class)
				.install(framework);
		genericBinder.bindType(providerType).annotatedWith(InfoConverter.class)
				.to(ProviderInfoConverter.class).install(framework);

		Type providerFieldList = literalBuilder.fromRawType(List.class)
				.withType(ProviderAuthenticationField.class).build();
		Type providerFieldEntityList = literalBuilder.fromRawType(List.class)
				.withType(ProviderAuthenticationFieldEntity.class).build();
		Type providerFieldType = literalBuilder.fromRawType(Converter.class)
				.withType(providerFieldEntityList, providerFieldList).build();
		genericBinder.bindType(providerFieldType)
				.to(ProviderAuthenticationFieldConverter.class)
				.install(framework);

		Type linkType = literalBuilder.fromRawType(TwoWayConverter.class)
				.withType(LinkEntity.class, Link.class).build();
		genericBinder.bindType(linkType).to(LinkConverter.class)
				.install(framework);

		Type jobType = literalBuilder.fromRawType(TwoWayConverter.class)
				.withType(JobEntity.class, Job.class).build();
		genericBinder.bindType(jobType).to(JobConverter.class)
				.install(framework);

		Type jobSourceType = literalBuilder.fromRawType(TwoWayConverter.class)
				.withType(JobTargetEntity.class, JobSource.class).build();
		genericBinder.bindType(jobSourceType).to(JobSourceConverter.class)
				.install(framework);

		Type jobTargetType = literalBuilder.fromRawType(TwoWayConverter.class)
				.withType(JobTargetEntity.class, JobTarget.class).build();
		genericBinder.bindType(jobTargetType).to(JobTargetConverter.class)
				.install(framework);

		EnumSet<Resources> resources = EnumSet.allOf(Resources.class);
		for (Resources resource : resources) {
			binder.publish(resource.publishable).to(resource.path)
					.install(framework);
		}
	}
}

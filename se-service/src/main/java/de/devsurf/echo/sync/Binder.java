package de.devsurf.echo.sync;

import java.lang.reflect.Type;
import java.util.EnumSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.frameworks.rs.api.InstallableModule;
import de.devsurf.echo.frameworks.rs.api.TwoWayConverter;
import de.devsurf.echo.frameworks.rs.system.api.Framework;
import de.devsurf.echo.frameworks.rs.system.api.GenericBinder;
import de.devsurf.echo.frameworks.rs.system.api.ResourceBinder;
import de.devsurf.echo.frameworks.rs.system.api.TypeLiteralBuilder;
import de.devsurf.echo.sync.jobs.api.Job;
import de.devsurf.echo.sync.jobs.converter.JobConverter;
import de.devsurf.echo.sync.jobs.persistence.JobEntity;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.links.converter.LinkConverter;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.persistence.PersistencyProvider;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.converter.ProviderConverter;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;

public class Binder implements InstallableModule {
	@Inject
	private ResourceBinder binder;

	@Inject
	private GenericBinder genericBinder;

	@Inject
	private TypeLiteralBuilder literalBuilder;

	@Override
	public void install(Framework framework) {
		genericBinder.bindClass(EntityManager.class)
				.toProvider(PersistencyProvider.class)/*.asSingleton()*/
				.install(framework);

		Type providerType = literalBuilder.fromRawType(Converter.class)
				.withType(ProviderEntity.class, Provider.class).build();
		genericBinder.bindType(providerType).to(ProviderConverter.class)
				.install(framework);
		
		Type linkType = literalBuilder.fromRawType(TwoWayConverter.class)
				.withType(LinkEntity.class, Link.class).build();
		genericBinder.bindType(linkType).to(LinkConverter.class)
				.install(framework);
		
		Type jobType = literalBuilder.fromRawType(TwoWayConverter.class)
				.withType(JobEntity.class, Job.class).build();
		genericBinder.bindType(jobType).to(JobConverter.class)
				.install(framework);

		EnumSet<Resources> resources = EnumSet.allOf(Resources.class);
		for (Resources resource : resources) {
			binder.publish(resource.publishable).to(resource.path)
					.install(framework);
		}
	}
}

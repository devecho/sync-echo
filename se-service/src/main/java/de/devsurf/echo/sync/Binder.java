package de.devsurf.echo.sync;

import java.lang.reflect.Type;
import java.util.EnumSet;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.frameworks.rs.api.InstallableModule;
import de.devsurf.echo.frameworks.rs.system.api.Framework;
import de.devsurf.echo.frameworks.rs.system.api.GenericBinder;
import de.devsurf.echo.frameworks.rs.system.api.ResourceBinder;
import de.devsurf.echo.frameworks.rs.system.api.TypeLiteralBuilder;
import de.devsurf.echo.sync.persistence.PersistenceProvider;
import de.devsurf.echo.sync.providers.ProviderConverter;
import de.devsurf.echo.sync.providers.api.Provider;
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
				.toProvider(PersistenceProvider.class)/*.asSingleton()*/
				.install(framework);

		Type type = literalBuilder.fromRawType(Converter.class)
				.withType(ProviderEntity.class, Provider.class).build();
		genericBinder.bindType(type).to(ProviderConverter.class)
				.install(framework);

		EnumSet<Resources> resources = EnumSet.allOf(Resources.class);
		for (Resources resource : resources) {
			binder.publish(resource.publishable).to(resource.path)
					.install(framework);
		}
	}
}

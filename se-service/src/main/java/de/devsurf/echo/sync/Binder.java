package de.devsurf.echo.sync;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.slf4j.Logger;

import com.google.common.base.Stopwatch;

import de.devsurf.common.lang.converter.Converter;
import de.devsurf.common.lang.converter.InfoConverter;
import de.devsurf.common.lang.converter.TwoWayConverter;
import de.devsurf.common.lang.di.InjectLogger;
import de.devsurf.common.lang.di.SecureRandom;
import de.devsurf.common.lang.di.SecureRandomProvider;
import de.devsurf.common.lang.di.TypeLiteralBuilder;
import de.devsurf.common.lang.secret.vault.Vault;
import de.devsurf.echo.frameworks.rs.di.api.Framework;
import de.devsurf.echo.frameworks.rs.di.api.GenericBinder;
import de.devsurf.echo.frameworks.rs.di.api.InstallableModule;
import de.devsurf.echo.frameworks.rs.di.api.ResourceBinder;
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
import de.devsurf.echo.sync.security.internal.AccessTokenFactory;
import de.devsurf.echo.sync.security.vault.internal.VaultService;
import de.devsurf.echo.sync.system.Setup;
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

	@InjectLogger
	private Logger logger;

	@Override
	public void install(Framework framework) {
		Stopwatch stopwatch = Stopwatch.createStarted();

		try {
			genericBinder.bindClass(String.class).named("mac").to(readMac())
					.install(framework);
		} catch (IOException e) {
			logger.error("Fatal failure, while trying to read the mac adress",
					e);
		}

		List<String> values = new ArrayList<String>(Arrays.asList(
				"6CA7A09BE52B85FB", "B47D4D8AD573E168", "E1FB9B104F5317F0",
				"13EAA1AC17B57147"));
		Type valuesType = literalBuilder.fromRawType(List.class)
				.withType(String.class).build();
		genericBinder.bindType(valuesType).named("values").to(values)
				.install(framework);

		genericBinder.bindClass(String.class).named("bootstrap")
				.to(VaultService.DEFAULT_ENCRYPT_ALGORITHM).install(framework);

		genericBinder.bindClass(Vault.class)
				.to(VaultService.class).asSingleton().install(framework);
		
		genericBinder.bindClass(AccessTokenFactory.class).asSingleton().install(framework);
		
		MailConfiguration google = MailConfiguration.google();
		google.setUsername(username);
		google.setPassword(password);
		genericBinder.bindClass(MailConfiguration.class).to(google)
		.install(framework);

		genericBinder.bindClass(Setup.class).install(framework);
		genericBinder.bindClass(EntityManager.class)
				.toProvider(PersistencyProvider.class)
				.install(framework);

		genericBinder.bindClass(String.class).annotatedWith(SecureRandom.class)
				.toProvider(SecureRandomProvider.class)
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

		long seconds = stopwatch.stop().elapsed(TimeUnit.MILLISECONDS);
		logger.info("Binding time took " + seconds + " ms.");
	}

	private String readMac() throws IOException {
		InetAddress address = InetAddress.getLocalHost();
		NetworkInterface network = NetworkInterface.getByInetAddress(address);
		byte[] mac = network.getHardwareAddress();

		if (mac == null || mac.length == 0) {
			logger.error("Fatal failure, MAC Adress couldn't be determined.");
			return "00-00-00-00-00-00";
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i],
					(i < mac.length - 1) ? "-" : ""));
		}

		return sb.toString();
	}
}

package de.devsurf.echo.sync.providers;


import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.errors.ErrorResponse;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderPersistency;
import de.devsurf.echo.sync.providers.api.Provider;


@Path(ResourcePath.PROVIDERS_PATH)
public class ProviderResource extends AbstractEndpoint {
	
	@Inject
	private ProviderPersistency persistence;
	
	@Inject
	private Converter<ProviderEntity, Provider> converter;

	@Override
	public String description() {
		return "Endpoint returns all supported provider.";
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@HEAD
	@Path("{providerId}")
	public Response isAvailable(@PathParam("providerId") String providerId) {
		return Response.ok().build();
	}

	/**
	 * Returns the requested provider, if available.
	 * @throws URISyntaxException 
	 */
	@GET
	@Path("{providerId}")
	public Response find(@PathParam("providerId") Long providerId) throws URISyntaxException {
		ProviderEntity result = persistence.find(providerId);
		if(result == null) {
			return ErrorResponse.item("provider").withId(providerId).wasNotFound();
		}
//		ProviderPojo provider = new ProviderPojo();
//		provider.id = providerId;
//		provider.name = "fileNshare";
//		provider.url = new URI("https://www.filenshare.com");
//		
//		ProviderAuthentication auth = new ProviderAuthentication();
//		auth.mode = "basic";
//		auth.data = new ArrayList<>();
//		
//		ProviderAuthenticationField username = new ProviderAuthenticationField();
//		username.name = "username";
//		username.type = ProviderAuthenticationFieldType.TEXT;
//		auth.data.add(username);
//		
//		ProviderAuthenticationField password = new ProviderAuthenticationField();
//		password.name = "password";
//		password.type = ProviderAuthenticationFieldType.PASSWORD;
//		auth.data.add(password);
//		
//		provider.auth = auth;
		
		return Response.ok(converter.convert(result)).build();
	}

	/**
	 * Returns all supported provider.
	 */
	@Override
	@GET
	@Consumes("*/*")
	@Produces("application/json")
	public Response get() {
		List<ProviderEntity> entities = persistence.findAll();
		List<Provider> providers = new ArrayList<>(entities.size());
		for(ProviderEntity entity : entities) {
			providers.add(converter.convert(entity));
		}
		return Response.ok(new GenericEntity<List<Provider>>(providers){}).build();
	}
}
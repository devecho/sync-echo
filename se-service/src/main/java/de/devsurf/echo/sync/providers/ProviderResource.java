package de.devsurf.echo.sync.providers;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.errors.ErrorResponse;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderPersistency;


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
		long id;
		try {
			id = Long.parseLong(providerId);
		} catch (NumberFormatException e) {
			return ErrorResponse.item("provider").withId(providerId).wasNotFound();
		}
		
		ProviderEntity result = persistence.find(id);
		if(result == null) {
			return ErrorResponse.item("provider").withId(providerId).wasNotFound();
		}
		
		return Response.ok().build();
	}

	/**
	 * Returns the requested provider, if available. 
	 */
	@GET
	@Path("{providerId}")
	public Response find(@PathParam("providerId") String providerId) {
		long id;
		try {
			id = Long.parseLong(providerId);
		} catch (NumberFormatException e) {
			return ErrorResponse.item("provider").withId(providerId).wasNotFound();
		}
		ProviderEntity result = persistence.find(id);
		if(result == null) {
			return ErrorResponse.item("provider").withId(providerId).wasNotFound();
		}
		
		return Response.ok(converter.convertTo(result)).build();
	}

	/**
	 * Returns all supported provider.
	 */
	@Override
	@GET
	@Consumes("*/*")
	public Response get() {
		List<ProviderEntity> entities = persistence.findAll();
		List<Provider> providers = new ArrayList<>(entities.size());
		for(ProviderEntity entity : entities) {
			providers.add(converter.convertTo(entity));
		}
		return Response.ok(new GenericEntity<List<Provider>>(providers){}).build();
	}
}
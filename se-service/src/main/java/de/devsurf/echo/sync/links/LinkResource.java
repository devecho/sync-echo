package de.devsurf.echo.sync.links;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.devsurf.echo.frameworks.rs.api.Converter;
import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.errors.ErrorResponse;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.links.persistence.LinksPersistency;
import de.devsurf.echo.sync.links.transport.LinkPojo;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;

@Path(ResourcePath.LINKS_PATH)
public class LinkResource extends AbstractEndpoint {
	
	@Inject
	private LinksPersistency persistence;
	
	@Inject
	private Converter<LinkEntity, Link> converter;
	
	@Override
	public String description() {
		return "Endpoint returns the information about the authenticated user.";
	}

	@Override
	@GET
	@Consumes("*/*")
	@Produces("application/json")
	public Response get() {
		List<LinkEntity> entities = persistence.findAll();
		List<Link> links = new ArrayList<>(entities.size());
		for(LinkEntity entity : entities) {
			links.add(converter.convert(entity));
		}
		return Response.ok(new GenericEntity<List<Link>>(links){}).build();
	}

	/**
	 * Returns the requested link is available for the user.
	 */
	@HEAD
	@Path("{linkId}")
	public Response isAvailable(@PathParam("linkId") String linkId) {
		long id;
		try {
			id = Long.parseLong(linkId);
		} catch (NumberFormatException e) {
			return ErrorResponse.item("links").withId(linkId).wasNotFound();
		}
		
		LinkEntity result = persistence.find(id);
		if(result == null) {
			return ErrorResponse.item("links").withId(linkId).wasNotFound();
		}
		
		return Response.ok().build();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@GET
	@Path("{linkId}")
	public Response find(@PathParam("linkId") String linkId) {
		long id;
		try {
			id = Long.parseLong(linkId);
		} catch (NumberFormatException e) {
			return ErrorResponse.item("links").withId(linkId).wasNotFound();
		}
		
		LinkEntity result = persistence.find(id);
		if(result == null) {
			return ErrorResponse.item("links").withId(linkId).wasNotFound();
		}
		
		return Response.ok(converter.convert(result)).build();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Link link) {
		return Response.ok().build();// TODO change to 201
	}
	
	@PUT
	@Path("{linkId}")
	public Response update(@PathParam("linkId") String linkId) {
		return Response.ok(new LinkPojo()).build();
	}
}
package de.devsurf.echo.sync.links;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.frameworks.rs.api.TwoWayConverter;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.api.Field;
import de.devsurf.echo.sync.errors.ErrorResponse;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.links.persistence.LinksPersistency;
import de.devsurf.echo.sync.persistence.FieldEntity;
import de.devsurf.echo.sync.persistence.ItemAlreadyExistsException;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderEntity;
import de.devsurf.echo.sync.providers.persistence.ProviderPersistency;

@Path(ResourcePath.LINKS_PATH)
public class LinkResource extends AbstractEndpoint {

	@Inject
	private LinksPersistency persistence;

	@Inject
	private ProviderPersistency retrieval;

	@Inject
	private TwoWayConverter<LinkEntity, Link> converter;

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
		for (LinkEntity entity : entities) {
			links.add(converter.convertTo(entity));
		}
		return Response.ok(new GenericEntity<List<Link>>(links) {
		}).build();
	}

	/**
	 * Returns the requested link is available for the user.
	 */
	@HEAD
	@Path("{linkId}")
	public Response isAvailable(@PathParam("linkId") String linkId) {
		findLink(linkId);
		return Response.ok().build();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@GET
	@Path("{linkId}")
	public Response find(@PathParam("linkId") String linkId) {
		LinkEntity result = findLink(linkId);
		return Response.ok(converter.convertTo(result)).build();
	}

	/**
	 * Returns the requested provider, if available.
	 * 
	 * @throws ItemAlreadyExistsException
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response create(Link link) throws ItemAlreadyExistsException {
		validateLink(link);

		LinkEntity target = converter.convertFrom(link);
		long id = persistence.persist(target).getId();

		return Response
				.created(
						UriBuilder.fromResource(getClass())
								.path(Long.toString(id)).build())
				.entity(converter.convertTo(target)).build();
	}

	@PUT
	@Path("{linkId}")
	public Response update(@PathParam("linkId") String linkId, Link link)
			throws ItemAlreadyExistsException {
		LinkEntity result = findLink(linkId);

		validateLink(link);

		LinkEntity target = converter.convertFrom(link);
		if (target.getId() != result.getId()) {
			throw new BadRequestException(ErrorResponse.item("id")
					.withId(target.getId()).forStatus(Status.BAD_REQUEST));
		}
		persistence.merge(target);

		return Response.ok(converter.convertTo(target)).build();
	}

	@DELETE
	@Path("{linkId}")
	public Response delete(@PathParam("linkId") String linkId) {
		LinkEntity result = findLink(linkId);
		persistence.delete(result);
		return Response.ok().build();
	}

	private LinkEntity findLink(String linkId) {
		long id;
		try {
			id = Long.parseLong(linkId);
		} catch (NumberFormatException e) {
			throw new WebApplicationException(ErrorResponse.item("links").withId(linkId).wasNotFound());
		}

		LinkEntity result = persistence.find(id);
		if (result == null) {
			throw new WebApplicationException(ErrorResponse.item("links").withId(linkId).wasNotFound());
		}
		return result;
	}

	private void validateLink(Link link) {
		Provider provider = link.getProvider();
		long providerId = provider.getId();
		ProviderEntity providerEntity = retrieval.get(providerId);
		if (providerEntity == null) {
			throw new BadRequestException(ErrorResponse.item("provider")
					.withId(providerId).forStatus(Status.BAD_REQUEST));
		}

		List<Field> linkFields = link.getData();
		ProviderAuthenticationEntity providerAuthentication = providerEntity
				.getAuthentication();
		List<ProviderAuthenticationFieldEntity> providerFields = providerAuthentication
				.getFields();

		for (ProviderAuthenticationFieldEntity providerField : providerFields) {
			if(providerField.isOptional()) {
				continue;
			}
			boolean found = false;
			for (Field linkField : linkFields) {
				if (providerField.getName().equalsIgnoreCase(
						linkField.getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				throw new BadRequestException(ErrorResponse.item("field")
						.withId(providerField.getName())
						.forStatus(Status.BAD_REQUEST));
			}
		}
		//TODO double checking should be changed
		for (Field linkField : linkFields) {
			boolean found = false;
			for (FieldEntity providerField : providerFields) {
				if (providerField.getName().equalsIgnoreCase(
						linkField.getName())) {
					found = true;
					break;
				}
			}
			if (!found) {
				throw new BadRequestException(ErrorResponse.item("field")
						.withId(linkField.getName())
						.forStatus(Status.BAD_REQUEST));
			}
		}
	}
}
package de.devsurf.echo.sync.users;


import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.errors.ErrorResponse;
import de.devsurf.echo.sync.system.transport.Registration;
import de.devsurf.echo.sync.users.persistence.UserPersistency;


@Path(ResourcePath.USERS_PATH)
public class UserResource extends AbstractEndpoint {
	public static final String CURRENT_ID = "current";
	
	@Inject
	private UserPersistency users;

	@Override
	public String description() {
		return "Endpoint returns the information about the authenticated user.";
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@HEAD
	@Path("{userId}")
	public Response isAvailable(@PathParam("userId") String userId) {
		if (!CURRENT_ID.equalsIgnoreCase(userId)) {
			return ErrorResponse.item("user").withId(userId).wasNotFound();
		}
		return Response.ok().build();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@GET
	@Path("{userId}")
	public Response find(@PathParam("userId") String userId) {
		if (!CURRENT_ID.equalsIgnoreCase(userId)) {
			return ErrorResponse.item("user").withId(userId).wasNotFound();
		}
		User user = new User();
		user.id = "1";
		user.name = "Daniel Manzke";
		user.email = "daniel.manzke@googlemail.com";
		user.since = new Date(0);
		return Response.ok(user).build();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{userId}/password")
	public Response changePassword(@PathParam("userId") String userId,
			ChangePasswordRequest request) {
		if (!CURRENT_ID.equalsIgnoreCase(userId)) {
			return ErrorResponse.item("user").withId(userId).wasNotFound();
		}
		return Response.ok().build();
	}
}
package de.devsurf.echo.sync.users;


import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonValue;


import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;
import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.sync.errors.ErrorResponse;


@Path("users")
public class UserResource extends AbstractEndpoint {
	public static final String CURRENT_ID = "current";

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
	
	@JsonPropertyOrder({ "type", "id", "name", "email" })
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class User implements Typed {
		private String id;
		private String name;
		private String email;
		private Date since;
		
		public static final Type TYPE = new Type() {
			@Override
			@JsonValue
			public String value() {
				return "user";
			}
		};
		
		@Override
		@JsonProperty("type")
		public Type type() {
			return TYPE;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Date getSince() {
			return since;
		}

		public void setSince(Date since) {
			this.since = since;
		}
	}

	@JsonPropertyOrder({ "type", "old", "new", "hash" })
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ChangePasswordRequest implements Typed {
		private String oldPassword;
		private String newPassword;
		private String hash;

		public static final Type TYPE = new Type() {
			@Override
			@JsonValue
			public String value() {
				return "password";
			}
		};

		public ChangePasswordRequest() {
		}

		@Override
		@JsonProperty("type")
		public Type type() {
			return TYPE;
		}

		@JsonProperty("old")
		public String getOldPassword() {
			return oldPassword;
		}

		public void setOldPassword(String old) {
			this.oldPassword = old;
		}

		@JsonProperty("new")
		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		public String getHash() {
			return hash;
		}

		@JsonProperty("hash")
		public void setHash(String hash) {
			this.hash = hash;
		}
	}
}
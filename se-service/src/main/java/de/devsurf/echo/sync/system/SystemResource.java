package de.devsurf.echo.sync.system;


import java.net.URI;
import java.util.Date;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;
import de.devsurf.echo.sync.Resources.ResourcePath;


@Path(ResourcePath.SYSTEM_PATH)
public class SystemResource extends AbstractEndpoint {
	@Inject
	private Setup setup;
	
	@Override
	public String description() {
		return "Endpoint returns the information about the system and its status.";
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("setup")
	public Response setup() throws Exception {
		return Response.ok(setup.doIt()).build();
	}
	
	@HEAD
	@Path("status")
	public Response available() {
		Status status = new Status();
		status.id = Long.toString(System.nanoTime());
		status.title = "actual system status";
		status.message = "running";

		return Response.ok(status).build();
	}

	@GET
	@Path("status")
	public Response status() {
		Status status = new Status();
		status.id = Long.toString(System.nanoTime());
		status.title = "actual system status";
		status.message = "running";

		return Response.ok(status).build();
	}

	@POST
	@Path("registration")
	public Response register(RegistrationRequest request) {
		return Response.ok().build();
	}

	@JsonPropertyOrder({ "type", "id", "title", "message", "level", "url",
			"created" })
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonSerialize(include = Inclusion.NON_NULL)
	public static class Status implements Typed {
		private String id;
		private String title;
		private String message;
		private String level;
		private URI url;
		private Date created;

		public static final Type TYPE = new Type() {
			@Override
			public String value() {
				return "status";
			}
		};

		@Override
		public Type type() {
			return TYPE;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public String getLevel() {
			return level;
		}

		public void setLevel(String level) {
			this.level = level;
		}

		public URI getUrl() {
			return url;
		}

		public void setUrl(URI url) {
			this.url = url;
		}

		public Date getCreated() {
			return created;
		}

		public void setCreated(Date created) {
			this.created = created;
		}
	}

	@JsonPropertyOrder({ "type", "id", "name", "email", "referrer", "created"})
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonSerialize(include = Inclusion.NON_NULL)
	public static class RegistrationRequest implements Typed {
		private String id;
		private String name;
		private String email;
		private String referrer;
		private Date created;

		public static final Type TYPE = new Type() {
			@Override
			public String value() {
				return "registration";
			}
		};

		@Override
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

		public String getReferrer() {
			return referrer;
		}

		public void setReferrer(String referrer) {
			this.referrer = referrer;
		}

		public Date getCreated() {
			return created;
		}

		public void setCreated(Date created) {
			this.created = created;
		}
	}
}
package de.devsurf.echo.sync.links;


import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonValue;

import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.providers.api.ProviderAuthenticationField;


@Path(ResourcePath.LINKS_PATH)
public class LinkResource extends AbstractEndpoint {
	@Override
	public String description() {
		return "Endpoint returns the information about the authenticated user.";
	}
	
	@Override
	@GET
	@Consumes("*/*")
	@Produces("application/json")
	public Response get() {
		return Response.ok(new Link()).build();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@HEAD
	@Path("{linkId}")
	public Response isAvailable(@PathParam("linkId") String linkId) {
		return Response.ok().build();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@GET
	@Path("{linkId}")
	public Response find(@PathParam("linkId") String linkId) {
		return Response.ok(new Link()).build();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response link(Link link) {
		return Response.ok().build();//TODO change to 201
	}
	
	@JsonPropertyOrder({ "type", "id", "provider", "data" })
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Link implements Typed {
		private String id;
		private String provider;
		private List<ProviderAuthenticationField> data;
		
		public static final Type TYPE = new Type() {
			@Override
			@JsonValue
			public String value() {
				return "link";
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

		public String getProvider() {
			return provider;
		}

		public void setProvider(String provider) {
			this.provider = provider;
		}

		public List<ProviderAuthenticationField> getData() {
			return data;
		}

		public void setData(List<ProviderAuthenticationField> data) {
			this.data = data;
		}
	}
}
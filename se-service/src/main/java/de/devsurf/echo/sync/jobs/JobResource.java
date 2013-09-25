package de.devsurf.echo.sync.jobs;


import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;

import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.errors.ErrorResponse;


@Path(ResourcePath.JOBS_PATH)
public class JobResource extends AbstractEndpoint {
	
	@Override
	public String description() {
		return "Endpoint returns the information about the authenticated user.";
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@HEAD
	@Path("{jobId}")
	public Response isAvailable(@PathParam("jobId") String jobId) {
		return ErrorResponse.item("job").withId(jobId).wasNotFound();
	}

	/**
	 * Returns the requested provider, if available.
	 */
	@GET
	@Path("{jobId}")
	public Response find(@PathParam("jobId") String jobId) {
		return ErrorResponse.item("job").withId(jobId).wasNotFound();
	}

	
	@JsonPropertyOrder({ "type", "id", "name", "description", "source", "target" })
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class Job implements Typed {
		private String id;
		private String name;
		private String description;
		private String source;
		private String target;
		
		public static final Type TYPE = new Type() {
			@Override
			public String value() {
				return "job";
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

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getTarget() {
			return target;
		}

		public void setTarget(String target) {
			this.target = target;
		}
	}
}
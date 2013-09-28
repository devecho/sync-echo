package de.devsurf.echo.sync.jobs;


import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;


import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.frameworks.rs.api.TwoWayConverter;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.errors.ErrorResponse;
import de.devsurf.echo.sync.links.api.Link;
import de.devsurf.echo.sync.links.persistence.LinkEntity;
import de.devsurf.echo.sync.links.persistence.LinksPersistency;
import de.devsurf.echo.sync.providers.persistence.ProviderPersistency;


@Path(ResourcePath.JOBS_PATH)
public class JobResource extends AbstractEndpoint {

	@Inject
	private LinksPersistency retrieval;

	@Inject
	private TwoWayConverter<LinkEntity, Link> converter;

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
	
	@Override
	@GET
	@Consumes("*/*")
	@Produces("application/json")
	public Response get() {
		// TODO Auto-generated method stub
		return super.get();
	}
}
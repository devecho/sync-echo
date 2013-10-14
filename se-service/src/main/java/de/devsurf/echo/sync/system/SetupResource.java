package de.devsurf.echo.sync.system;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import de.devsurf.common.lang.di.InjectLogger;
import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.sync.Resources.ResourcePath;

@Path(ResourcePath.SETUP_PATH)
public class SetupResource extends AbstractEndpoint {
	@Inject
	private Setup setup;

	@InjectLogger
	private Logger logger;

	@Override
	public String description() {
		return "Endpoint returns the information about the system and its status.";
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response setup() throws Exception {
		return Response.ok(setup.doIt()).build();
	}
}
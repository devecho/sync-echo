package de.devsurf.echo.sync.system;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import de.devsurf.common.lang.di.InjectLogger;
import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.system.transport.Status;

@Path(ResourcePath.SYSTEM_PATH)
public class SystemResource extends AbstractEndpoint {
	@Inject
	private Setup setup;

	@InjectLogger
	private Logger logger;

	@Override
	public String description() {
		return "Endpoint returns the information about the system and its status.";
	}

	@HEAD
	@Path("status")
	public Response available() {
		return status();
	}

	@GET
	@Path("status")
	public Response status() {
		Status status = new Status();
		status.setId(Long.toString(System.nanoTime()));
		status.setTitle("actual system status");
		status.setMessage("running");

		return Response.ok(status).build();
	}
}
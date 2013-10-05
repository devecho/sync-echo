package de.devsurf.echo.sync.system;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import de.devsurf.common.lang.formatter.ExceptionMessage;
import de.devsurf.common.lang.formatter.ToStringMessage;
import de.devsurf.echo.frameworks.rs.api.Log;
import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.system.transport.Registration;
import de.devsurf.echo.sync.system.transport.Status;
import de.devsurf.echo.sync.users.persistence.UserEntity;
import de.devsurf.echo.sync.users.persistence.UserPersistency;
import de.devsurf.echo.sync.utils.Mailing;
import de.devsurf.echo.sync.utils.Mailing.MailConfigurationException;

@Path(ResourcePath.SYSTEM_PATH)
public class SystemResource extends AbstractEndpoint {
	@Inject
	private Setup setup;

	@Inject
	private UserPersistency users;
	
	@Inject
	private Mailing mailing;

	@Log
	private Logger logger;

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

	@POST
	@Path("registration")
	public Response register(Registration request) throws UnsupportedEncodingException, MessagingException, MailConfigurationException {
		String email = request.getEmail();
		if(email == null || email.length() == 0) {
			throw new BadRequestException(ExceptionMessage.format("No email was specified.").build());
		}
		
//		UserEntity user = users.find(email);
//		if (user != null) {
//			logger.error(ExceptionMessage.format("User already exists.")
//					.addParameter("user", email).build());
//			throw new BadRequestException();
//		}
//		
//		long referrer = request.getReferrer();
//		if(referrer != 0) {
//			UserEntity referrerEntity = users.get(referrer);
//			if (referrerEntity == null) {
//				logger.error(ExceptionMessage.format("Referrer was not found.")
//						.addParameter("referrer", referrer).build());
//				throw new BadRequestException();
//			}
//		}
		
		mailing.sendMail("daniel.manzke@googlemail.com", "daniel.manzke@googlemail.com", email, "service test mail",
				"my mail");
		System.out.println("sent");
		
		// create registration token (similar to the access token)
		// send mail
		return Response.ok().build();
	}
}
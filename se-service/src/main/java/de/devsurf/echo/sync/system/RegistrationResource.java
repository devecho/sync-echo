package de.devsurf.echo.sync.system;

import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;

import com.google.common.collect.Maps;

import de.devsurf.common.lang.di.InjectLogger;
import de.devsurf.common.lang.formatter.ExceptionMessage;
import de.devsurf.common.lang.formatter.TemplateMessage;
import de.devsurf.common.lang.secret.vault.Vault;
import de.devsurf.echo.frameworks.rs.api.Publishable.AbstractEndpoint;
import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.security.internal.AccessTokenFactory;
import de.devsurf.echo.sync.system.transport.Otp;
import de.devsurf.echo.sync.system.transport.Registration;
import de.devsurf.echo.sync.users.persistence.UserPersistency;
import de.devsurf.echo.sync.utils.Mailing;
import de.devsurf.echo.sync.utils.Templates;
import de.devsurf.echo.sync.utils.Templates.DefaultTemplateType;

@Path(ResourcePath.REGISTRATION_PATH)
public class RegistrationResource extends AbstractEndpoint {
	@Inject
	private UserPersistency users;

	@Inject
	private Mailing mailing;

	@Inject
	private Vault security;

	@Inject
	private AccessTokenFactory tokenFactory;

	@InjectLogger
	private Logger logger;

	@Override
	public String description() {
		return "Endpoint returns the information about the system and its status.";
	}

	@GET
	public Response startRegistration() {
		String otp = tokenFactory.generate(System.currentTimeMillis()).now();
		return Response.ok(new Otp(otp)).build();
	}

	@POST
	public Response submitRegistration(Registration request) throws Exception {
		String email = request.getEmail();
		if (email == null || email.length() == 0) {
			throw new BadRequestException(ExceptionMessage.format(
					"No email was specified.").build());
		}

		String otp = request.getOtp();
		if(otp == null || otp.length() == 0) {
			throw new BadRequestException(ExceptionMessage.format(
					"No otp was specified.").build());
		}
		boolean valid = tokenFactory.generate(System.currentTimeMillis())
				.verify(otp);
		if (!valid) {
			throw new BadRequestException(ExceptionMessage.format(
					"OTP is not valid.").build());
		}

		// validate otp

		// UserEntity user = users.find(email);
		// if (user != null) {
		// logger.error(ExceptionMessage.format("User already exists.")
		// .addParameter("user", email).build());
		// throw new BadRequestException();
		// }
		//
		// long referrer = request.getReferrer();
		// if (referrer != 0) {
		// UserEntity referrerEntity = users.get(referrer);
		// if (referrerEntity == null) {
		// logger.error(ExceptionMessage.format("Referrer was not found.")
		// .addParameter("referrer", referrer).build());
		// throw new BadRequestException();
		// }
		// }

		String message = Templates.load("registration",
				DefaultTemplateType.EMAIL);
		Map<String, String> parameters = Maps.newHashMap();
		parameters.put("link", request.getUrl().toString());
		parameters.put("email", request.getEmail());
		parameters.put("token", "12345");
		message = TemplateMessage.format(message, parameters);

		mailing.sendMail("daniel.manzke@googlemail.com",
				"daniel.manzke@googlemail.com", email,
				"Create your account now.", message);

		/*
		 * get on registration sends an otp creates a registration request which
		 * must be approved send an approval mail that the registration was
		 * submitted the registration link contains the mail and the request
		 * token user has to specify name and password
		 */

		// create registration token (similar to the access token)
		// send mail
		return Response.ok().build();
	}
}
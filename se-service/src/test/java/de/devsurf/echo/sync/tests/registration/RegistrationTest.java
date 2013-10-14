package de.devsurf.echo.sync.tests.registration;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.testng.annotations.Test;

import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.system.transport.Otp;
import de.devsurf.echo.sync.tests.GuicyfiedTestBase;

public class RegistrationTest extends GuicyfiedTestBase {
	
	@Test
	public void GetOtpToken() {
		WebTarget target = target();
//		String answer = target.request().get(String.class);
//		System.out.println(answer);
		
		Otp otp = target.path(ResourcePath.REGISTRATION_PATH).request(MediaType.APPLICATION_JSON_TYPE).get(Otp.class);
		System.out.println(otp);
	}
}

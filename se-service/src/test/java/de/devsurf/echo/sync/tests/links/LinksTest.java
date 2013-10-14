package de.devsurf.echo.sync.tests.links;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.testng.annotations.Test;

import de.devsurf.echo.sync.Resources.ResourcePath;
import de.devsurf.echo.sync.tests.GuicyfiedTestBase;

public class LinksTest extends GuicyfiedTestBase {
	
	@Test
	public void GetAllLinksTest() {
		WebTarget target = target();
//		String answer = target.request().get(String.class);
//		System.out.println(answer);
		
		Response answer = target.path(ResourcePath.LINKS_PATH).request(MediaType.APPLICATION_JSON_TYPE).get();
		System.out.println(answer);
	}
	
	@Test
	public void GetAllEmptyLinksTest() {

	}
	
	@Test
	public void CreateLinkTest() {

	}
	
	@Test
	public void UpdateLinkTest() {

	}

	@Test
	public void CreateLinkWithMissingPropertyTest() {

	}
	
	@Test
	public void UpdateLinkWithMissingPropertyTest() {

	}
	
	@Test
	public void CreateLinkWitNonExistingProviderTest() {

	}
	
	@Test
	public void UpdateLinkWitNonExistingProviderTest() {

	}
	
	@Test
	public void UpdateNonExistingLinkTest() {

	}

	@Test
	public void GetNonExistingLinkTest() {

	}

	@Test
	public void GetIllegalIdLinkTest() {

	}
}

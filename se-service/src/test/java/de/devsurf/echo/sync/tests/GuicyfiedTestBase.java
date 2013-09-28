package de.devsurf.echo.sync.tests;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import de.devsurf.echo.frameworks.rs.service.startup.guice.GuicyfiedApplication;

public abstract class GuicyfiedTestBase extends JerseyTest {
	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		return ResourceConfig.forApplicationClass(GuicyfiedApplication.class);
	}
	
	@Override
	protected void configureClient(ClientConfig config) {
		config.register(new JacksonFeature());
	}

	@Override
	public WebTarget target() {
		return super.target().path("api").path("1");
	}
	
	@Override
	@BeforeSuite
	public void setUp() throws Exception {
		super.setUp();
		String message = target().path("system").path("setup").request().get(String.class);
		if(!"done".equalsIgnoreCase(message)) {
			throw new Exception("System couldn't be setup.");
		}
	}
	
	@Override
	@AfterSuite
	public void tearDown() throws Exception {
		super.tearDown();
	}
}

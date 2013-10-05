package de.devsurf.echo.sync.tests.providers;

import java.net.URI;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;
import org.testng.Assert;
import org.testng.annotations.Test;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.sync.providers.api.Provider;
import de.devsurf.echo.sync.providers.api.ProviderAuthentication;
import de.devsurf.echo.sync.tests.GuicyfiedTestBase;

public class ProvidersTest extends GuicyfiedTestBase {
	@Test
	public void GetAllProviderTest() {
		List<ProviderPojo> providers = target().path("providers").request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<List<ProviderPojo>>() {
		});
		Assert.assertEquals(providers.size(), 1);
		
		Provider provider = providers.get(0);
		Assert.assertEquals(provider.getId(), 1);
		Assert.assertEquals(provider.getName(), "fileNshare");
		Assert.assertEquals(provider.getWebsite(), URI.create("https://www.filenshare.com"));
	}

	@Test
	public void GetOneProviderTest() {
		Provider provider = target().path("providers").path("1").request(MediaType.APPLICATION_JSON_TYPE).get(ProviderPojo.class);
		Assert.assertEquals(provider.getId(), 1);
		Assert.assertEquals(provider.getName(), "fileNshare");
		Assert.assertEquals(provider.getWebsite(), URI.create("https://www.filenshare.com"));
	}

	@Test
	public void GetNonExistingProviderTest() {
		Response response = target().path("providers").path("2").request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
		Assert.assertEquals(response.getStatus(), 404);
	}

	@Test
	public void GetIllegalIdProviderTest() {
		Response response = target().path("providers").path("abc").request(MediaType.APPLICATION_JSON_TYPE).get(Response.class);
		Assert.assertEquals(response.getStatus(), 404);
	}
	
	public static class TypePojo implements Type {
		private String type;
		
		public TypePojo(String type) {
			this.type = type;
		}
		
		@Override
		@JsonValue
		public String value() {
			return type;
		}
	}
	
	public static class ProviderPojo implements Provider {
		private long id;
		private String name;
		private URI website;
		private String version;

		@Override
		public long getId() {
			return id;
		}
		
		public void setId(long id) {
			this.id = id;
		}

		@Override
		public String getName() {
			return name;
		}
		
		public void setName(String name) {
			this.name = name;
		}

		@Override
		@JsonProperty("url")
		public URI getWebsite() {
			return website;
		}
		
		public void setWebsite(URI website) {
			this.website = website;
		}

		@Override
		public String getVersion() {
			return version;
		}
		
		public void setVersion(String version) {
			this.version = version;
		}

		@Override
		@JsonIgnore
		public ProviderAuthentication getAuth() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}

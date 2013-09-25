package de.devsurf.echo.sync;


import de.devsurf.echo.frameworks.rs.api.Publishable;
import de.devsurf.echo.sync.jobs.JobResource;
import de.devsurf.echo.sync.links.LinkResource;
import de.devsurf.echo.sync.providers.ProviderResource;
import de.devsurf.echo.sync.system.SystemResource;
import de.devsurf.echo.sync.users.UserResource;


public enum Resources {
	SYSTEM(ResourcePath.SYSTEM_PATH, SystemResource.class),
	USERS(ResourcePath.USERS_PATH, UserResource.class),
	JOBS(ResourcePath.JOBS_PATH, JobResource.class),
	PROVIDERS(ResourcePath.PROVIDERS_PATH, ProviderResource.class),
	LINKS(ResourcePath.LINKS_PATH, LinkResource.class);
	
	public final String path;
	public final Class<? extends Publishable> publishable;
	
	private Resources(String path, Class<? extends Publishable> publishable) {
		this.path = path;
		this.publishable = publishable;
	}
	
	public String path() {
		return path;
	}
	
	public static class ResourcePath {
		public static final String SYSTEM_PATH = "system";
		public static final String USERS_PATH = "users";
		public static final String JOBS_PATH = "jobs";
		public static final String PROVIDERS_PATH = "providers";
		public static final String LINKS_PATH = "links";
	}
}
package de.devsurf.echo.sync.providers.persistence;

import java.net.URI;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "providers")
@NamedQueries(@NamedQuery(name = "providers.findAll", query = "SELECT p FROM ProviderEntity p"))
public class ProviderEntity {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "version", nullable = false, length = 25)
	private String version;

	@Column(name = "website", nullable = true, length = 256)
	private URI website;
	
	@Embedded
	private ProviderAuthenticationEntity authentication;

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public URI getWebsite() {
		return website;
	}

	public void setWebsite(URI url) {
		this.website = url;
	}

	public ProviderAuthenticationEntity getAuthentication() {
		return authentication;
	}

	public void setAuthentication(ProviderAuthenticationEntity authentication) {
		this.authentication = authentication;
	}
}

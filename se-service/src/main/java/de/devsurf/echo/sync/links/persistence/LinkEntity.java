package de.devsurf.echo.sync.links.persistence;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import de.devsurf.echo.sync.providers.persistence.ProviderAuthenticationFieldEntity;

@Entity
@Table(name = "links")
@NamedQueries(@NamedQuery(name = "findAll", query = "SELECT p FROM LinkEntity p"))
public class LinkEntity {
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	private long id;
	
	@Column(name = "user", nullable = false)
	private long user;

	@Column(name = "provider", nullable = false)
	private long provider;
	
	@ElementCollection
	@CollectionTable(name = "link_fields", joinColumns = @JoinColumn(name = "link_field_id"))
	@Column(name = "link_field")
	private List<ProviderAuthenticationFieldEntity> fields;

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getUser() {
		return user;
	}

	public void setUser(long user) {
		this.user = user;
	}

	public long getProvider() {
		return provider;
	}

	public void setProvider(long provider) {
		this.provider = provider;
	}

	public List<ProviderAuthenticationFieldEntity> getFields() {
		return fields;
	}

	public void setFields(List<ProviderAuthenticationFieldEntity> fields) {
		this.fields = fields;
	}
}

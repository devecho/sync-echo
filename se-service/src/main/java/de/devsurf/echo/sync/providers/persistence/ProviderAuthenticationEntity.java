package de.devsurf.echo.sync.providers.persistence;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;

import com.google.common.collect.Lists;

@Embeddable
public class ProviderAuthenticationEntity {
	@Column(name = "auth_type", nullable = false, length = 50)
	private String type;

	@ElementCollection
	@CollectionTable(name = "auth_types_fields", joinColumns = @JoinColumn(name = "auth_type_id"))
	@Column(name = "auth_type_field")
	private List<ProviderAuthenticationFieldEntity> fields;

	public ProviderAuthenticationEntity() {
		this("unknown");
	}

	public ProviderAuthenticationEntity(String type) {
		this.type = type;
		fields = Lists.newArrayListWithExpectedSize(3);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public List<ProviderAuthenticationFieldEntity> getFields() {
		return fields;
	}

	public void setFields(List<ProviderAuthenticationFieldEntity> fields) {
		this.fields = fields;
	}
}
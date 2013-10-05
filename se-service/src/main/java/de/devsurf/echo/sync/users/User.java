package de.devsurf.echo.sync.users;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonValue;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;

@JsonPropertyOrder({ "type", "id", "name", "email" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Typed {
	String id;
	String name;
	String email;
	Date since;
	
	public static final Type TYPE = new Type() {
		@Override
		@JsonValue
		public String value() {
			return "user";
		}
	};
	
	@Override
	@JsonProperty("type")
	public Type type() {
		return TYPE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getSince() {
		return since;
	}

	public void setSince(Date since) {
		this.since = since;
	}
}
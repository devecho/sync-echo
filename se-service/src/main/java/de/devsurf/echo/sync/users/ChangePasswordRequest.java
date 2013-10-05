package de.devsurf.echo.sync.users;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonValue;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;

@JsonPropertyOrder({ "type", "old", "new", "hash" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChangePasswordRequest implements Typed {
	private String oldPassword;
	private String newPassword;
	private String hash;

	public static final Type TYPE = new Type() {
		@Override
		@JsonValue
		public String value() {
			return "password";
		}
	};

	public ChangePasswordRequest() {
	}

	@Override
	@JsonProperty("type")
	public Type type() {
		return TYPE;
	}

	@JsonProperty("old")
	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String old) {
		this.oldPassword = old;
	}

	@JsonProperty("new")
	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getHash() {
		return hash;
	}

	@JsonProperty("hash")
	public void setHash(String hash) {
		this.hash = hash;
	}
}
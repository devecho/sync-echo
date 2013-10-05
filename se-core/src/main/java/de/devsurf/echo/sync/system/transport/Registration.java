package de.devsurf.echo.sync.system.transport;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;

@JsonPropertyOrder({ "type", "email", "referrer" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = Inclusion.NON_NULL)
public class Registration implements Typed {
	private String email;
	private long referrer;

	public static final Type TYPE = new Type() {
		@Override
		public String value() {
			return "registration";
		}
	};

	@Override
	public Type type() {
		return TYPE;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public long getReferrer() {
		return referrer;
	}

	public void setReferrer(long referrer) {
		this.referrer = referrer;
	}
}
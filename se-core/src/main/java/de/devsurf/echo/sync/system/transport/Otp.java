package de.devsurf.echo.sync.system.transport;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonPropertyOrder({ "type", "token" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeName("otp")
public class Otp {
	private String token;
	
	public Otp(String otp) {
		this.token = otp;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String otp) {
		this.token = otp;
	}
}
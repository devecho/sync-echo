package de.devsurf.echo.sync.security.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class StringAccessToken implements AccessToken<String> {
	private static final long serialVersionUID = 4211183502724690864L;
	private String token;
	private Date expires;
	private Collection<String> options;
	
	public StringAccessToken(String token) {
		this.token = token;
		this.expires = new Date(0);
	}
	
	public StringAccessToken(String token, Date expires){
		this.token = token;
		this.expires = expires;
	}
	
	@Override
	public Date expires() {
		return expires;
	}
	
	public String token(){
		return token;
	}
	
	@Override
	public Collection<String> options() {
		if(options == null) {
			options = new ArrayList<String>();
		}
		return options;
	}
	
	public StringAccessToken addOption(String option) {
		if(options == null) {
			options = new ArrayList<String>();
		}
		options.add(option);
		return this;
	}
	
	public StringAccessToken setOptions(Collection<String> options) {
		this.options = options; 
		return this;
	} 
	
	@Override
	public String toString() {
		return token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((expires == null) ? 0 : expires.hashCode());
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result + ((token == null) ? 0 : token.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringAccessToken other = (StringAccessToken) obj;
		if (expires == null) {
			if (other.expires != null)
				return false;
		} else if (!expires.equals(other.expires))
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (token == null) {
			if (other.token != null)
				return false;
		} else if (!token.equals(other.token))
			return false;
		return true;
	}
}
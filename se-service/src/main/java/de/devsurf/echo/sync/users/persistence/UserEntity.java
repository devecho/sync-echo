package de.devsurf.echo.sync.users.persistence;

import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import de.devsurf.echo.sync.persistence.FieldEntity;

public class UserEntity {
	private long id;
	private String email;
	private boolean locked;
	private AccountEntity account;
	private PasswordEntity password;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static class AccountEntity {
		private String externalId;
		private AccountType type;
		@Temporal(TemporalType.TIMESTAMP)
		private Date since;
		private URI url;
		
		private List<FieldEntity> fields;
	}
	
	public static enum AccountType {
		DIRECT,
		FACEBOOK,
		GITHUB,
		GOOGLE,
		OPENID,
		EXTERNAL
	}
	
	public static class PasswordEntity {
		private String password;
		private String salt;
		@Temporal(TemporalType.TIMESTAMP)
		private Date lastChanged;
	}
}

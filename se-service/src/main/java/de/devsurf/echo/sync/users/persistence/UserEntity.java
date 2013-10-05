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
	
	/*
	 	SecureRandom random = new SecureRandom();
		byte bytes[] = new byte[20];
		random.nextBytes(bytes);
		byte seed[] = random.generateSeed(20);
	 */
}

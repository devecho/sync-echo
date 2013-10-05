package de.devsurf.echo.sync.users.persistence;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.security.NoSuchAlgorithmException;

import javax.inject.Provider;
import javax.inject.Qualifier;

import com.google.common.io.BaseEncoding;

import de.devsurf.common.lang.formatter.ExceptionMessage;

public class SecureRandomProvider implements Provider<String> {
	private final static java.security.SecureRandom INSTANCE;

	static {
		try {
			INSTANCE = java.security.SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(ExceptionMessage
					.format("Algorithm doesn't exists.")
					.addParameter("algo", "SHA1PRNG").build());
		}
	}

	@Override
	public String get() {
		byte bytes[] = new byte[20];
		INSTANCE.nextBytes(bytes);
		return BaseEncoding.base64Url().encode(bytes);
	}

	@Target({ ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	@Qualifier
	public static @interface SecureRandom {

	}
}

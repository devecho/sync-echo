package de.devsurf.echo.sync.security.internal;

import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import org.json.JSONArray;
import org.json.JSONObject;

import de.devsurf.common.lang.di.SecureRandom;
import de.devsurf.common.lang.formatter.ExceptionMessage;
import de.devsurf.common.lang.obfuscation.ObfuscatedString;
import de.devsurf.common.lang.secret.vault.Vault;
import de.devsurf.common.lang.secret.vault.exceptions.VaultException;
import de.devsurf.echo.sync.security.exceptions.api.InvalidTokenException;
import de.devsurf.echo.sync.security.exceptions.api.TokenExpiredException;
import de.devsurf.echo.sync.security.vault.internal.JSONVaultReader;
import de.devsurf.echo.sync.security.vault.internal.JSONVaultWriter;
import de.devsurf.echo.sync.users.persistence.UserEntity;
import de.devsurf.security.otp.api.Clock;
import de.devsurf.security.otp.api.Totp;

public class AccessTokenFactory {
	// username/expires/created/otp/mac[/option1/option2]#securedHash
	public final static String INFO_FORMAT = "%s/%d/%d/%s/%s";
	public final static int TOKEN_PARTS = 5;
	public final static String OTP_KEY = "secured.store.otp";
	public final static String OTP_VALUES_KEY = "otp.values";

	@Inject
	private Vault store;

	@Inject
	@Named("mac")
	private String mac;

	@Inject
	public void initialize(@SecureRandom String base) throws VaultException {
		String key = AccessTokenFactory.OTP_KEY + "." + mac;
		if (!store.contains(key)) {
			try {
				JSONObject json = new JSONObject();
				if (base.length() < 20) {
					throw new IllegalStateException(ExceptionMessage
							.format("OTP Secret must be 20 characters long.")
							.addParameter("secret", base)
							.addParameter("lenght", base.length()).build());
				}
				String secret = base.substring(0, 20);
				List<String> values = ObfuscatedString.obfuscate(secret).values();
				json.put(AccessTokenFactory.OTP_VALUES_KEY, values);

				store.store(key, json, new JSONVaultWriter());
			} catch (Exception e) {
				throw new VaultException(e);
			}
		}
	}

	public AccessToken<String> generate(UserEntity user, long expiresIn,
			TimeUnit unit, Collection<String> additionalOptions) {
		if (additionalOptions == null) {
			additionalOptions = Collections.emptyList();
		}
		try {
			long timestamp = System.currentTimeMillis();
			long expires = timestamp + unit.toMillis(expiresIn);
			String token = String.format(INFO_FORMAT, user.getId(), timestamp,
					expires, generate(mac, timestamp).now(), mac);

			for (String option : additionalOptions) {
				option = option.replace("/", "");
				if (option.length() == 0) {
					throw new IllegalArgumentException(
							"Empty option parameter is not allowed.");
				}
				token = token + "/" + option;
			}

			String secured = token + "#" + store.sign(token);
			String encrypted = store.encrypt(secured);

			StringAccessToken stringAccessToken = new StringAccessToken(
					encrypted, new Date(expires));

			stringAccessToken.setOptions(additionalOptions);
			return stringAccessToken;

		} catch (Exception e) {
			throw new IllegalStateException("Couldn't generate token.", e);
		}
	}

	public boolean validate(UserEntity user, AccessToken<String> accessToken)
			throws InvalidTokenException {
		try {
			// if access token is valid login
			if (accessToken == null) {
				throw new InvalidTokenException(
						"No AccessToken was passed for login.");
			}

			String decrypted = store.decrypt(accessToken.token());
			if (decrypted.lastIndexOf("#") > 0) {
				String[] tokens = decrypted.split("#");
				String token = tokens[0];
				String signature = tokens[1];

				String[] tokenParts = token.split("/");
				if (tokenParts.length >= AccessTokenFactory.TOKEN_PARTS) {
					boolean valid = store.verify(token, signature);
					if (valid) {
						String tokenPartUsername = tokenParts[0];
						long tokenPartCreated = Long.parseLong(tokenParts[1]);
						long tokenPartExpires = Long.parseLong(tokenParts[2]);
						String tokenPartOtp = tokenParts[3];
						String tokenPartMac = tokenParts[4];

						if (!Long.toString(user.getId()).equalsIgnoreCase(
								tokenPartUsername)) {
							throw new InvalidTokenException(
									"AccessToken contains wrong username.");
						}

						long timestamp = System.currentTimeMillis();
						if (tokenPartExpires < timestamp) {
							throw new TokenExpiredException(
									"AccessToken is expired.");
						}

						Totp otp = generate(tokenPartMac, tokenPartCreated);
						if (!otp.verify(tokenPartOtp)) {
							throw new InvalidTokenException(
									"AccessToken otp parameter is not valid.");
						}

						// check the additional options
						Collection<String> additionalOptions = accessToken
								.options();
						if (additionalOptions == null) {
							additionalOptions = Collections.emptyList();
						} else {
							for (String option : additionalOptions) {
								if (option.length() == 0) {
									throw new InvalidTokenException(
											"Empty option parameter is not allowed.");
								}
							}
						}

						int optionCountInToken = Math.max(0, tokenParts.length
								- AccessTokenFactory.TOKEN_PARTS);
						if (additionalOptions.size() != optionCountInToken) {
							throw new InvalidTokenException(
									"AccessToken options parameter doesn't match.");
						}

						Set<String> options = new HashSet<String>(
								additionalOptions);
						for (int i = 7; i < tokenParts.length; i++) {
							String option = tokenParts[i];
							if (!options.contains(option)) {
								throw new InvalidTokenException(
										"Option in Access Token doesn't match.");
							}
						}

						return true;
					} else {
						throw new InvalidTokenException(
								"AccessToken isn't valid, after signature check.");
					}
				} else {
					throw new InvalidTokenException(
							"AccessToken doesn't contain the exact amount of parts.");
				}
			} else {
				throw new InvalidTokenException(
						"AccessToken doesn't contain Signature.");
			}
		} catch (SignatureException e) {
			throw new InvalidTokenException(
					"The signature of the Access Token can't be verified.", e);
		} catch (VaultException e) {
			throw new InvalidTokenException(
					"The Access Token is corrupt and can't be decrypted.", e);
		} catch (Exception e) {
			throw new InvalidTokenException(
					"Failure while trying to validate the token", e);
		}
	}

	public Totp generate(long timestamp) {
		return generate(mac, timestamp);
	}
	
	public Totp generate(String mac, long timestamp) {
		String key = AccessTokenFactory.OTP_KEY + "." + mac;
		try {
			if (store.contains(key)) {
				JSONObject json = store.load(key, new JSONVaultReader());
				JSONArray array = json
						.getJSONArray(AccessTokenFactory.OTP_VALUES_KEY);
				int count = array.length();
				List<String> values = new ArrayList<String>();
				for (int i = 0; i < count; i++) {
					String hex = array.getString(i);
					values.add(hex);
				}

				String secret = new ObfuscatedString(values).deobfuscate();
				Totp totp = Totp.configure(secret)
						.clock(new Clock.StaticClock(timestamp)).build();
				totp.now(); // validation

				return totp;
			}

			throw new IllegalStateException(
					"No informations were found for given mac adress.");
		} catch (Exception e) {
			throw new IllegalStateException("Couldn't generate token.", e);
		}
	}
}

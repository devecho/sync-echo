package de.devsurf.echo.sync.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public final class SecretGenerator {

	public static KeyPair generateKeyPair(String algorithm, int keysize)
			throws NoSuchAlgorithmException {
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(algorithm);
		kpg.initialize(keysize);
		return kpg.generateKeyPair();
	}

	public static SecretKey generateKey(String algorithm, int keysize)
			throws NoSuchAlgorithmException {
		KeyGenerator gen = KeyGenerator.getInstance(algorithm);
		gen.init(keysize);
		return gen.generateKey();
	}

}
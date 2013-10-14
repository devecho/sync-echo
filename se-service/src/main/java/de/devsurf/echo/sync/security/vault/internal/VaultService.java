package de.devsurf.echo.sync.security.vault.internal;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Named;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

import de.devsurf.common.lang.di.InjectLogger;
import de.devsurf.common.lang.formatter.ExceptionMessage;
import de.devsurf.common.lang.obfuscation.ObfuscatedString;
import de.devsurf.common.lang.secret.Ciphers;
import de.devsurf.common.lang.secret.SecretGenerator;
import de.devsurf.common.lang.secret.vault.Vault;
import de.devsurf.common.lang.secret.vault.VaultReader;
import de.devsurf.common.lang.secret.vault.VaultWriter;
import de.devsurf.common.lang.secret.vault.exceptions.NotExistingException;
import de.devsurf.common.lang.secret.vault.exceptions.StoreException;
import de.devsurf.common.lang.secret.vault.exceptions.VaultException;
import de.devsurf.echo.sync.persistence.ItemAlreadyExistsException;
import de.devsurf.echo.sync.system.persistence.SettingEntity;
import de.devsurf.echo.sync.system.persistence.SettingPersistency;

public final class VaultService implements Vault {
	@InjectLogger
	protected Logger logger;

	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	@Inject
	private SettingPersistency store;

	@Inject
	@Named("values")
	private List<String> bootstrapValues;

	@Inject
	@Named("bootstrap")
	private String bootstrap;

	private Key cryptKey;

	private String cryptAlgorithm;

	private KeyPair signKey;

	private String signAlgorithm;

	private boolean reset;

	@Inject
	public void initialize() throws VaultException {
		cryptKey = null;
		cryptAlgorithm = null;
		signKey = null;
		signAlgorithm = null;

		try {
			setupEncryption();

			String b = new ObfuscatedString(bootstrapValues).deobfuscate();
			SecretKey bk = new SecretKeySpec(BaseEncoding.base64().decode(b),
					bootstrap);

			Ciphers.cipher(bootstrap, bk, true); // for validation
			cryptKey = bk;
			cryptAlgorithm = bootstrap;

			JSONObject json = load(ENCRYPTION_KEY, new JSONVaultReader());
			JSONArray array = json.getJSONArray(ENCRYPTION_VALUES_KEY);
			String algorithm = json.getString(ENCRYPTION_ALGORITHM_KEY);
			int count = array.length();
			List<String> values = new ArrayList<String>();
			for (int i = 0; i < count; i++) {
				String hex = array.getString(i);
				values.add(hex);
			}

			String string = new ObfuscatedString(values).deobfuscate();
			SecretKey key = new SecretKeySpec(BaseEncoding.base64().decode(
					string), algorithm);

			Ciphers.cipher(algorithm, key, true); // for validation

			this.cryptKey = key;
			this.cryptAlgorithm = algorithm;
		} catch (Exception e) {
			throw new VaultException(e);
		}

		try {
			setupSignatures();

			JSONObject json = load(SIGNATURE_KEY, new JSONVaultReader());

			String signAlgorithm = json.getString(SIGN_ALGORITHM_KEY);
			String keyPairAlgorithm = json
					.getString(SIGN_KEYPAIR_ALGORITHM_KEY);

			JSONArray publicValueArray = json.getJSONArray(SIGN_PUBLIC_KEY);
			int publicValueCount = publicValueArray.length();
			List<String> publicValues = new ArrayList<String>();
			for (int i = 0; i < publicValueCount; i++) {
				String hex = publicValueArray.getString(i);
				publicValues.add(hex);
			}
			String publicValue = new ObfuscatedString(publicValues)
					.deobfuscate();

			JSONArray privateValueArray = json.getJSONArray(SIGN_PRIVATE_KEY);
			int privateValueCount = privateValueArray.length();
			List<String> privateValues = new ArrayList<String>();
			for (int i = 0; i < privateValueCount; i++) {
				String hex = privateValueArray.getString(i);
				privateValues.add(hex);
			}
			String privateValue = new ObfuscatedString(privateValues)
					.deobfuscate();

			KeyFactory keyFactory = KeyFactory.getInstance(keyPairAlgorithm);

			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
					BaseEncoding.base64().decode(publicValue));
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(
					BaseEncoding.base64().decode(privateValue));
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

			KeyPair keyPair = new KeyPair(publicKey, privateKey);
			Signature sig = Signature.getInstance(signAlgorithm);
			sig.initVerify(publicKey); // for validation
			sig.initSign(privateKey);

			this.signKey = keyPair;
			this.signAlgorithm = signAlgorithm;
		} catch (Exception e) {
			throw new VaultException(e);
		}
	}

	public <ValueType, GenericExceptionType extends Throwable, WriterType extends VaultWriter<ValueType, GenericExceptionType>> void store(
			String key, ValueType value, WriterType writer)
			throws VaultException, GenericExceptionType {
		SettingEntity setting = store.get(convert(key));
		if (setting != null) {
			throw new NotExistingException(ExceptionMessage
					.format("Entry for key already exists.")
					.addParameter("key", key).build());
		}

		if (cryptKey != null) {
			try {
				Cipher cipher = Ciphers.cipher(cryptAlgorithm, cryptKey, true);
				writer.init(cipher);
			} catch (Exception e) {
				throw new StoreException("Encryption couldn't be established.",
						e);
			}
		}

		SettingEntity entity = new SettingEntity();
		entity.setId(convert(key));
		entity.setContent(writer.write(value));
		try {
			store.persist(entity);
		} catch (ItemAlreadyExistsException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}

	public <ValueType, GenericExceptionType extends Throwable, LoaderType extends VaultReader<ValueType, GenericExceptionType>> ValueType load(
			String key, LoaderType loader) throws GenericExceptionType,
			VaultException {
		SettingEntity setting = store.get(convert(key));
		if (setting == null) {
			throw new NotExistingException(ExceptionMessage
					.format("No entry was found for key.")
					.addParameter("key", key).build());
		}

		if (cryptKey != null) {
			try {
				Cipher cipher = Ciphers.cipher(cryptAlgorithm, cryptKey, false);
				loader.init(cipher);
			} catch (Exception e) {
				throw new StoreException("Encryption couldn't be established.",
						e);
			}
		}

		return loader.read(setting.getContent());
	}

	@Override
	public void delete(String key) throws NotExistingException, VaultException {
		SettingEntity setting = store.get(convert(key));
		if (setting == null) {
			throw new NotExistingException(ExceptionMessage
					.format("No entry was found for key.")
					.addParameter("key", key).build());
		}
		store.delete(setting);
	}

	@Override
	public boolean contains(String key) throws VaultException {
		return null != store.get(convert(key));
	}

	@Override
	public String sign(String toSign) throws SignatureException, VaultException {
		try {
			Signature sig = Signature.getInstance(signAlgorithm);
			sig.initSign(signKey.getPrivate());
			sig.update(toSign.getBytes(Charsets.UTF_8));
			byte[] signatureBytes = sig.sign();

			return BaseEncoding.base64().encode(signatureBytes);
		} catch (SignatureException e) {
			throw e;
		} catch (Exception e) {
			throw new VaultException(
					"Couldn't sign the item, because of an failure of the signkey.",
					e);
		}
	}

	@Override
	public boolean verify(String signed, String hash)
			throws SignatureException, VaultException {
		try {
			Signature sig = Signature.getInstance(signAlgorithm);
			sig.initVerify(signKey.getPublic());
			sig.update(signed.getBytes(Charsets.UTF_8));

			return sig.verify(BaseEncoding.base64().decode(hash));
		} catch (SignatureException e) {
			throw e;
		} catch (Exception e) {
			throw new VaultException(
					"Couldn't verify the signature of the item, because of an failure with the signkey.",
					e);
		}
	}

	@Override
	public String encrypt(String toEncrypt) throws VaultException {
		try {
			Cipher cipher = Ciphers.cipher(cryptAlgorithm, cryptKey, true);
			byte[] encrypted = cipher.doFinal(toEncrypt
					.getBytes(Charsets.UTF_8));
			return BaseEncoding.base64Url().encode(encrypted);
		} catch (Exception e) {
			throw new VaultException("String couldn't be encrypted.", e);
		}
	}

	@Override
	public String decrypt(String toDecrypt) throws VaultException {
		try {
			Cipher cipher = Ciphers.cipher(cryptAlgorithm, cryptKey, false);
			byte[] base = BaseEncoding.base64().decode(toDecrypt);
			return new String(cipher.doFinal(base), Charsets.UTF_8);
		} catch (Exception e) {
			throw new VaultException("String couldn't be decrypted.", e);
		}
	}

	private String convert(String key) {
		return BaseEncoding.base64().encode(key.getBytes(Charsets.UTF_8));
	}

	private void setupEncryption() throws VaultException {
		boolean contains = contains(ENCRYPTION_KEY);
		if (reset && contains) {
			delete(ENCRYPTION_KEY);
			contains = false;
		}

		if (!contains) {
			try {
				String algorithm = DEFAULT_ENCRYPT_ALGORITHM;
				SecretKey key = SecretGenerator.generateKey(algorithm,
						DEFAULT_KEY_SIZE);

				String base = BaseEncoding.base64().encode(key.getEncoded());
				ObfuscatedString obfuscated = ObfuscatedString.obfuscate(base);
				List<String> values = obfuscated.values();
				JSONObject json = new JSONObject();
				json.put(ENCRYPTION_VALUES_KEY, values);
				json.put(ENCRYPTION_ALGORITHM_KEY, algorithm);

				String b = new ObfuscatedString(bootstrapValues).deobfuscate();
				SecretKey bk = new SecretKeySpec(BaseEncoding.base64()
						.decode(b), bootstrap);

				Ciphers.cipher(bootstrap, bk, true);// init for validation
				cryptKey = bk;
				cryptAlgorithm = bootstrap;

				store(ENCRYPTION_KEY, json, new JSONVaultWriter());
			} catch (VaultException e) {
				throw e;
			} catch (Exception e) {
				throw new VaultException("Couldn't store signature.", e);
			}
		}
	}

	private void setupSignatures() throws VaultException {
		boolean contains = contains(SIGNATURE_KEY);
		if (reset && contains) {
			delete(SIGNATURE_KEY);
			contains = false;
		}
		if (!contains) {
			try {
				String keyPairAlgorithm = DEFAULT_KEYPAIR_ALGORITHM;
				String signAlgorithm = DEFAULT_SIGN_ALGORITHM;
				int keyPairSize = DEFAULT_KEYPAIR_SIZE;
				KeyPair pair = SecretGenerator.generateKeyPair(
						keyPairAlgorithm, keyPairSize);

				JSONObject json = new JSONObject();
				json.put(SIGN_KEYPAIR_ALGORITHM_KEY, keyPairAlgorithm);
				json.put(SIGN_KEYPAIR_SIZE_KEY, keyPairSize);
				json.put(SIGN_ALGORITHM_KEY, signAlgorithm);

				String publicKey = BaseEncoding.base64().encode(
						pair.getPublic().getEncoded());
				;
				List<String> publicValues = ObfuscatedString.obfuscate(
						publicKey).values();
				json.put(SIGN_PUBLIC_KEY, publicValues);

				String privateKey = BaseEncoding.base64().encode(
						pair.getPrivate().getEncoded());
				List<String> privateValues = ObfuscatedString.obfuscate(
						privateKey).values();
				json.put(SIGN_PRIVATE_KEY, privateValues);

				store(SIGNATURE_KEY, json, new JSONVaultWriter());
			} catch (VaultException e) {
				throw e;
			} catch (Exception e) {
				throw new VaultException("Couldn't store signature.", e);
			}
		}
	}

	public final static String DEFAULT_ENCRYPT_ALGORITHM = "AES";
	public final static int DEFAULT_KEY_SIZE = 128;
	public final static String ENCRYPTION_KEY = "secured.store.encryption";
	public final static String ENCRYPTION_ALGORITHM_KEY = "algorithm";
	public final static String ENCRYPTION_VALUES_KEY = "values";

	public final static String DEFAULT_SIGN_ALGORITHM = "SHA1withDSA";
	public final static String DEFAULT_KEYPAIR_ALGORITHM = "DSA";
	public final static int DEFAULT_KEYPAIR_SIZE = 512;
	public final static String SIGNATURE_KEY = "secured.store.signature";
	public final static String SIGN_ALGORITHM_KEY = "sign.algorithm";
	public final static String SIGN_KEYPAIR_ALGORITHM_KEY = "keypair.algorithm";
	public final static String SIGN_KEYPAIR_SIZE_KEY = "keypair.size";
	public final static String SIGN_PUBLIC_KEY = "keypair.public";
	public final static String SIGN_PRIVATE_KEY = "keypair.private";
}
package de.devsurf.echo.sync.security.vault.internal;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SignatureException;

import javax.crypto.Cipher;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

import de.devsurf.common.lang.di.InjectLogger;
import de.devsurf.common.lang.secret.Ciphers;
import de.devsurf.common.lang.secret.MessageDigests;
import de.devsurf.common.lang.secret.vault.Vault;
import de.devsurf.common.lang.secret.vault.VaultReader;
import de.devsurf.common.lang.secret.vault.VaultWriter;
import de.devsurf.common.lang.secret.vault.exceptions.LoadException;
import de.devsurf.common.lang.secret.vault.exceptions.NotExistingException;
import de.devsurf.common.lang.secret.vault.exceptions.StoreException;
import de.devsurf.common.lang.secret.vault.exceptions.VaultException;

public class UserVault implements Vault {
	@InjectLogger
	protected Logger logger;

	private int userId;
	
	@Inject
	private Vault secured;
	
	protected UserVault(){
	}

	@Override
	public <ValueType, GenericExceptionType extends Throwable, WriterType extends VaultWriter<ValueType, GenericExceptionType>> void store(
			String key, ValueType value, WriterType writer)
			throws StoreException, VaultException, GenericExceptionType {
		throw new UnsupportedOperationException("SecuredStore.store(..) is not implemented. Try an instance of ServerSecuredStore.");
	}

	@Override
	public <ValueType, GenericExceptionType extends Throwable, LoaderType extends VaultReader<ValueType, GenericExceptionType>> ValueType load(
			String key, LoaderType loader) throws GenericExceptionType,
			LoadException, NotExistingException, VaultException {
		throw new UnsupportedOperationException("SecuredStore.load(..) is not implemented. Try an instance of ServerSecuredStore.");
	}

	@Override
	public boolean contains(String key) throws VaultException {
		throw new UnsupportedOperationException("SecuredStore.contains(..) is not implemented. Try an instance of ServerSecuredStore.");
	}

	@Override
	public void delete(String key) throws NotExistingException,
			VaultException {
		throw new UnsupportedOperationException("SecuredStore.delete(..) is not implemented. Try an instance of ServerSecuredStore.");		
	}

	@Override
	public String sign(String toSign) throws SignatureException,
			VaultException {
		throw new UnsupportedOperationException("SecuredStore.sign(..) is not implemented. Try an instance of ServerSecuredStore.");
	}

	@Override
	public boolean verify(String signed, String hash)
			throws SignatureException, VaultException {
		throw new UnsupportedOperationException("SecuredStore.verify(..) is not implemented. Try an instance of ServerSecuredStore.");
	}

	@Override
	public String encrypt(String toEncrypt) throws VaultException {
		try {
			Cipher cipher = Ciphers.aes256(salt(userId), true);
			byte[] encrypted = cipher.doFinal(toEncrypt.getBytes(Charsets.UTF_8));
			return secured.encrypt(BaseEncoding.base64().encode(encrypted));
		} catch (VaultException e) {
			throw e;
		} catch (Exception e) {;
			throw new VaultException(e.getMessage(), e);
		}
	}

	@Override
	public String decrypt(String toDecrypt) throws VaultException {
		byte[] original;
		try {
			Cipher cipher = Ciphers.aes256(salt(userId), false);
			String decrypted = secured.decrypt(toDecrypt);
			original = cipher.doFinal(BaseEncoding.base64().decode(decrypted));
			return new String(original, Charsets.UTF_8);
		} catch (VaultException e) {
			throw e;
		} catch (Exception e) {
			throw new VaultException(e.getMessage(), e);
		}
	}
	
	public static byte[] salt(int userId) {
		MessageDigest d = MessageDigests.md5();
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(userId);		
		return d.digest(buffer.array());
	}
	
	public UserVault forUser(int userId) {
		this.userId = userId;
		return this;
	}
	
	public static UserVault with(Vault store) {
		UserVault facade = new UserVault();
		facade.secured = store;
		return facade;
	}
}

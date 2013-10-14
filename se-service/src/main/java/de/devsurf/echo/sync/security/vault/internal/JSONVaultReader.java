package de.devsurf.echo.sync.security.vault.internal;

import java.nio.charset.Charset;

import javax.crypto.Cipher;

import org.json.JSONObject;

import com.google.common.io.BaseEncoding;

import de.devsurf.common.lang.secret.vault.VaultReader;

public class JSONVaultReader implements VaultReader<JSONObject, Exception>{
	private Cipher decrypt;

	@Override
	public void init(Cipher cipher) {
		this.decrypt = cipher;	
	}

	@Override
	public JSONObject read(String in) throws Exception {
		if(decrypt == null) {
			return new JSONObject(in);	
		} else {
			final byte[] encrypted = BaseEncoding.base64().decode(in);
			final byte[] decrypted = decrypt.doFinal(encrypted);
			
			return new JSONObject(new String(decrypted, Charset.forName("UTF-8")));
		}
	}
}
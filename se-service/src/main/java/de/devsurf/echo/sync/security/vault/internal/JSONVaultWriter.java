package de.devsurf.echo.sync.security.vault.internal;

import java.nio.charset.Charset;

import javax.crypto.Cipher;

import org.json.JSONObject;

import com.google.common.io.BaseEncoding;

import de.devsurf.common.lang.secret.vault.VaultWriter;

public class JSONVaultWriter implements VaultWriter<JSONObject, Exception>{
	private Cipher encrypt;
	
	@Override
	public void init(Cipher cipher) {
		this.encrypt = cipher;
	}
	
	@Override
	public String write(JSONObject value) throws Exception {
		if(encrypt == null) {
			return value.toString();	
		} else {
			final byte[] original = value.toString().getBytes(Charset.forName("UTF-8"));
			final byte[] encrypted = encrypt.doFinal(original);
			
			return BaseEncoding.base64().encode(encrypted);	
		}
	}	
}
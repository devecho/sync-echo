package de.devsurf.echo.sync.security.exceptions.api;


public class TokenExpiredException extends Exception {
	private static final long serialVersionUID = 686688194483856519L;

	public TokenExpiredException() {
		super();
	}

	public TokenExpiredException(String s, Throwable cause) {
		super(s, cause);
	}

	public TokenExpiredException(String s) {
		super(s);
	}

}

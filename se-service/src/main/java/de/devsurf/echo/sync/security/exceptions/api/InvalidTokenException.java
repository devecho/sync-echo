package de.devsurf.echo.sync.security.exceptions.api;


public class InvalidTokenException extends Exception {
	private static final long serialVersionUID = 8561691778461043786L;

	public InvalidTokenException() {
		super();
	}

	public InvalidTokenException(String s, Throwable cause) {
		super(s, cause);
	}

	public InvalidTokenException(String s) {
		super(s);
	}

}

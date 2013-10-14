package de.devsurf.echo.sync.security.internal;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

public interface AccessToken<Type> extends Serializable {
	Type token();
	
	Collection<String> options();
	
	Date expires();
}
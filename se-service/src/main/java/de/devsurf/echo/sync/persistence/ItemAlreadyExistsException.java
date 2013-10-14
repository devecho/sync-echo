package de.devsurf.echo.sync.persistence;

public class ItemAlreadyExistsException extends Exception {

	private static final long serialVersionUID = -4395551649499002729L;
	
	private String id;
	
	private String type;

	public ItemAlreadyExistsException(long id, String type, Throwable cause) {
		this(Long.toString(id), type, cause);
	}
	
	public ItemAlreadyExistsException(String id, String type, Throwable cause) {
		super(String.format("%s with id %s already exists.", type, id), cause);
		this.id = id;
		this.type = type;
	}


	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}
}

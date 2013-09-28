package de.devsurf.echo.sync.errors;


import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;


public class ErrorResponse {
	private String name;
	private String id;

	private ErrorResponse(String name) {
		this.name = name;
	}

	public ErrorResponse withId(String id) {
		this.id = id;
		return this;
	}
	
	public ErrorResponse withId(long id) {
		this.id = Long.toString(id);
		return this;
	}

	public Response wasNotFound() {
		String message = String
				.format("%s for id [%s] wasn't found.", name, id);
		String json = String.format("{ \"message\" : \"%s\"}", message);
		return Response.status(Status.NOT_FOUND)
				.type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
	}
	
	public Response forStatus(Status status) {
		String message = String
				.format("%s for id [%s] wasn't found.", name, id);
		String json = String.format("{ \"message\" : \"%s\"}", message);
		return Response.status(status)
				.type(MediaType.APPLICATION_JSON_TYPE).entity(json).build();
	}

	public static ErrorResponse item(String name) {
		return new ErrorResponse(name);
	}
}
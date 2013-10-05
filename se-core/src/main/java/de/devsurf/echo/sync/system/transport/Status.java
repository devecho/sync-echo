package de.devsurf.echo.sync.system.transport;

import java.net.URI;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import de.devsurf.echo.frameworks.rs.api.Type;
import de.devsurf.echo.frameworks.rs.api.Typed;

@JsonPropertyOrder({ "type", "id", "title", "message", "level", "url",
		"created" })
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSerialize(include = Inclusion.NON_NULL)
public class Status implements Typed {
	private String id;
	private String title;
	private String message;
	private String level;
	private URI url;
	private Date created;

	public static final Type TYPE = new Type() {
		@Override
		public String value() {
			return "status";
		}
	};

	@Override
	public Type type() {
		return TYPE;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public URI getUrl() {
		return url;
	}

	public void setUrl(URI url) {
		this.url = url;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
}
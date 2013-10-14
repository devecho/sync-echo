package de.devsurf.echo.sync.system.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import de.devsurf.common.lang.formatter.ExceptionMessage;

@Entity
@Table(name = "settings")
public class SettingEntity {
	private static final int CONTENT_FIELD_LENGTH = 8192;

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "content", nullable = false, length = CONTENT_FIELD_LENGTH)
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		if (content.length() > CONTENT_FIELD_LENGTH) {
			throw new IllegalArgumentException(ExceptionMessage
					.format("Content length exceeds field length.")
					.addParameter("length", content.length())
					.addParameter("allowed", CONTENT_FIELD_LENGTH).build());
		}
		this.content = content;
	}
}

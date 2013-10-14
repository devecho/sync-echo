package de.devsurf.echo.sync.utils;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Locale;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

import de.devsurf.common.lang.formatter.TemplateMessage;
import de.devsurf.common.lang.values.Valueable;

public class Templates {
	public static String load(String name, TemplateType type)
			throws Exception {
		return load(name, type, Locale.ENGLISH, null);
	}
	
	public static String load(String name, TemplateType type, Locale locale)
			throws Exception {
		return load(name, type, locale, null);
	}

	public static String load(String name, TemplateType type, Locale locale,
			Locale fallback) throws Exception {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(type);
		Preconditions.checkNotNull(locale);
		
		String group = type.value();
		URL resource = Templates.class.getResource("/templates/" + group + "/"
				+ name + "." + locale.getLanguage() + ".html");
		if (resource == null) {
			// try default
			String location;
			if (fallback != null) {
				location = "/templates/" + group + "/" + name + "."
						+ fallback.getLanguage() + ".html";
			} else {
				location = "/templates/" + group + "/" + name + ".html";
			}
			resource = Templates.class.getResource(location);

			if (resource == null) {
				Preconditions
						.checkArgument(
								resource != null,
								"either the request language %s nor the default template exists in %s",
								(locale != null ? locale.getLanguage()
										: "default"), location);
			}
		}

		byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
		return new String(bytes, Charsets.UTF_8);
	}

	public static enum DefaultTemplateType implements TemplateType {
		EMAIL("mail");
		private String group;

		private DefaultTemplateType(String group) {
			this.group = group;
		}

		@Override
		public String value() {
			return group;
		}
	}

	public static interface TemplateType extends Valueable<String> {

	}

	public static void main(String[] args) throws Exception {
		String content = Templates.load("registration",
				DefaultTemplateType.EMAIL, Locale.FRENCH);
		System.out.println(content);
		System.out.println(TemplateMessage.format(content,
				Collections.singletonMap("link", "http://github.com/deveco")));
	}
}

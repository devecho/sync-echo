package de.devsurf.echo.sync.api;

import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.codehaus.jackson.annotate.JsonTypeName;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import de.devsurf.echo.sync.transport.FieldPojo;


@JsonPropertyOrder({ "type", "name", "kind", "value" })
@JsonSerialize(include = Inclusion.NON_NULL)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({ @org.codehaus.jackson.annotate.JsonSubTypes.Type(value = FieldPojo.class, name = "field") })
@JsonTypeName("field")
public interface Field {
	public FieldType getKind();
	
	public String getName();

	public String getValue();
}
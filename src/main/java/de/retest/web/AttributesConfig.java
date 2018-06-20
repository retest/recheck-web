package de.retest.web;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributesConfig {

	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	private final List<String> attributes;
	private final List<String> identifyingAttributes;

	@JsonCreator
	public AttributesConfig( @JsonProperty( "attributes" ) final List<String> attributes,
			@JsonProperty( "identifyingAttributes" ) final List<String> identifyingAttributes ) {
		this.attributes = attributes;
		this.identifyingAttributes = identifyingAttributes;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public List<String> getIdentifyingAttributes() {
		return identifyingAttributes;
	}

}

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
	private final List<String> sizes;

	@JsonCreator
	public AttributesConfig( @JsonProperty( "attributes" ) final List<String> attributes,
			@JsonProperty( "identifyingAttributes" ) final List<String> identifyingAttributes,
			@JsonProperty( "sizes" ) final List<String> sizes ) {
		this.attributes = attributes;
		this.identifyingAttributes = identifyingAttributes;
		this.sizes = sizes;
	}

	public List<String> getAttributes() {
		return attributes;
	}

	public List<String> getIdentifyingAttributes() {
		return identifyingAttributes;
	}

	public List<String> getSizes() {
		return sizes;
	}
}

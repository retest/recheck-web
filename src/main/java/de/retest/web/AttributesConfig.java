package de.retest.web;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributesConfig {

	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	private final List<String> textualAttributes;
	private final List<String> numericalAttributes;
	private final List<String> identifyingAttributes;

	@JsonCreator
	public AttributesConfig( @JsonProperty( "textualAttributes" ) final List<String> textualAttributes,
			@JsonProperty( "numericalAttributes" ) final List<String> numericalAttributes,
			@JsonProperty( "identifyingAttributes" ) final List<String> identifyingAttributes ) {
		this.textualAttributes = textualAttributes;
		this.numericalAttributes = numericalAttributes;
		this.identifyingAttributes = identifyingAttributes;
	}

	public List<String> getTextualAttributes() {
		return textualAttributes;
	}

	public List<String> getNumericalAttributes() {
		return numericalAttributes;
	}

	public List<String> getIdentifyingAttributes() {
		return identifyingAttributes;
	}
}

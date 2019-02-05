package de.retest.web;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributesConfig {

	public static final String ABSOLUTE_X = "absolute-x";
	public static final String ABSOLUTE_Y = "absolute-y";
	public static final String ABSOLUTE_WIDTH = "absolute-width";
	public static final String ABSOLUTE_HEIGHT = "absolute-height";

	public static final String X = "x";
	public static final String Y = "y";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";

	public static final String TAG_NAME = "tagName";

	public static final String TEXT = "text";

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

	public static List<String> getAllIdentifyingKeys() {
		return Arrays.asList( ABSOLUTE_HEIGHT, ABSOLUTE_WIDTH, ABSOLUTE_Y, ABSOLUTE_X, X, Y, WIDTH, HEIGHT, TAG_NAME,
				TEXT );
	}

}

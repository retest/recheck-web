package de.retest.web;

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

	private final List<String> cssAttributes;
	private final List<String> htmlAttributes;

	@JsonCreator
	public AttributesConfig( @JsonProperty( "cssAttributes" ) final List<String> cssAttributes,
			@JsonProperty( "htmlAttributes" ) final List<String> htmlAttributes ) {
		this.cssAttributes = cssAttributes;
		this.htmlAttributes = htmlAttributes;
	}

	public List<String> getCssAttributes() {
		return cssAttributes;
	}

	public List<String> getHtmlAttributes() {
		return htmlAttributes;
	}

}

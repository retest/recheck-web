package de.retest.web;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AttributesConfig {

	private final List<String> cssAttributes;
	private final List<String> htmlAttributes;

	@JsonCreator
	public AttributesConfig( @JsonProperty( "cssAttributes" ) final List<String> cssAttributes,
			@JsonProperty( "htmlAttributes" ) final List<String> htmlAttributes ) {
		this.cssAttributes = cssAttributes;
		this.htmlAttributes = htmlAttributes;
	}

	/**
	 * @return List of CSS style attributes that are added to an element's state.
	 */
	public List<String> getCssAttributes() {
		return cssAttributes;
	}

	/**
	 * @return List of HTML attributes that are used for element identification (a.k.a. identifying attributes).
	 */
	public List<String> getHtmlAttributes() {
		return htmlAttributes;
	}

}

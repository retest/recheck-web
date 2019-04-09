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
	 * List of CSS attributes that are added to an element's state. Can be a list of selected attributes or empty ("all"
	 * not supported).
	 *
	 * @return Possibly empty list of selected CSS attributes.
	 */
	public List<String> getCssAttributes() {
		return cssAttributes;
	}

	/**
	 * List of HTML attributes that are added to an element's state. Can be a list of selected attributes, empty, or
	 * "all". In the case of "all", you have to use {@link AttributesConfig#allHtmlAttributes()}.
	 *
	 * @return Possibly empty list of selected HTML attributes.
	 */
	public List<String> getHtmlAttributes() {
		return htmlAttributes;
	}

	/**
	 * @return {@code true} if all HTML attributes should be used, otherwise false.
	 */
	public boolean allHtmlAttributes() {
		// TODO Implement.
		return true;
	}

}

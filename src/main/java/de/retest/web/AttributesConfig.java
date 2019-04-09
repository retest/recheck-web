package de.retest.web;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize( using = AttributesConfigDeserializer.class )
public class AttributesConfig {

	private final List<String> cssAttributes;
	private final List<String> htmlAttributes;

	public AttributesConfig( final List<String> cssAttributes, final List<String> htmlAttributes ) {
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
	 * "all".
	 *
	 * @return Possibly empty list of selected HTML attributes or {@code null} in the case of "all".
	 */
	public List<String> getHtmlAttributes() {
		return htmlAttributes;
	}

	/**
	 * @return {@code true} if all HTML attributes should be used, otherwise false.
	 */
	public boolean allHtmlAttributes() {
		return htmlAttributes == null;
	}

}

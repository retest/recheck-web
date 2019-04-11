package de.retest.web;

import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize( using = AttributesConfigDeserializer.class )
public class AttributesConfig {

	private final Set<String> cssAttributes;
	private final Set<String> htmlAttributes;

	public AttributesConfig( final Set<String> cssAttributes, final Set<String> htmlAttributes ) {
		this.cssAttributes = cssAttributes;
		this.htmlAttributes = htmlAttributes;
	}

	/**
	 * Set of CSS attributes that are added to an element's state. Can be a set of selected attributes or empty ("all"
	 * not supported).
	 *
	 * @return Possibly empty set of selected CSS attributes.
	 */
	public Set<String> getCssAttributes() {
		return cssAttributes;
	}

	/**
	 * Set of HTML attributes that are added to an element's state. Can be a set of selected attributes, empty, or
	 * "all".
	 *
	 * @return Possibly empty set of selected HTML attributes or {@code null} in the case of "all".
	 */
	public Set<String> getHtmlAttributes() {
		return htmlAttributes;
	}

	/**
	 * @return {@code true} if all HTML attributes should be used, otherwise false.
	 */
	public boolean allHtmlAttributes() {
		return htmlAttributes == null;
	}

}

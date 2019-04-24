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
	 * @see AttributesConfig#getCssAttributes()
	 */
	public Set<String> getCssAttributes() {
		return cssAttributes;
	}

	/**
	 * @see AttributesConfig#getHtmlAttributes()
	 */
	public Set<String> getHtmlAttributes() {
		return htmlAttributes;
	}

	/**
	 * @see AttributesConfig#allHtmlAttributes()
	 */
	public boolean allHtmlAttributes() {
		return htmlAttributes == null;
	}

}

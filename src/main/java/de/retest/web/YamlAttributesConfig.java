package de.retest.web;

import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize( using = YamlAttributesConfigDeserializer.class )
public class YamlAttributesConfig {

	private final Set<String> cssAttributes;
	private final Set<String> htmlAttributes;

	public YamlAttributesConfig( final Set<String> cssAttributes, final Set<String> htmlAttributes ) {
		this.cssAttributes = cssAttributes;
		this.htmlAttributes = htmlAttributes;
	}

	/**
	 * @see YamlAttributesConfig#getCssAttributes()
	 */
	public Set<String> getCssAttributes() {
		return cssAttributes;
	}

	/**
	 * @see YamlAttributesConfig#getHtmlAttributes()
	 */
	public Set<String> getHtmlAttributes() {
		return htmlAttributes;
	}

	/**
	 * @see YamlAttributesConfig#allHtmlAttributes()
	 */
	public boolean allHtmlAttributes() {
		return htmlAttributes == null;
	}

}

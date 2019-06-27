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
	 * @return See {@link AttributesProvider#getCssAttributes()}.
	 */
	public Set<String> getCssAttributes() {
		return cssAttributes;
	}

	/**
	 * @return See {@link AttributesProvider#getHtmlAttributes()}.
	 */
	public Set<String> getHtmlAttributes() {
		return htmlAttributes;
	}

	/**
	 * @return See {@link AttributesProvider#allHtmlAttributes()}.
	 */
	public boolean allHtmlAttributes() {
		return htmlAttributes == null;
	}

}

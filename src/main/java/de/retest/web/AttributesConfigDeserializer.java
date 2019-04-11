package de.retest.web;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

class AttributesConfigDeserializer extends JsonDeserializer<AttributesConfig> {

	private static final String CSS_ATTRIBUTES_KEY = "cssAttributes";
	private static final String HTML_ATTRIBUTES_KEY = "htmlAttributes";
	private static final String ALL_VALUE = "all";

	@Override
	public AttributesConfig deserialize( final JsonParser parser, final DeserializationContext context )
			throws IOException, JsonProcessingException {
		final JsonNode node = parser.getCodec().readTree( parser );
		final JsonNode cssAttributesNode = node.get( CSS_ATTRIBUTES_KEY );
		final JsonNode htmlAttributesNode = node.get( HTML_ATTRIBUTES_KEY );
		return new AttributesConfig( toCssAttributesSet( cssAttributesNode ),
				toHtmlAttributesSet( htmlAttributesNode ) );
	}

	private Set<String> toCssAttributesSet( final JsonNode cssAttributesNode ) {
		if ( isAll( cssAttributesNode, CSS_ATTRIBUTES_KEY ) ) {
			throw new IllegalArgumentException(
					"CSS attributes can only be a set of selected attributes or empty ('all' not supported)." );
		}
		return toSet( cssAttributesNode );
	}

	private Set<String> toHtmlAttributesSet( final JsonNode htmlAttributesNode ) {
		if ( isAll( htmlAttributesNode, HTML_ATTRIBUTES_KEY ) ) {
			return null;
		}
		return toSet( htmlAttributesNode );
	}

	private boolean isAll( final JsonNode node, final String key ) {
		if ( !node.isTextual() ) {
			return false;
		}
		final String value = node.asText();
		if ( value.equals( ALL_VALUE ) ) {
			return true;
		}
		throw new IllegalArgumentException( "'" + value + "' is an invalid value for '" + key + "'." );
	}

	private Set<String> toSet( final JsonNode node ) {
		if ( node.isNull() ) {
			return Collections.emptySet();
		}
		return StreamSupport.stream( node.spliterator(), false ) //
				.map( JsonNode::asText ) //
				.collect( Collectors.toCollection( HashSet::new ) );
	}

}

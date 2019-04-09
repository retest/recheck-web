package de.retest.web;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
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
		if ( cssAttributesNode.isTextual() ) {
			throw new IllegalArgumentException(
					"CSS attributes can only be a set of selected attributes or empty ('all' not supported)." );
		}
		return toSet( cssAttributesNode );
	}

	private Set<String> toHtmlAttributesSet( final JsonNode htmlAttributesNode ) {
		if ( htmlAttributesNode.isTextual() ) {
			return null;
		}
		return toSet( htmlAttributesNode );
	}

	private Set<String> toSet( final JsonNode node ) {
		if ( node.isNull() ) {
			return Collections.emptySet();
		}
		final Spliterator<String> spliterator =
				Spliterators.spliteratorUnknownSize( node.fieldNames(), Spliterator.NONNULL );
		return StreamSupport.stream( spliterator, false ) //
				.collect( Collectors.toCollection( HashSet::new ) );
	}

}

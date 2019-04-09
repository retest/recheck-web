package de.retest.web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
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
		return new AttributesConfig( toCssAttributesList( cssAttributesNode ),
				toHtmlAttributesList( htmlAttributesNode ) );
	}

	private List<String> toCssAttributesList( final JsonNode cssAttributesNode ) {
		if ( cssAttributesNode.isTextual() ) {
			throw new IllegalArgumentException(
					"CSS attributes can only be a list of selected attributes or empty ('all' not supported)." );
		}
		return toList( cssAttributesNode );
	}

	private List<String> toHtmlAttributesList( final JsonNode htmlAttributesNode ) {
		if ( htmlAttributesNode.isTextual() ) {
			return null;
		}
		return toList( htmlAttributesNode );
	}

	private List<String> toList( final JsonNode node ) {
		if ( node.isNull() ) {
			return Collections.emptyList();
		}
		return StreamSupport.stream( node.spliterator(), false ) //
				.map( JsonNode::asText ) //
				.collect( Collectors.toList() );
	}

}

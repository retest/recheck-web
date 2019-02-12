package de.retest.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;

public class DefaultWebValueFinder implements DefaultValueFinder {

	public static final String DEFAULTS_FILE_PATH = "/defaults.yaml";

	private static final Set<String> commonDefaults = new HashSet<>( Arrays.asList( //
			"0px", //
			"0px 0px", //
			"auto", //
			"normal", //
			"rgb(0, 0, 0)", //
			"none" ) );

	private final Map<String, Map<String, String>> defaultValues;

	public DefaultWebValueFinder() {
		try ( final InputStream url = getClass().getResourceAsStream( DEFAULTS_FILE_PATH ) ) {
			defaultValues = readAttributesConfigFromFile( url );
		} catch ( final IOException e ) {
			throw new UncheckedIOException( "Cannot read defaults file '" + DEFAULTS_FILE_PATH + "'.", e );
		}
	}

	private Map<String, Map<String, String>> readAttributesConfigFromFile( final InputStream in ) throws IOException {
		final Map<String, Map<String, String>> defaultValues = new HashMap<>();
		final ObjectMapper mapper = new ObjectMapper( new YAMLFactory() );
		final JsonNode jsonNode = mapper.readTree( in );
		for ( final Iterator<Entry<String, JsonNode>> elements = jsonNode.fields(); elements.hasNext(); ) {
			final Entry<String, JsonNode> field = elements.next();
			final Map<String, String> defaults = new HashMap<>();
			final ArrayNode valuesNode = (ArrayNode) field.getValue();
			for ( final Iterator<JsonNode> values = valuesNode.elements(); values.hasNext(); ) {
				final Entry<String, JsonNode> value = values.next().fields().next();
				defaults.put( value.getKey(), value.getValue().asText() );
			}
			defaultValues.put( field.getKey(), defaults );
		}
		return defaultValues;
	}

	@Override
	public boolean isDefaultValue( final IdentifyingAttributes identifyingAttributes, final String attributeKey,
			final Serializable attributeValue ) {
		final String attributeValueString = attributeValue != null ? attributeValue.toString() : null;
		final String defaultValueString = getDefaultValue( identifyingAttributes, attributeKey );
		if ( defaultValueString != null ) {
			return defaultValueString.equalsIgnoreCase( attributeValueString );
		}
		if ( attributeValueString == null || attributeValueString.trim().isEmpty() ) {
			return true;
		}
		if ( commonDefaults.contains( attributeValueString ) ) {
			return true;
		}
		return false;
	}

	String getDefaultValue( final IdentifyingAttributes identifyingAttributes, final String attribute ) {
		final String tag = identifyingAttributes.getType();
		final Map<String, String> defaults = defaultValues.get( tag.toLowerCase() );
		if ( defaults != null ) {
			final String defaultValue = defaults.get( attribute.toLowerCase() );
			if ( defaultValue != null ) {
				return defaultValue;
			}
		}
		return defaultValues.get( "all" ).get( attribute.toLowerCase() );
	}

}

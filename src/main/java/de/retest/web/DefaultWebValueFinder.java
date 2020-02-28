package de.retest.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.NodeId;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

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
		Yaml yaml = new Yaml( new Constructor(), new Representer(), new DumperOptions(), new LoaderOptions(),
				new Resolver() {
			@Override
			public Tag resolve( NodeId kind, String value, boolean implicit ) {
				if ( (kind == NodeId.scalar) && implicit ) {
					return Tag.STR;
				}
				return super.resolve( kind, value, implicit );
			}
		} );
		Map<String, Object> loaded = yaml.load( in );
		for ( Map.Entry<String, Object> entry : loaded.entrySet() ) {
			final Map<String, String> defaults = new HashMap<>();
			ArrayList<LinkedHashMap<String, String>> value =
					(ArrayList<LinkedHashMap<String, String>>) entry.getValue();
			for ( LinkedHashMap<String, String> linkedHashMap : value ) {
				defaults.putAll( linkedHashMap );
			}
			defaultValues.put( entry.getKey(), defaults );
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

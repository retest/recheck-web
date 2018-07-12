package de.retest.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class AttributesProvider {

	public static final String ATTRIBUTES_FILE_PROPERTY = "de.retest.recheck.web.attributesFile";
	public static final String DEFAULT_ATTRIBUTES_FILE_PATH = "/attributes.yaml";

	private static final Logger logger = LoggerFactory.getLogger( AttributesProvider.class );

	private static AttributesProvider instance;

	public static AttributesProvider getInstance() {
		if ( instance == null ) {
			instance = new AttributesProvider();
		}
		return instance;
	}

	private final AttributesConfig attributesConfig;

	private AttributesProvider() {
		attributesConfig = readAttributesConfig();
	}

	private AttributesConfig readAttributesConfig() {
		final String userAttributesFilePath = System.getProperty( ATTRIBUTES_FILE_PROPERTY );
		if ( userAttributesFilePath != null ) {
			final File userAttributes = new File( userAttributesFilePath );
			logger.debug( "Loading user-defined attribues file '{}'.", userAttributes );
			return readAttributesConfigFromFile( userAttributes );
		} else {
			final File defaultAttributes = new File( getClass().getResource( DEFAULT_ATTRIBUTES_FILE_PATH ).getPath() );
			logger.debug( "Loading default attributes file '{}'", defaultAttributes );
			return readAttributesConfigFromFile( defaultAttributes );
		}
	}

	private AttributesConfig readAttributesConfigFromFile( final File attributesFile ) {
		final ObjectMapper mapper = new ObjectMapper( new YAMLFactory() );
		try {
			return mapper.readValue( attributesFile, AttributesConfig.class );
		} catch ( final IOException e ) {
			throw new RuntimeException( "Cannot read attributes file '" + attributesFile + "'.", e );
		}
	}

	public List<String> getAttributes() {
		return attributesConfig.getAttributes();
	}

	public List<String> getIdentifyingAttributes() {
		return attributesConfig.getIdentifyingAttributes();
	}

	public List<String> getJoinedAttributes() {
		final List<String> result = new ArrayList<>();
		result.addAll( getIdentifyingAttributes() );
		result.addAll( getAttributes() );
		return result;
	}

	public List<String> getSizes() {
		return attributesConfig.getSizes();
	}

}

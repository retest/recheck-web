package de.retest.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class AttributeProvider {

	public static final String ATTRIBUTES_FILE_PROPERTY = "de.retest.recheck.web.attributesFile";
	public static final String DEFAULT_ATTRIBUTES_FILE_PATH = "/attributes.yaml";

	private static final Logger logger = LoggerFactory.getLogger( AttributeProvider.class );

	private static AttributeProvider instance;

	public static AttributeProvider getInstance() {
		if ( instance == null ) {
			instance = new AttributeProvider();
		}
		return instance;
	}

	private final Attributes attributes;

	private AttributeProvider() {
		attributes = readAttributes();
	}

	private Attributes readAttributes() {
		final String userAttributesFilePath = System.getProperty( ATTRIBUTES_FILE_PROPERTY );
		if ( userAttributesFilePath != null ) {
			final File userAttributes = new File( userAttributesFilePath );
			logger.debug( "Loading user-defined attribues file '{}'.", userAttributesFilePath );
			return readAttributesFromFile( userAttributes );
		} else {
			final File defaultAttributes = new File( getClass().getResource( DEFAULT_ATTRIBUTES_FILE_PATH ).getPath() );
			logger.debug( "Loading default attributes file '{}'", defaultAttributes );
			return readAttributesFromFile( defaultAttributes );
		}
	}

	private Attributes readAttributesFromFile( final File attributesFile ) {
		final ObjectMapper mapper = new ObjectMapper( new YAMLFactory() );
		try {
			return mapper.readValue( attributesFile, Attributes.class );
		} catch ( final IOException e ) {
			throw new RuntimeException( "Cannot read attributes file '{}'.", e );
		}
	}

	public List<String> getAttributes() {
		return attributes.getAttributes();
	}

	public List<String> getIdentifyingAttributes() {
		return attributes.getIdentifyingAttributes();
	}

	public List<String> getJoinedAttributes() {
		final List<String> result = new ArrayList<>();
		result.addAll( getIdentifyingAttributes() );
		result.addAll( getAttributes() );
		return result;
	}

}

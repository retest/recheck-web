package de.retest.web;

import static java.util.Collections.unmodifiableList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeProvider {

	public static final String ATTRIBUTES_FILE_PROPERTY = "de.retest.recheck.web.attributesFile";
	public static final String IDENTIFYING_ATTRIBUTES_FILE_PROPERTY = "de.retest.recheck.web.identifyingAttributesFile";

	private static final Logger logger = LoggerFactory.getLogger( AttributeProvider.class );

	private static AttributeProvider instance;

	public static AttributeProvider getInstance() {
		if ( instance == null ) {
			instance = new AttributeProvider();
		}
		return instance;
	}

	private final List<String> readAttributes;
	private final List<String> readIdentifyingAttributes;

	private AttributeProvider() {
		readAttributes = unmodifiableList( readAttributes( ATTRIBUTES_FILE_PROPERTY, "/attributes.txt" ) );
		readIdentifyingAttributes =
				unmodifiableList( readAttributes( IDENTIFYING_ATTRIBUTES_FILE_PROPERTY, "/identifying.txt" ) );
	}

	private List<String> readAttributes( final String property, final String defaultResource ) {
		final String userFile = System.getProperty( property );
		if ( userFile != null ) {
			try {
				return FileUtils.readLines( new File( userFile ) );
			} catch ( final IOException e ) {
				logger.error( "Exception retrieving configured attributes from file '{}'.", userFile, e );
			}
		}
		return readDefaultAttributes( defaultResource );
	}

	private List<String> readDefaultAttributes( final String defaultResource ) {
		try {
			final InputStream resource = AttributeProvider.class.getResourceAsStream( defaultResource );
			return IOUtils.readLines( resource );
		} catch ( final IOException e ) {
			throw new RuntimeException( "Unable to read file '" + defaultResource + "'.", e );
		}
	}

	public List<String> getAttributes() {
		return readAttributes;
	}

	public List<String> getIdentifyingAttributes() {
		return readIdentifyingAttributes;
	}

	public List<String> getJoinedAttributes() {
		final List<String> result = new ArrayList<>();
		result.addAll( getIdentifyingAttributes() );
		result.addAll( getAttributes() );
		return result;
	}

}

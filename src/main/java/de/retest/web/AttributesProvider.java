package de.retest.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
	private final AttributesConfig attributesConfig;

	private AttributesProvider() {
		attributesConfig = readAttributesConfig();
	}

	public static AttributesProvider getInstance() {
		if ( instance == null ) {
			instance = new AttributesProvider();
		}
		return instance;
	}

	static AttributesProvider getTestInstance() {
		logger.warn( "A test instance of {} is being used.", AttributesProvider.class.getSimpleName() );
		return new AttributesProvider();
	}

	private AttributesConfig readAttributesConfig() {
		final String userAttributesFilePath = System.getProperty( ATTRIBUTES_FILE_PROPERTY );
		if ( userAttributesFilePath != null ) {
			final Path userAttributes = Paths.get( userAttributesFilePath );
			logger.debug( "Loading user-defined attributes file '{}'.", userAttributes );
			try ( final InputStream in = Files.newInputStream( userAttributes ) ) {
				return readAttributesConfigFromFile( in );
			} catch ( final IOException e ) {
				throw new UncheckedIOException( "Cannot read attributes file '" + userAttributesFilePath + "'.", e );
			}
		} else {
			logger.debug( "Loading default attributes file '{}'", DEFAULT_ATTRIBUTES_FILE_PATH );
			try ( final InputStream url = getClass().getResourceAsStream( DEFAULT_ATTRIBUTES_FILE_PATH ) ) {
				return readAttributesConfigFromFile( url );
			} catch ( final IOException e ) {
				throw new UncheckedIOException( "Cannot read attributes file '" + DEFAULT_ATTRIBUTES_FILE_PATH + "'.",
						e );
			}
		}
	}

	private AttributesConfig readAttributesConfigFromFile( final InputStream in ) throws IOException {
		final ObjectMapper mapper = new ObjectMapper( new YAMLFactory() );
		return mapper.readValue( in, AttributesConfig.class );
	}

	/**
	 * @see AttributesConfig#getCssAttributes()
	 */
	public List<String> getCssAttributes() {
		return attributesConfig.getCssAttributes();
	}

	/**
	 * @see AttributesConfig#getHtmlAttributes()
	 */
	public List<String> getHtmlAttributes() {
		return attributesConfig.getHtmlAttributes();
	}

	/**
	 * @see AttributesConfig#allHtmlAttributes()
	 */
	public boolean allHtmlAttributes() {
		return attributesConfig.allHtmlAttributes();
	}

}

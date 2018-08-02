package de.retest.web;

import java.awt.Rectangle;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebData {

	private static final Logger logger = LoggerFactory.getLogger( WebData.class );

	private final Map<String, Object> webData;

	public WebData( final Map<String, Object> webData ) {
		this.webData = webData;
	}

	public String get( final String key ) {
		final Object result = webData.get( key );
		if ( result == null ) {
			return null;
		}
		if ( result instanceof String ) {
			return normalize( (String) result );
		}
		throw new RuntimeException(
				"Attribute value for " + key + " was of " + result.getClass() + ", not of type String." );
	}

	protected static String normalize( final String value ) {
		if ( value == null ) {
			return value;
		}
		String result = value;
		if ( result.startsWith( "\"" ) && result.endsWith( "\"" ) ) {
			result = result.substring( 1, result.length() - 1 );
		}
		return result.trim();
	}

	public Rectangle getOutline() {
		if ( webData.get( AttributesConfig.X ) == null || webData.get( AttributesConfig.Y ) == null //
				|| webData.get( AttributesConfig.WIDTH ) == null || webData.get( AttributesConfig.HEIGHT ) == null ) {
			return null;
		}
		try {
			final int x = toInt( webData.get( AttributesConfig.X ) );
			final int y = toInt( webData.get( AttributesConfig.Y ) );
			final int width = toInt( webData.get( AttributesConfig.WIDTH ) );
			final int height = toInt( webData.get( AttributesConfig.HEIGHT ) );
			return new Rectangle( x, y, width, height );
		} catch ( final Exception e ) {
			logger.error( "Exception retrieving outline: ", e );
		}
		return null;
	}

	private int toInt( final Object value ) throws NumberFormatException {
		if ( value instanceof Integer ) {
			return (Integer) value;
		}
		if ( value instanceof String ) {
			return Integer.parseInt( (String) value );
		}
		if ( value instanceof Double ) {
			return Math.toIntExact( Math.round( (Double) value ) );
		}
		if ( value instanceof Long ) {
			return Math.toIntExact( Math.round( (Long) value ) );
		}
		throw new IllegalArgumentException( "Don't know how to convert a " + value.getClass() + " to int!" );
	}
}

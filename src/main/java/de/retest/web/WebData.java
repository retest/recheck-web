package de.retest.web;

import static de.retest.web.ScreenshotProvider.SCALE;

import java.awt.Rectangle;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebData {

	private static final Logger logger = LoggerFactory.getLogger( WebData.class );

	private final Map<String, Object> wrappedData;

	public WebData( final Map<String, Object> wrappedData ) {
		this.wrappedData = wrappedData;
	}

	/**
	 * @param key
	 *            the data key
	 * @return the string value
	 */
	public String getAsString( final String key ) {
		final Object result = wrappedData.get( key );
		if ( result == null ) {
			return null;
		}
		final String value = String.valueOf( result );
		return key.equals( AttributesConfig.TEXT ) ? value : normalize( value );
	}

	public Set<String> getKeys() {
		return wrappedData.keySet();
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

	public Rectangle getAbsoluteOutline() {
		if ( wrappedData.get( AttributesConfig.ABSOLUTE_X ) == null
				|| wrappedData.get( AttributesConfig.ABSOLUTE_Y ) == null
				|| wrappedData.get( AttributesConfig.ABSOLUTE_WIDTH ) == null
				|| wrappedData.get( AttributesConfig.ABSOLUTE_HEIGHT ) == null ) {
			return null;
		}
		try {
			final int x = getAsInt( AttributesConfig.ABSOLUTE_X );
			final int y = getAsInt( AttributesConfig.ABSOLUTE_Y );
			final int width = getAsInt( AttributesConfig.ABSOLUTE_WIDTH );
			final int height = getAsInt( AttributesConfig.ABSOLUTE_HEIGHT );
			return new Rectangle( x, y, width, height );
		} catch ( final Exception e ) {
			logger.error( "Exception retrieving outline: ", e );
		}
		return null;
	}

	public Rectangle getOutline() {
		if ( wrappedData.get( AttributesConfig.X ) == null //
				|| wrappedData.get( AttributesConfig.Y ) == null //
				|| wrappedData.get( AttributesConfig.WIDTH ) == null
				|| wrappedData.get( AttributesConfig.HEIGHT ) == null ) {
			return null;
		}
		try {
			final int x = getAsInt( AttributesConfig.X ) / SCALE;
			final int y = getAsInt( AttributesConfig.Y ) / SCALE;
			final int width = getAsInt( AttributesConfig.WIDTH ) / SCALE;
			final int height = getAsInt( AttributesConfig.HEIGHT ) / SCALE;
			return new Rectangle( x, y, width, height );
		} catch ( final Exception e ) {
			logger.error( "Exception retrieving outline: ", e );
		}
		return null;
	}

	/**
	 * @param key
	 *            the data key
	 * @return the int value
	 * @throws ConversionException
	 *             if the value for the given key cannot be converted to int
	 */
	public int getAsInt( final String key ) {
		final Object value = wrappedData.get( key );
		try {
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
				return Math.toIntExact( (Long) value );
			}
		} catch ( final Exception e ) {
			throw new ConversionException(
					"Converting " + value + " of type " + value.getClass() + " to int caused an exception!", e );
		}
		throw new ConversionException( "Don't know how to convert " + value + " of "
				+ (value != null ? value.getClass() : "null") + " to int!" );
	}

	public boolean isShown() {
		final Boolean shown = (Boolean) wrappedData.get( "shown" );
		if ( shown != null && !shown ) {
			return false;
		}
		final Rectangle outline = getOutline();
		if ( outline == null ) {
			return false;
		}
		return true;
	}

	public String getTag() {
		return getAsString( AttributesConfig.TAG_NAME );
	}

	public String getText() {
		return getAsString( AttributesUtil.TEXT );
	}

}

package de.retest.web.mapping;

import static de.retest.web.screenshot.ScreenshotProviders.SCALE;

import java.awt.Rectangle;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.web.AttributesUtil;
import de.retest.web.ConversionException;

/**
 * Mapping of key-value pairs for attributes:
 *
 * <pre>
 * {
 *   attribute0 = value0,
 *   attribute1 = value1,
 *   ...
 * }
 * </pre>
 */
public class WebData {

	private static final Logger logger = LoggerFactory.getLogger( WebData.class );

	private final Map<String, Object> wrappedData;
	private final int scale;

	public WebData( final Map<String, Object> wrappedData ) {
		this( wrappedData, SCALE );
	}

	public WebData( final Map<String, Object> wrappedData, final int scale ) {
		this.wrappedData = wrappedData;
		this.scale = scale;
	}

	/**
	 * @param key
	 *            the attribute key
	 * @return the attribute value as string
	 */
	public String getAsString( final String key ) {
		final Object result = wrappedData.get( key );
		if ( result == null ) {
			return null;
		}
		final String value = String.valueOf( result );
		return key.equals( AttributesUtil.TEXT ) ? value : normalize( value );
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
		if ( wrappedData.get( AttributesUtil.ABSOLUTE_X ) == null
				|| wrappedData.get( AttributesUtil.ABSOLUTE_Y ) == null
				|| wrappedData.get( AttributesUtil.ABSOLUTE_WIDTH ) == null
				|| wrappedData.get( AttributesUtil.ABSOLUTE_HEIGHT ) == null ) {
			return null;
		}
		try {
			final int x = getAsInt( AttributesUtil.ABSOLUTE_X );
			final int y = getAsInt( AttributesUtil.ABSOLUTE_Y );
			final int width = getAsInt( AttributesUtil.ABSOLUTE_WIDTH );
			final int height = getAsInt( AttributesUtil.ABSOLUTE_HEIGHT );
			return new Rectangle( x, y, width, height );
		} catch ( final Exception e ) {
			logger.error( "Exception retrieving outline: ", e );
		}
		return null;
	}

	public Rectangle getOutline() {
		if ( wrappedData.get( AttributesUtil.X ) == null //
				|| wrappedData.get( AttributesUtil.Y ) == null //
				|| wrappedData.get( AttributesUtil.WIDTH ) == null
				|| wrappedData.get( AttributesUtil.HEIGHT ) == null ) {
			return null;
		}
		try {
			final int x = getAsInt( AttributesUtil.X ) / scale;
			final int y = getAsInt( AttributesUtil.Y ) / scale;
			final int width = getAsInt( AttributesUtil.WIDTH ) / scale;
			final int height = getAsInt( AttributesUtil.HEIGHT ) / scale;
			return new Rectangle( x, y, width, height );
		} catch ( final Exception e ) {
			logger.error( "Exception retrieving outline: ", e );
		}
		return null;
	}

	/**
	 * @param key
	 *            the attribute key
	 * @return the attribute value as integer
	 * @throws ConversionException
	 *             if the value for the given key cannot be converted to an integer
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

	public boolean isPseudo() {
		final Boolean pseudo = (Boolean) wrappedData.get( "pseudo" );
		return pseudo != null && pseudo;
	}

	public String getTag() {
		return getAsString( AttributesUtil.TAG_NAME );
	}

	public String getText() {
		return getAsString( AttributesUtil.TEXT );
	}

}

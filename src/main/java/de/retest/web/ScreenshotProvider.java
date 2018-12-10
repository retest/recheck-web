package de.retest.web;

import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;

import org.apache.commons.lang3.SystemUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;

public class ScreenshotProvider {

	private static final int SCROLL_TIMEOUT = 100;
	private static final boolean USE_DEVICE_PIXEL_RATIO = true;

	private static final Logger logger = LoggerFactory.getLogger( ScreenshotProvider.class );

	private ScreenshotProvider() {
		// private constructor for util class
	}

	public static BufferedImage shootFullPage( final WebDriver driver ) {
		final BufferedImage image;
		if ( driver instanceof ChromeDriver ) {
			image = Shutterbug
					.shootPage( driver, ScrollStrategy.WHOLE_PAGE_CHROME, SCROLL_TIMEOUT, USE_DEVICE_PIXEL_RATIO )
					.getImage();
			final int scale = extractScale();
			return resize( image, image.getWidth() / scale, image.getHeight() / scale );
		} else {
			image = Shutterbug
					.shootPage( driver, ScrollStrategy.BOTH_DIRECTIONS, SCROLL_TIMEOUT, USE_DEVICE_PIXEL_RATIO )
					.getImage();
		}
		return image;
	}

	// TODO Remove with retest-model version 5.1.0 and use ImageUtils instead.
	public static BufferedImage resize( final BufferedImage image, final int width, final int height ) {
		final Image tmp = image.getScaledInstance( width, height, Image.SCALE_SMOOTH );
		final BufferedImage resized = new BufferedImage( width, height, BufferedImage.TYPE_INT_ARGB );
		final Graphics2D graphics2D = resized.createGraphics();
		graphics2D.drawImage( tmp, 0, 0, null );
		graphics2D.dispose();
		return resized;
	}

	// TODO Remove with retest-model version 5.1.0 and use ImageUtils instead.
	public static int extractScale() {
		final int defaultScale = 1;
		if ( !GraphicsEnvironment.isHeadless() && SystemUtils.IS_OS_MAC ) {
			final GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			final GraphicsDevice device = environment.getDefaultScreenDevice();
			try {
				final Field scale = device.getClass().getDeclaredField( "scale" );
				if ( scale != null ) {
					scale.setAccessible( true );
					return (Integer) scale.get( device );
				}
			} catch ( final Exception e ) {
				logger.error( "Unable to get the scale from the graphic environment", e );
			}
		}
		return defaultScale;
	}
}

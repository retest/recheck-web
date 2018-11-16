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

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;

public class ScreenshotProvider {

	private static final int SCROLL_TIMEOUT = 100;
	private static final boolean USE_DEVICE_PIXEL_RATIO = true;
	private static final int RETINA_SCALE_FACTOR = 2;

	private ScreenshotProvider() {
		// private constructor for util class
	}

	public static BufferedImage shootFullPage( final WebDriver driver ) {
		if ( driver instanceof ChromeDriver ) {
			return Shutterbug
					.shootPage( driver, ScrollStrategy.WHOLE_PAGE_CHROME, SCROLL_TIMEOUT, USE_DEVICE_PIXEL_RATIO )
					.getImage();
		} else {
			return Shutterbug
					.shootPage( driver, ScrollStrategy.BOTH_DIRECTIONS, SCROLL_TIMEOUT, USE_DEVICE_PIXEL_RATIO )
					.getImage();
		}
	public static boolean isRetinaDisplay() {
		if ( SystemUtils.IS_OS_MAC ) {
			final GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
			final GraphicsDevice device = environment.getDefaultScreenDevice();
			try {
				final Field field = device.getClass().getDeclaredField( "scale" );
				if ( field != null ) {
					field.setAccessible( true );
					final Object scale = field.get( device );
					if ( ((Integer) scale).intValue() == RETINA_SCALE_FACTOR ) {
						return true;
					}
				}
			} catch ( final Exception ignore ) {}
		}
		return false;
	}
}

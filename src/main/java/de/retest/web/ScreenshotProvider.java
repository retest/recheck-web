package de.retest.web;

import static de.retest.recheck.ui.image.ImageUtils.extractScale;
import static de.retest.recheck.ui.image.ImageUtils.resizeImage;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;

public class ScreenshotProvider {

	private static final String VIEWPORT_ONLY_SCREENSHOT_PROPERTY = "de.retest.recheck.web.viewportOnlyScreenshot";
	private static final int SCROLL_TIMEOUT_MS = 100;
	private static final boolean USE_DEVICE_PIXEL_RATIO = true;

	public static final int SCALE = extractScale();

	private static final Logger logger = LoggerFactory.getLogger( ScreenshotProvider.class );

	private ScreenshotProvider() {}

	public static BufferedImage shoot( final WebDriver driver, final WebElement element ) {
		try {
			if ( element != null ) {
				return shootElement( driver, element );
			}
			final boolean viewportOnly = Boolean.getBoolean( VIEWPORT_ONLY_SCREENSHOT_PROPERTY );
			return viewportOnly ? shootViewportOnly( driver ) : shootFullPage( driver );
		} catch ( final Exception e ) {
			logger.error( "Exception creating screenshot for check.", e );
			return null;
		}
	}

	private static BufferedImage shootElement( final WebDriver driver, final WebElement element ) {
		return Shutterbug.shootElement( driver, element, USE_DEVICE_PIXEL_RATIO ).getImage();
	}

	private static BufferedImage shootFullPage( final WebDriver driver ) {
		if ( driver instanceof ChromeDriver ) {
			final BufferedImage image = Shutterbug //
					.shootPage( driver, ScrollStrategy.WHOLE_PAGE_CHROME, SCROLL_TIMEOUT_MS, USE_DEVICE_PIXEL_RATIO ) //
					.getImage();
			return resizeImage( image, image.getWidth() / SCALE, image.getHeight() / SCALE );
		}
		return Shutterbug //
				.shootPage( driver, ScrollStrategy.BOTH_DIRECTIONS, SCROLL_TIMEOUT_MS, USE_DEVICE_PIXEL_RATIO ) //
				.getImage();
	}

	private static BufferedImage shootViewportOnly( final WebDriver driver ) {
		return Shutterbug.shootPage( driver, USE_DEVICE_PIXEL_RATIO ).getImage();
	}

}

package de.retest.web;

import static de.retest.recheck.ui.image.ImageUtils.extractScale;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.assertthat.selenium_shutterbug.core.Shutterbug;

import de.retest.web.screenshot.FullPageScreenshot;
import de.retest.web.screenshot.ScreenshotProvider;
import de.retest.web.screenshot.ViewportOnlyScreenshot;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScreenshotProviders {

	private static final String SCREENSHOT_PROVIDER_PROPERTY = "de.retest.recheck.web.screenshotProvider";
	private static final boolean USE_DEVICE_PIXEL_RATIO = true;
	private static final Logger logger = LoggerFactory.getLogger( ScreenshotProviders.class );

	public static final int SCALE = extractScale();

	private ScreenshotProviders() {}

	public static BufferedImage shoot( final WebDriver driver, final WebElement element,
			final ScreenshotProvider screenshotProvider ) {
		try {
			if ( element != null ) {
				return shootElement( driver, element );
			}
			return screenshotProvider.shoot( driver );
		} catch ( final Exception e ) {
			logger.error( "Exception creating screenshot for check.", e );
			return null;
		}
	}

	private static BufferedImage shootElement( final WebDriver driver, final WebElement element ) {
		return Shutterbug.shootElement( driver, element, USE_DEVICE_PIXEL_RATIO ).getImage();
	}

	public static ScreenshotProvider getGlobalScreenshotProvider() {
		switch ( System.getProperty( SCREENSHOT_PROVIDER_PROPERTY, "viewPortOnly" ) ) {
			case "fullPage":
				return new FullPageScreenshot();
			case "viewPortOnly":
				return new ViewportOnlyScreenshot();
			default:
				log.warn( "Global property does not match a correct entry. Using default value (viewPortOnly)." );
				return new ViewportOnlyScreenshot();
		}
	}
}

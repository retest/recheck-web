package de.retest.web.screenshot;

import static de.retest.recheck.ui.image.ImageUtils.extractScale;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.assertthat.selenium_shutterbug.core.Shutterbug;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScreenshotProviders {

	private static final String SCREENSHOT_PROVIDER_PROPERTY = "de.retest.recheck.web.screenshot.provider";
	private static final boolean USE_DEVICE_PIXEL_RATIO = true;

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
			log.error( "Exception creating screenshot for check.", e );
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
			case "none":
				return new NoScreenshot();
			default:
				log.warn( "Global property does not match a correct entry. Using default value (viewPortOnly)." );
				return new ViewportOnlyScreenshot();
		}
	}
}

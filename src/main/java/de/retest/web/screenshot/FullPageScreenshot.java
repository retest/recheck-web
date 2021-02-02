package de.retest.web.screenshot;

import static de.retest.recheck.ui.image.ImageUtils.resizeImage;
import static de.retest.web.screenshot.ScreenshotProviders.SCALE;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.assertthat.selenium_shutterbug.core.Capture;
import com.assertthat.selenium_shutterbug.core.Shutterbug;

/**
 * Scrolls to the bottom of the page and takes a screenshot of the entire page.
 */
public class FullPageScreenshot implements ScreenshotProvider {

	private static final int SCROLL_TIMEOUT_MS = 100;
	private static final boolean USE_DEVICE_PIXEL_RATIO = true;

	@Override
	public BufferedImage shoot( final WebDriver driver ) {
		if ( driver instanceof ChromeDriver ) {
			final BufferedImage image = Shutterbug //
					.shootPage( driver, Capture.FULL, SCROLL_TIMEOUT_MS, USE_DEVICE_PIXEL_RATIO ) //
					.getImage();
			return resizeImage( image, image.getWidth() / SCALE, image.getHeight() / SCALE );
		}
		return Shutterbug //
				.shootPage( driver, Capture.FULL, SCROLL_TIMEOUT_MS, USE_DEVICE_PIXEL_RATIO ) //
				.getImage();
	}

	@Override
	public BufferedImage shoot( final WebDriver driver, final WebElement element ) {
		return Shutterbug.shootElement( driver, element, USE_DEVICE_PIXEL_RATIO ).getImage();
	}

}

package de.retest.web;

import static de.retest.recheck.ui.image.ImageUtils.resizeImage;
import static de.retest.web.ScreenshotProviders.SCALE;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import com.assertthat.selenium_shutterbug.utils.web.ScrollStrategy;

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
					.shootPage( driver, ScrollStrategy.WHOLE_PAGE_CHROME, SCROLL_TIMEOUT_MS, USE_DEVICE_PIXEL_RATIO ) //
					.getImage();
			return resizeImage( image, image.getWidth() / SCALE, image.getHeight() / SCALE );
		}
		return Shutterbug //
				.shootPage( driver, ScrollStrategy.BOTH_DIRECTIONS, SCROLL_TIMEOUT_MS, USE_DEVICE_PIXEL_RATIO ) //
				.getImage();
	}

}

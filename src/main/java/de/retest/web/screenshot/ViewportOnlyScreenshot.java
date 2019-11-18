package de.retest.web.screenshot;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;

import com.assertthat.selenium_shutterbug.core.Shutterbug;

/**
 * The default implementation that takes a screenshot of only the viewport visible to the user from the top page.
 */
public class ViewportOnlyScreenshot implements ScreenshotProvider {

	private static final boolean USE_DEVICE_PIXEL_RATIO = true;

	@Override
	public BufferedImage shoot( final WebDriver driver ) {
		return Shutterbug.shootPage( driver, USE_DEVICE_PIXEL_RATIO ).getImage();
	}

}

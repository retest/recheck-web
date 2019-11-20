package de.retest.web.screenshot;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;

/**
 * Does not take any screenshot at all. Increases the performance. Cannot be set globally.
 */
public class NoScreenshot implements ScreenshotProvider {

	@Override
	public BufferedImage shoot( final WebDriver driver ) {
		return null;
	}

}

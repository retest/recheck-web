package de.retest.web;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;

/**
 * Not implemented yet. Does not take any screenshot at all. Increases the performance. Only used for tests at the
 * moment.
 */
public class NoScreenshot implements ScreenshotProvider {

	@Override
	public BufferedImage shoot( final WebDriver driver ) {
		return null;
	}

}

package de.retest.web;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;

/**
 * The screenshot provider for way screenshots are taken.
 *
 * The ScreenshotProvider can choose whether screenshots are taken for the full page or just the viewport. The Default
 * implementation is {@link ViewPortOnlyScreenshot}.
 */
public interface ScreenshotProvider {

	/**
	 * Takes the screenshot as stated.
	 *
	 * @return screenshot image of the tested page.
	 */
	BufferedImage shoot( WebDriver driver );

}

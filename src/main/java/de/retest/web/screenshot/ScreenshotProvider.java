package de.retest.web.screenshot;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;

/**
 * The screenshot provider for way screenshots are taken.
 *
 * The ScreenshotProvider can choose whether screenshots are taken for the full page, just the viewport or none at all.
 * The Default implementation is {@link ViewportOnlyMinimalScreenshot}.
 */
public interface ScreenshotProvider {

	/**
	 * Takes the screenshot from the {@link WebDriver}.
	 *
	 * @param driver
	 *            Webdriver for taking the screenshot.
	 * @return screenshot image of the tested page. May return {@code null} if screenshots are not supported.
	 * @throws Exception
	 *             If an exception occurred during screenshot creation.
	 */
	BufferedImage shoot( WebDriver driver ) throws Exception;

}

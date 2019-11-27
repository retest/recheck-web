package de.retest.web.screenshot;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;

import com.assertthat.selenium_shutterbug.core.Shutterbug;

import de.retest.recheck.ui.image.ImageUtils;

/**
 * The default implementation that takes a screenshot of only the viewport visible to the user from the top page.
 */
public class ViewportOnlyMinimalScreenshot implements ScreenshotProvider {

	static final int DEFAULT_WANTED_WIDTH_PX = 800;
	private static final String RESIZE_MAX_WIDTH_PX = "de.retest.recheck.web.resizeMaxWidthPx";
	private static final int WANTED_WIDTH = Integer.getInteger( RESIZE_MAX_WIDTH_PX, DEFAULT_WANTED_WIDTH_PX );

	private static final boolean USE_DEVICE_PIXEL_RATIO = true;

	@Override
	public BufferedImage shoot( final WebDriver driver ) {
		return resizeImage( Shutterbug.shootPage( driver, USE_DEVICE_PIXEL_RATIO ).getImage() );
	}

	public static BufferedImage resizeImage( final BufferedImage image ) {
		if ( image.getWidth() <= WANTED_WIDTH ) {
			return image;
		}
		final double height = image.getHeight() * ((double) WANTED_WIDTH / image.getWidth());
		return ImageUtils.resizeImage( image, WANTED_WIDTH, (int) height );
	}

}

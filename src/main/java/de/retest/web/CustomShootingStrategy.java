package de.retest.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import ru.yandex.qatools.ashot.shooting.ImageReadException;
import ru.yandex.qatools.ashot.shooting.SimpleShootingStrategy;

public class CustomShootingStrategy extends SimpleShootingStrategy {

	private static final long serialVersionUID = 1L;

	@Override
	public BufferedImage getScreenshot( final WebDriver wd ) {
		ByteArrayInputStream imageArrayStream = null;
		/*
		 * Original code:
		 *
		 * TakesScreenshot takesScreenshot = (TakesScreenshot) new Augmenter().augment(wd);
		 *
		 * TODO It is entirely possible this works only for retina displays or only for Mac or only for Chrome or
		 * whatever...
		 */
		final TakesScreenshot takesScreenshot = (TakesScreenshot) wd;
		try {
			imageArrayStream = new ByteArrayInputStream( takesScreenshot.getScreenshotAs( OutputType.BYTES ) );
			return ImageIO.read( imageArrayStream );
		} catch ( final IOException e ) {
			throw new ImageReadException( "Can not parse screenshot data.", e );
		} finally {
			IOUtils.closeQuietly( imageArrayStream );
		}
	}
}

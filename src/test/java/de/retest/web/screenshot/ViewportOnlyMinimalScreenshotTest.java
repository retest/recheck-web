package de.retest.web.screenshot;

import static de.retest.web.screenshot.ViewportOnlyMinimalScreenshot.DEFAULT_WANTED_WIDTH_PX;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

class ViewportOnlyMinimalScreenshotTest {

	@Test
	void image_should_be_downscaled_to_maxwidth() {
		final BufferedImage big = new BufferedImage( 3000, 3000, BufferedImage.TYPE_INT_RGB );
		final BufferedImage result = ViewportOnlyMinimalScreenshot.resizeImage( big );

		assertThat( result.getWidth() ).isEqualTo( DEFAULT_WANTED_WIDTH_PX );
		assertThat( result.getHeight() ).isEqualTo( DEFAULT_WANTED_WIDTH_PX );
	}

	@Test
	void small_image_should_be_unaffected() {
		final BufferedImage small = new BufferedImage( 300, 300, BufferedImage.TYPE_INT_RGB );
		final BufferedImage result = ViewportOnlyMinimalScreenshot.resizeImage( small );

		assertThat( result.getWidth() ).isEqualTo( 300 );
		assertThat( result.getHeight() ).isEqualTo( 300 );
	}

}

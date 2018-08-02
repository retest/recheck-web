package de.retest.web;

import java.awt.image.BufferedImage;

import de.retest.ui.descriptors.IdentifyingAttributes;
import de.retest.ui.descriptors.MutableAttributes;
import de.retest.ui.descriptors.RootElement;
import de.retest.ui.image.ImageUtils;

public class RootElementPeer extends WebElementPeer {

	private static final String SCREENSHOT_PREFIX = "window";

	private final String title;
	private final BufferedImage screenshot;

	public RootElementPeer( final WebData webData, final String path, final String title,
			final BufferedImage screenshot ) {
		super( webData, path );
		this.screenshot = screenshot;
		this.title = title;
	}

	@Override
	public RootElement toElement() {
		final IdentifyingAttributes identifyingAttributes = retrieveIdentifyingAttributes();
		final MutableAttributes state = retrieveStateAttributes();
		return new RootElement( identifyingAttributes, state.immutable(),
				ImageUtils.image2Screenshot( SCREENSHOT_PREFIX, screenshot ), convertChildren(), title, 1, title );
	}

}

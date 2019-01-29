package de.retest.web;

import static de.retest.web.RecheckSeleniumAdapter.idProvider;

import java.awt.image.BufferedImage;

import de.retest.ui.descriptors.Element;
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
	public RootElement toElement( final Element parent ) {
		final IdentifyingAttributes identifyingAttributes = retrieveIdentifyingAttributes();
		final MutableAttributes state = retrieveStateAttributes();
		final RootElement rootElement = new RootElement( idProvider.getRetestId( identifyingAttributes ),
				identifyingAttributes, state.immutable(), ImageUtils.image2Screenshot( SCREENSHOT_PREFIX, screenshot ),
				title, 1, title );
		rootElement.addChildren( convertChildren( rootElement ) );
		return rootElement;
	}

}

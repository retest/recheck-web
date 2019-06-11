package de.retest.web;

import java.awt.image.BufferedImage;

import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.recheck.ui.image.ImageUtils;
import de.retest.recheck.ui.image.Screenshot;
import de.retest.web.mapping.WebData;

public class RootElementPeer extends WebElementPeer {

	private final String title;
	private final BufferedImage screenshot;

	public RootElementPeer( final RetestIdProvider retestIdProvider, final AttributesProvider attributesProvider,
			final WebData webData, final String path, final String title, final BufferedImage screenshot,
			final DefaultValueFinder defaultValueFinder ) {
		super( retestIdProvider, attributesProvider, webData, path, defaultValueFinder );
		this.screenshot = screenshot;
		this.title = title;
	}

	@Override
	public RootElement toElement( final Element parent ) {
		if ( webData == null ) {
			throw new IllegalStateException( "RootElement was not properly initialized!" );
		}
		final IdentifyingAttributes identifyingAttributes = retrieveIdentifyingAttributes();
		final MutableAttributes stateAttributes = retrieveStateAttributes( identifyingAttributes );
		final String retestId = retestIdProvider.getRetestId( identifyingAttributes );
		final Screenshot ss = ImageUtils.image2Screenshot( retestId, screenshot );
		final RootElement rootElement =
				new RootElement( retestId, identifyingAttributes, stateAttributes.immutable(), ss, title, 1, title );
		rootElement.addChildren( convertChildren( rootElement ) );
		return rootElement;
	}

}

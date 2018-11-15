package de.retest.web;

import static de.retest.web.RecheckSeleniumAdapter.idProvider;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import de.retest.ui.Path;
import de.retest.ui.descriptors.Attribute;
import de.retest.ui.descriptors.Element;
import de.retest.ui.descriptors.IdentifyingAttributes;
import de.retest.ui.descriptors.MutableAttributes;
import de.retest.ui.descriptors.OutlineAttribute;
import de.retest.ui.descriptors.PathAttribute;
import de.retest.ui.descriptors.StringAttribute;
import de.retest.ui.descriptors.SuffixAttribute;
import de.retest.ui.descriptors.TextAttribute;

public class WebElementPeer {

	private final DefaultValuesProvider defaults;

	protected final List<WebElementPeer> children = new ArrayList<>();
	protected final WebData webData;
	protected final String path;

	public WebElementPeer( final DefaultValuesProvider defaults, final WebData webData, final String path ) {
		this.defaults = defaults;
		this.webData = webData;
		this.path = path;
	}

	public void addChild( final WebElementPeer child ) {
		children.add( child );
	}

	public Element toElement() {
		if ( webData == null ) {
			return null;
		}
		final List<Element> convertedChildren = convertChildren();
		final IdentifyingAttributes identifyingAttributes = retrieveIdentifyingAttributes();
		final MutableAttributes state = retrieveStateAttributes();
		return new Element( idProvider.getRetestId( identifyingAttributes ), identifyingAttributes, state.immutable(),
				convertedChildren, null );
	}

	protected IdentifyingAttributes retrieveIdentifyingAttributes() {
		final List<Attribute> attributes = new ArrayList<>();

		attributes.add( new PathAttribute( Path.fromString( path ) ) );
		attributes.add( new SuffixAttribute( extractSuffix() ) );
		attributes.add( new StringAttribute( "type", webData.getTag() ) );

		final String text = webData.getAsString( "text" );
		if ( StringUtils.isNotBlank( text ) ) {
			attributes.add( new TextAttribute( "text", text ) );
		}

		final Rectangle outline = webData.getOutline();
		if ( outline != null ) {
			attributes.add( OutlineAttribute.create( outline ) );
		}

		final Rectangle absoluteOutline = webData.getAbsoluteOutline();
		if ( absoluteOutline != null ) {
			attributes.add( OutlineAttribute.createAbsolute( absoluteOutline ) );
		}

		final List<String> userDefinedAttributes =
				new ArrayList<>( AttributesProvider.getInstance().getIdentifyingAttributes() );
		for ( final String attribute : userDefinedAttributes ) {
			final String attributeValue = webData.getAsString( attribute );
			if ( attributeValue != null ) {
				attributes.add( new StringAttribute( attribute, attributeValue ) );
			}
		}

		return new IdentifyingAttributes( attributes );
	}

	public Integer extractSuffix() {
		final String suffix = path.substring( path.lastIndexOf( '[' ) + 1, path.lastIndexOf( ']' ) );
		return Integer.valueOf( suffix );
	}

	protected MutableAttributes retrieveStateAttributes() {
		final MutableAttributes state = new MutableAttributes();
		for ( final String attribute : AttributesProvider.getInstance().getAttributes() ) {
			final String attributeValue = webData.getAsString( attribute );
			if ( attributeValue != null && !defaults.isDefault( webData.getTag(), attribute, attributeValue ) ) {
				state.put( attribute, attributeValue );
			}
		}
		return state;
	}

	protected List<Element> convertChildren() {
		return children.stream() //
				.map( WebElementPeer::toElement ) //
				.filter( Objects::nonNull ) //
				.collect( Collectors.toList() );
	}

	@Override
	public String toString() {
		return path;
	}

	public List<WebElementPeer> getChildren() {
		return children;
	}
}

package de.retest.web;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private static final Logger logger = LoggerFactory.getLogger( WebElementPeer.class );

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
		return new Element( identifyingAttributes, state.immutable(), convertedChildren, null );
	}

	protected IdentifyingAttributes retrieveIdentifyingAttributes() {
		final List<Attribute> attributes = new ArrayList<>();
		attributes.add( new PathAttribute( Path.fromString( path ) ) );
		attributes.add( new SuffixAttribute( path.substring( path.lastIndexOf( '[' ) + 1, path.lastIndexOf( ']' ) ) ) );
		attributes.add( new StringAttribute( "type", webData.getAsString( "tagName" ) ) );
		final String text = webData.getAsString( "text" );
		if ( StringUtils.isNotBlank( text ) ) {
			attributes.add( new TextAttribute( "text", text ) );
		}
		final Rectangle outline = webData.getOutline();
		if ( outline != null ) {
			attributes.add( new OutlineAttribute( outline ) );
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

	protected MutableAttributes retrieveStateAttributes() {
		final MutableAttributes state = new MutableAttributes();
		for ( final String attribute : AttributesProvider.getInstance().getAttributes() ) {
			final String attributeValue = webData.getAsString( attribute );
			if ( attributeValue != null
					&& !defaults.isDefault( webData.getAsString( "tagName" ), attribute, attributeValue ) ) {
				state.put( attribute, attributeValue );
			}
		}
		return state;
	}

	protected List<Element> convertChildren() {
		final List<Element> result = new ArrayList<>();
		for ( int idx = 0; idx < children.size(); idx++ ) {
			final Element element = children.get( idx ).toElement();
			if ( element != null ) {
				result.add( element );
			} else {
				logger.warn( "Element was null: {}.", children.get( idx ).path );
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return path;
	}

	public List<WebElementPeer> getChildren() {
		return children;
	}
}

package de.retest.web;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

	protected final List<WebElementPeer> children = new ArrayList<>();
	protected final Map<String, String> webData;
	protected final String path;

	public WebElementPeer( final Map<String, String> webData, final String path ) {
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
		attributes.add( new StringAttribute( "type", webData.get( "tagName" ) ) );
		attributes.add( new TextAttribute( "text", webData.get( "text" ) ) );
		final List<String> userDefinedAttributes =
				new ArrayList<>( AttributesProvider.getInstance().getIdentifyingAttributes() );
		if ( containsOutline( userDefinedAttributes ) ) {
			userDefinedAttributes.remove( AttributesConfig.X );
			userDefinedAttributes.remove( AttributesConfig.Y );
			userDefinedAttributes.remove( AttributesConfig.WIDTH );
			userDefinedAttributes.remove( AttributesConfig.HEIGHT );
			final OutlineAttribute outline = retrieveOutline();
			if ( outline != null ) {
				attributes.add( outline );
			}
		}
		for ( final String attribute : userDefinedAttributes ) {
			final String attributeValue = webData.get( attribute );
			if ( attributeValue != null ) {
				attributes.add( new StringAttribute( attribute, attributeValue ) );
			}
		}
		return new IdentifyingAttributes( attributes );
	}

	public boolean containsOutline( final List<String> userDefinedAttributes ) {
		return userDefinedAttributes.contains( AttributesConfig.X ) //
				&& userDefinedAttributes.contains( AttributesConfig.Y ) //
				&& userDefinedAttributes.contains( AttributesConfig.WIDTH ) //
				&& userDefinedAttributes.contains( AttributesConfig.HEIGHT );
	}

	public OutlineAttribute retrieveOutline() {
		if ( webData.get( AttributesConfig.X ) == null || webData.get( AttributesConfig.Y ) == null //
				|| webData.get( AttributesConfig.WIDTH ) == null || webData.get( AttributesConfig.HEIGHT ) == null ) {
			return null;
		}
		try {
			final int x = Integer.parseInt( webData.get( AttributesConfig.X ) );
			final int y = Integer.parseInt( webData.get( AttributesConfig.Y ) );
			final int width = Integer.parseInt( webData.get( AttributesConfig.WIDTH ) );
			final int height = Integer.parseInt( webData.get( AttributesConfig.HEIGHT ) );
			return new OutlineAttribute( new Rectangle( x, y, width, height ) );
		} catch ( final Exception e ) {
			logger.error( "Exception retrieving outline: ", e );
		}
		return null;
	}

	protected MutableAttributes retrieveStateAttributes() {
		final MutableAttributes state = new MutableAttributes();
		for ( final String attribute : AttributesProvider.getInstance().getAttributes() ) {
			final String attributeValue = webData.get( attribute );
			if ( attributeValue != null && !isDefault( attributeValue ) ) {
				state.put( attribute, attributeValue );
			}
		}
		return state;
	}

	private boolean isDefault( final String attributeValue ) {
		if ( attributeValue == null || attributeValue.isEmpty() ) {
			return true;
		}
		if ( attributeValue.equals( "auto" ) ) {
			return true;
		}
		if ( attributeValue.equals( "none" ) ) {
			return true;
		}
		if ( attributeValue.equals( "normal" ) ) {
			return true;
		}
		return false;
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

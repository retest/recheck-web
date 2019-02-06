package de.retest.web;

import static de.retest.web.AttributesConfig.isIdentifyingAttribute;
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

	protected final List<WebElementPeer> children = new ArrayList<>();
	protected final WebData webData;
	protected final String path;

	public WebElementPeer( final WebData webData, final String path ) {
		this.webData = webData;
		this.path = path;
	}

	public void addChild( final WebElementPeer child ) {
		children.add( child );
	}

	public Element toElement( final Element parent ) {
		if ( webData == null ) {
			return null;
		}
		final IdentifyingAttributes identifyingAttributes = retrieveIdentifyingAttributes();
		final MutableAttributes state = retrieveStateAttributes();
		final Element element = Element.create( idProvider.getRetestId( identifyingAttributes ), parent,
				identifyingAttributes, state.immutable() );
		element.addChildren( convertChildren( element ) );
		return element;
	}

	protected IdentifyingAttributes retrieveIdentifyingAttributes() {
		final List<Attribute> attributes = new ArrayList<>();

		attributes.add( new PathAttribute( Path.fromString( path ) ) );
		attributes.add( new SuffixAttribute( extractSuffix() ) );
		attributes.add( new StringAttribute( "type", webData.getTag() ) );

		final Rectangle outline = webData.getOutline();
		if ( outline != null ) {
			attributes.add( OutlineAttribute.create( outline ) );
		}

		final Rectangle absoluteOutline = webData.getAbsoluteOutline();
		if ( absoluteOutline != null ) {
			attributes.add( OutlineAttribute.createAbsolute( absoluteOutline ) );
		}

		final List<String> identifyingAttributes = AttributesProvider.getInstance().getHtmlAttributes();
		for ( final String key : identifyingAttributes ) {
			final String value = webData.getAsString( key );
			if ( StringUtils.isNotBlank( value ) ) {
				if ( key.equals( AttributesConfig.TEXT ) ) {
					attributes.add( new TextAttribute( AttributesConfig.TEXT, value ) );
				} else {
					attributes.add( new StringAttribute( key, value ) );
				}
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
		for ( final String key : webData.getKeys() ) {
			final String attributeValue = webData.getAsString( key );
			if ( attributeValue != null && !isIdentifyingAttribute( key ) ) {
				state.put( key, attributeValue );
			}
		}
		return state;
	}

	protected List<Element> convertChildren( final Element parent ) {
		return children.stream() //
				.map( webElementPeer -> webElementPeer.toElement( parent ) ) //
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

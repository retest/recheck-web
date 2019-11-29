package de.retest.web;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.Attribute;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.descriptors.OutlineAttribute;
import de.retest.recheck.ui.descriptors.PathAttribute;
import de.retest.recheck.ui.descriptors.StringAttribute;
import de.retest.recheck.ui.descriptors.SuffixAttribute;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.web.mapping.WebData;
import de.retest.web.util.TextAttributeUtil;

public class WebElementPeer {

	protected final List<WebElementPeer> children = new ArrayList<>();

	protected final RetestIdProvider retestIdProvider;
	protected final WebData webData;

	private final String path;
	private final DefaultValueFinder defaultValueFinder;

	public WebElementPeer( final RetestIdProvider retestIdProvider, final WebData webData, final String path,
			final DefaultValueFinder defaultValueFinder ) {
		this.retestIdProvider = retestIdProvider;
		this.webData = webData;
		this.path = path;
		this.defaultValueFinder = defaultValueFinder;
	}

	public void addChild( final WebElementPeer child ) {
		children.add( child );
	}

	public Element toElement( final Element parent ) {
		if ( webData == null ) {
			return null;
		}
		final IdentifyingAttributes identifyingAttributes = retrieveIdentifyingAttributes();
		final MutableAttributes stateAttributes = retrieveStateAttributes( identifyingAttributes );
		final String retestId = retestIdProvider.getRetestId( identifyingAttributes );
		final Element element = Element.create( retestId, parent, identifyingAttributes, stateAttributes.immutable() );
		element.addChildren( convertChildren( element ) );
		return element;
	}

	protected IdentifyingAttributes retrieveIdentifyingAttributes() {
		final List<Attribute> identifyingAttributes = new ArrayList<>();

		final Rectangle absoluteOutline = webData.getAbsoluteOutline();
		if ( absoluteOutline != null ) {
			identifyingAttributes.add( OutlineAttribute.createAbsolute( absoluteOutline ) );
		}

		final Rectangle outline = webData.getOutline();
		if ( outline != null ) {
			identifyingAttributes.add( OutlineAttribute.create( outline ) );
		}

		final String text = webData.getText();
		if ( StringUtils.isNotBlank( text ) ) {
			identifyingAttributes.add( TextAttributeUtil.createTextAttribute( path, text ) );
		}

		final List<String> htmlAttributes =
				Arrays.asList( AttributesUtil.CLASS, AttributesUtil.ID, AttributesUtil.NAME );
		for ( final String key : htmlAttributes ) {
			final String value = webData.getAsString( key );
			if ( StringUtils.isNotBlank( value ) ) {
				identifyingAttributes.add( new StringAttribute( key, value ) );
			}
		}

		identifyingAttributes.add( new StringAttribute( IdentifyingAttributes.TYPE_ATTRIBUTE_KEY, webData.getTag() ) );
		identifyingAttributes.add( new PathAttribute( Path.fromString( path ) ) );
		identifyingAttributes.add( new SuffixAttribute( extractSuffix() ) );

		return new IdentifyingAttributes( identifyingAttributes );
	}

	private Integer extractSuffix() {
		final String suffix = path.substring( path.lastIndexOf( '[' ) + 1, path.lastIndexOf( ']' ) );
		return Integer.valueOf( suffix );
	}

	protected MutableAttributes retrieveStateAttributes( final IdentifyingAttributes identifyingAttributes ) {
		final MutableAttributes state = new MutableAttributes();
		webData.getKeys().stream() //
				.filter( Objects::nonNull ) //
				.filter( key -> !AttributesUtil.isIdentifyingAttribute( key ) && !defaultValueFinder
						.isDefaultValue( identifyingAttributes, key, webData.getAsString( key ) ) ) //
				.forEach( key -> state.put( key, webData.getAsString( key ) ) );
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

package de.retest.web;

import java.awt.Rectangle;
import java.util.ArrayList;
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
import de.retest.web.util.TextAttributeUtil;

public class WebElementPeer {

	protected final List<WebElementPeer> children = new ArrayList<>();
	protected final WebData webData;
	protected final String path;

	private final DefaultValueFinder defaultValueFinder;

	public WebElementPeer( final WebData webData, final String path, final DefaultValueFinder defaultValueFinder ) {
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
		final String retestId = RecheckSeleniumAdapter.idProvider.getRetestId( identifyingAttributes );
		final Element element = Element.create( retestId, parent, identifyingAttributes, stateAttributes.immutable() );
		element.addChildren( convertChildren( element ) );
		return element;
	}

	protected IdentifyingAttributes retrieveIdentifyingAttributes() {
		// TODO Inconsistent since we don't get all identifying attributes via attributes.yaml.
		final List<Attribute> identifyingAttributes = new ArrayList<>();

		identifyingAttributes.add( new PathAttribute( Path.fromString( path ) ) );
		identifyingAttributes.add( new SuffixAttribute( extractSuffix() ) );
		identifyingAttributes.add( new StringAttribute( "type", webData.getTag() ) );

		final Rectangle outline = webData.getOutline();
		if ( outline != null ) {
			identifyingAttributes.add( OutlineAttribute.create( outline ) );
		}

		final Rectangle absoluteOutline = webData.getAbsoluteOutline();
		if ( absoluteOutline != null ) {
			identifyingAttributes.add( OutlineAttribute.createAbsolute( absoluteOutline ) );
		}

		final List<String> htmlAttributes = AttributesProvider.getInstance().getHtmlAttributes();
		for ( final String key : htmlAttributes ) {
			final String value = webData.getAsString( key );
			if ( StringUtils.isNotBlank( value ) ) {
				if ( key.equals( AttributesConfig.TEXT ) ) {
					identifyingAttributes.add( TextAttributeUtil.createTextAttribute( path, value ) );
				} else {
					identifyingAttributes.add( new StringAttribute( key, value ) );
				}
			}
		}

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
				.filter( AttributesUtil::isStateAttribute ) //
				.filter( key -> !AttributesProvider.getInstance().getHtmlAttributes().contains( key ) ) //
				.filter( key -> !defaultValueFinder.isDefaultValue( identifyingAttributes, key,
						webData.getAsString( key ) ) ) //
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

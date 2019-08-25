package de.retest.web.mapping;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.Attribute;
import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.descriptors.OutlineAttribute;
import de.retest.recheck.ui.descriptors.PathAttribute;
import de.retest.recheck.ui.descriptors.StringAttribute;
import de.retest.recheck.ui.descriptors.SuffixAttribute;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.web.AttributesProvider;
import de.retest.web.AttributesUtil;
import de.retest.web.util.TextAttributeUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebElementConverter {

	private static final Set<String> htmlAttributes = new HashSet<>( Arrays.asList( //
			AttributesUtil.CLASS, //
			AttributesUtil.ID, //
			AttributesUtil.NAME //
	) );

	private final RetestIdProvider retestIdProvider;
	private final AttributesProvider attributesProvider;
	private final DefaultValueFinder defaultValueFinder;

	public Element convert( final Element parent, final WebDataProvider provider ) {
		final IdentifyingAttributes identifyingAttributes = retrieveIdentifyingAttributes( provider );
		final Attributes stateAttributes = retrieveStateAttributes( identifyingAttributes, provider );
		final String retestId = retestIdProvider.getRetestId( identifyingAttributes );
		return Element.create( retestId, parent, identifyingAttributes, stateAttributes );
	}

	private IdentifyingAttributes retrieveIdentifyingAttributes( final WebDataProvider provider ) {
		final List<Attribute> identifyingAttributes = new ArrayList<>();

		final String path = provider.getPath();
		final String tag = provider.getTag();

		identifyingAttributes.add( new StringAttribute( IdentifyingAttributes.TYPE_ATTRIBUTE_KEY, tag ) );
		identifyingAttributes.add( new PathAttribute( Path.fromString( path ) ) );
		identifyingAttributes.add( new SuffixAttribute( extractSuffix( path ) ) );

		final Rectangle absoluteOutline = provider.getAbsoluteOutline();
		if ( absoluteOutline != null ) {
			identifyingAttributes.add( OutlineAttribute.createAbsolute( absoluteOutline ) );
		}

		final Rectangle outline = provider.getOutline();
		if ( outline != null ) {
			identifyingAttributes.add( OutlineAttribute.create( outline ) );
		}

		final String text = provider.getText();
		if ( StringUtils.isNotBlank( text ) ) {
			identifyingAttributes.add( TextAttributeUtil.createTextAttribute( path, text ) );
		}

		provider.getHTMLAttributes() //
				.filter( key -> htmlAttributes.contains( key.getKey() ) ) //
				.map( entry -> new StringAttribute( entry.getKey(), entry.getValue() ) ) //
				.forEach( identifyingAttributes::add );

		return new IdentifyingAttributes( identifyingAttributes );
	}

	private Integer extractSuffix( final String path ) {
		final String suffix = path.substring( path.lastIndexOf( '[' ) + 1, path.lastIndexOf( ']' ) );
		return Integer.valueOf( suffix );
	}

	private Attributes retrieveStateAttributes( final IdentifyingAttributes id, final WebDataProvider provider ) {
		final MutableAttributes state = new MutableAttributes();

		Stream.concat( provider.getHTMLAttributes(), provider.getCSSAttributes() ) //
				.filter( entry -> AttributesUtil.isStateAttribute( entry.getKey(), attributesProvider ) ) //
				.filter( entry -> !defaultValueFinder.isDefaultValue( id, entry.getKey(), entry.getValue() ) ) //
				.forEach( entry -> state.put( entry.getKey(), entry.getValue() ) );

		return state.immutable();
	}
}

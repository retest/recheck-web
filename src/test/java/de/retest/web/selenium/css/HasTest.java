package de.retest.web.selenium.css;

import static de.retest.web.AttributesUtil.CLASS;
import static de.retest.web.AttributesUtil.ID;
import static de.retest.web.AttributesUtil.NAME;
import static de.retest.web.AttributesUtil.TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;

@ExtendWith( MockitoExtension.class )
class HasTest {

	private static final String TYPE = IdentifyingAttributes.TYPE_ATTRIBUTE_KEY;

	@Mock
	private Element element;
	@Mock
	private IdentifyingAttributes identifyingAttribues;
	@Mock
	private Attributes attributes;

	@Test
	void cssAttributes() throws Exception {
		assertAll( createAssert( TYPE, Has::cssTag ), createAssert( NAME, Has::cssName ),
				createAssert( CLASS, Has::cssClass ), createAssert( ID, Has::cssId ) );
	}

	private Executable createAssert( final String attribute, final Function<String, Predicate<Element>> predicate ) {
		final String value = "value";
		when( identifyingAttribues.get( attribute ) ).thenReturn( value );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );
		return () -> assertThat( predicate.apply( value ).test( element ) ).isTrue();
	}

	@Test
	void hasAttributeWithValueContainsSubstring() throws Exception {
		final Function<String, String> quote = string -> "\"" + string + "\"";
		final Function<String, String> beginString = string -> string.substring( 0, 5 );
		final Function<String, String> centerString = string -> string.substring( 2, 5 );
		final Function<String, String> endEscapedString = string -> string.substring( 5 );

		assertAll( //
				assertAttribute( "~", centerString, Has::attributeContaining ), //
				assertAttribute( "|", beginString, Has::attributeStarting ), //
				assertAttribute( "^", beginString.andThen( quote ), Has::attributeBeginning ), //
				assertAttribute( "$", endEscapedString.andThen( quote ), Has::attributeEnding ), //
				assertAttribute( "*", centerString.andThen( quote ), Has::attributeContainingSubstring ) //
		);
	}

	private Executable assertAttribute( final String selectorChar, final Function<String, String> substring,
			final Function<String, Predicate<Element>> has ) {
		final String attributeName = "attributeName";
		final String attributeValue = "attributeValue";
		final String selector = attributeName + selectorChar + "=" + substring.apply( attributeValue );
		when( element.getAttributeValue( attributeName ) ).thenReturn( attributeValue );

		return () -> assertThat( has.apply( selector ).test( element ) ).isTrue();
	}

	@Test
	void hasAttributeWithValue() throws Exception {
		final String attributeName = "attributeName";
		final String attributeValue = "attributeValue";
		final String selector = attributeName + "=" + attributeValue;
		when( element.getAttributeValue( attributeName ) ).thenReturn( attributeValue );

		assertThat( Has.attribute( selector ).test( element ) ).isTrue();
	}

	@Test
	void hasAttributeWithEscapedValue() throws Exception {
		final String attributeName = "attributeName";
		final String attributeValue = "attributeValue";
		final String selector = attributeName + "=\"" + attributeValue + "\"";
		when( element.getAttributeValue( attributeName ) ).thenReturn( attributeValue );

		assertThat( Has.attribute( selector ).test( element ) ).isTrue();
	}

	@Test
	void hasAttribute() throws Exception {
		final String attributeName = "attributeName";
		final String selector = attributeName;
		when( element.getAttributeValue( attributeName ) ).thenReturn( "true" );

		assertThat( Has.attribute( selector ).test( element ) ).isTrue();
	}

	@Test
	void hasLinkTextAsAttribute() throws Exception {
		final String value = "value";
		when( identifyingAttribues.getType() ).thenReturn( "a" );
		when( attributes.get( TEXT ) ).thenReturn( value );
		when( element.getAttributes() ).thenReturn( attributes );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );

		assertThat( Has.linkText( value ).test( element ) ).isTrue();
	}

	@Test
	void hasLinkTextAsIdentifyingAttribute() throws Exception {
		final String value = "value";
		when( identifyingAttribues.get( TEXT ) ).thenReturn( value );
		when( identifyingAttribues.getType() ).thenReturn( "not a" );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );

		assertThat( Has.linkText( value ).test( element ) ).isTrue();
	}

	@Test
	void hasPartialLinkText() throws Exception {
		final String partialLinkText = "partial link";
		final String linkText = partialLinkText + "prefix";
		when( identifyingAttribues.getType() ).thenReturn( "a" );
		when( element.getAttributeValue( TEXT ) ).thenReturn( linkText );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );

		assertThat( Has.partialLinkText( partialLinkText ).test( element ) ).isEqualTo( true );
	}

	@Test
	void hasNoPartialLinkText() throws Exception {
		final String partialLinkText = "partial link";
		final String linkText = partialLinkText + "prefix";
		final String notMatchingLink = "not matching";
		when( identifyingAttribues.getType() ).thenReturn( "a" );
		when( element.getAttributeValue( TEXT ) ).thenReturn( linkText );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );

		assertThat( Has.partialLinkText( notMatchingLink ).test( element ) ).isEqualTo( false );
	}
}

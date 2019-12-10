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

import org.assertj.core.api.AbstractBooleanAssert;
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
				assertAttribute( "^", beginString.andThen( quote ), Has::attributeBeginning ), //
				assertAttribute( "$", endEscapedString.andThen( quote ), Has::attributeEnding ), //
				assertAttribute( "*", centerString.andThen( quote ), Has::attributeContainingSubstring ) //
		);
	}

	@Test
	void hasAttributeWithValueContainingWord() throws Exception {
		final Function<String, Predicate<Element>> has = Has::attributeContaining;
		final String selectorChar = "~";
		final String attributeName = "attributeName";
		final String wordSeparator = " ";
		final String prefix = "at";
		final String word = "tribe";
		final String suffix = "uteValue";
		final String matchingValue = prefix + wordSeparator + word + wordSeparator + suffix;
		final String notMatchingValue = prefix + word + suffix;
		final String selector = attributeName + selectorChar + "=" + word;
		when( element.getAttributeValue( attributeName ) ).thenReturn( matchingValue );
		assertThat( has.apply( selector ).test( element ) ).isTrue();
		when( element.getAttributeValue( attributeName ) ).thenReturn( notMatchingValue );
		assertThat( has.apply( selector ).test( element ) ).isFalse();
	}

	@Test
	void hasAttributeWithValueStartingWord() throws Exception {
		final String wordSeparator = "-";
		final String prefix = "prefix";
		final String word = "word";
		final String suffix = "suffix";
		assertAll( //
				() -> assertWord( Has::attributeStarting, "|", word, word ).isTrue(), //
				() -> assertWord( Has::attributeStarting, "|", word, word + wordSeparator + suffix ).isTrue(), //
				() -> assertWord( Has::attributeStarting, "|", word, word + suffix ).isFalse(), //
				() -> assertWord( Has::attributeStarting, "|", word, prefix + word + wordSeparator + suffix ).isFalse() //
		);
	}

	private AbstractBooleanAssert<?> assertWord( final Function<String, Predicate<Element>> has,
			final String selectorChar, final String word, final String matchingValue ) {
		final String attributeName = "attributeName";
		final String selector = attributeName + selectorChar + "=" + word;
		when( element.getAttributeValue( attributeName ) ).thenReturn( matchingValue );
		return assertThat( has.apply( selector ).test( element ) );
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

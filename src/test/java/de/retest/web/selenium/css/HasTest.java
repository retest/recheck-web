package de.retest.web.selenium.css;

import static de.retest.web.AttributesUtil.CLASS;
import static de.retest.web.AttributesUtil.ID;
import static de.retest.web.AttributesUtil.NAME;
import static de.retest.web.AttributesUtil.TEXT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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

	@MethodSource( "basicCssAttributes" )
	@ParameterizedTest
	void should_use_identifying_attributes_in_predicate( final String attribute, final PredicateFactory predicate ) {
		final String value = "value";
		when( identifyingAttribues.get( attribute ) ).thenReturn( value );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );

		assertThat( predicate.create( value ).test( element ) ).isTrue();
	}

	private static Stream<Arguments> basicCssAttributes() {
		return Stream.of( //
				Arguments.of( TYPE, (PredicateFactory) Has::cssTag ), //
				Arguments.of( NAME, (PredicateFactory) Has::cssName ), //
				Arguments.of( CLASS, (PredicateFactory) Has::cssClass ), //
				Arguments.of( ID, (PredicateFactory) Has::cssId ) //
		);
	}

	@MethodSource( "attributeMatchers" )
	@ParameterizedTest
	void should_match_attribute_with_value_containing_substring( final String selectorChar, final String substring,
			final PredicateFactory has ) {
		final String attributeName = "attributeName";
		final String attributeValue = "attributeValue";
		final String selector = attributeName + selectorChar + "=\"" + substring + "\"";
		when( element.getAttributeValue( attributeName ) ).thenReturn( attributeValue );

		assertThat( has.create( selector ).test( element ) ).isTrue();
	}

	private static Stream<Arguments> attributeMatchers() {
		return Stream.of( //
				Arguments.of( "^", "attribute", (PredicateFactory) Has::attributeBeginning ), //
				Arguments.of( "$", "Value", (PredicateFactory) Has::attributeEnding ), //
				Arguments.of( "*", "buteVal", (PredicateFactory) Has::attributeContainingSubstring ) //
		);
	}

	@MethodSource( "attributeMatchersContainingWord" )
	@ParameterizedTest( name = "[{index}] using selector ''{0}'', to match ''{1}'', expecting {2}" )
	void should_match_attribute_with_value_containing_word( final String selector, final String value,
			final boolean expectedResult ) throws Exception {
		when( element.getAttributeValue( "attributeName" ) ).thenReturn( value );
		assertThat( Has.attributeContaining( selector ).test( element ) ).isEqualTo( expectedResult );
	}

	private static Stream<Arguments> attributeMatchersContainingWord() {
		final String selectorChar = "~";
		final String attributeName = "attributeName";
		final String wordSeparator = " ";
		final String prefix = "at";
		final String word = "tribe";
		final String suffix = "uteValue";
		final String matchingValue = prefix + wordSeparator + word + wordSeparator + suffix;
		final String notMatchingValue = prefix + word + suffix;
		final String selector = attributeName + selectorChar + "=" + word;

		return Stream.of( //
				Arguments.of( selector, matchingValue, true ), //
				Arguments.of( selector, notMatchingValue, false ) //
		);
	}

	@CsvSource( value = { "word, true", "word-suffix,true", "wordsuffix,false", "prefixword-suffix,false" } )
	@ParameterizedTest( name = "matching value: {0}" )
	void should_match_attribute_with_value_starting_with_word( final String matchingValue, final boolean result )
			throws Exception {
		final String word = "word";
		final String selectorChar = "|";
		final String attributeName = "attributeName";
		final String selector = attributeName + selectorChar + "=" + word;
		when( element.getAttributeValue( attributeName ) ).thenReturn( matchingValue );
		assertThat( Has.attributeStarting( selector ).test( element ) ).isEqualTo( result );
	}

	@Test
	void should_match_attribute_with_value() throws Exception {
		final String attributeName = "attributeName";
		final String attributeValue = "attributeValue";
		final String selector = attributeName + "=" + attributeValue;
		when( element.getAttributeValue( attributeName ) ).thenReturn( attributeValue );

		assertThat( Has.attribute( selector ).test( element ) ).isTrue();
	}

	@Test
	void should_work_with_null_value() throws Exception {
		final String attributeName = "attributeName";
		final String attributeValue = "attributeValue";
		final String selector = attributeName + "=" + attributeValue;
		when( element.getAttributeValue( attributeName ) ).thenReturn( null );

		assertThat( Has.attribute( selector ).test( element ) ).isFalse();
	}

	@ValueSource( strings = { "\"", "'" } )
	@ParameterizedTest
	void should_match_attribute_with_escaped_value( final char escapeChar ) throws Exception {
		final String attributeName = "attributeName";
		final String attributeValue = "attributeValue";
		final String selector = attributeName + "=" + escapeChar + attributeValue + escapeChar;
		when( element.getAttributeValue( attributeName ) ).thenReturn( attributeValue );

		assertThat( Has.attribute( selector ).test( element ) ).isTrue();
	}

	@Test
	void should_match_attribute() throws Exception {
		final String attributeName = "attributeName";
		final String selector = attributeName;
		when( element.getAttributeValue( attributeName ) ).thenReturn( "true" );

		assertThat( Has.attribute( selector ).test( element ) ).isTrue();
	}

	@MethodSource( "pseudoClasses" )
	@ParameterizedTest( name = "Pseudo-class: {0}" )
	void should_match_pseudo_class( final String value, final boolean expected ) throws Exception {
		final String attributeName = "checked";
		final String selector = attributeName;
		when( element.getAttributeValue( attributeName ) ).thenReturn( value );

		assertThat( Has.cssPseudoClass( selector ).test( element ) ).isEqualTo( expected );
	}

	private static Stream<Arguments> pseudoClasses() {
		return Stream.of( //
				Arguments.of( "true", true ), //
				Arguments.of( null, false ), //
				Arguments.of( "false", false ) //
		);
	}

	@Test
	void should_match_link_text_as_attribute() throws Exception {
		final String value = "value";
		when( identifyingAttribues.getType() ).thenReturn( "a" );
		when( attributes.get( TEXT ) ).thenReturn( value );
		when( element.getAttributes() ).thenReturn( attributes );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );

		assertThat( Has.linkText( value ).test( element ) ).isTrue();
	}

	@Test
	void should_match_link_text_as_identifying_attribute() throws Exception {
		final String value = "value";
		when( identifyingAttribues.get( TEXT ) ).thenReturn( value );
		when( identifyingAttribues.getType() ).thenReturn( "not a" );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );

		assertThat( Has.linkText( value ).test( element ) ).isTrue();
	}

	@Test
	void should_match_partial_link_text() throws Exception {
		final String partialLinkText = "partial link";
		final String linkText = partialLinkText + "prefix";
		when( identifyingAttribues.getType() ).thenReturn( "a" );
		when( element.getAttributeValue( TEXT ) ).thenReturn( linkText );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );

		assertThat( Has.partialLinkText( partialLinkText ).test( element ) ).isEqualTo( true );
	}

	@Test
	void should_match_no_partial_link_text() throws Exception {
		final String partialLinkText = "partial link";
		final String linkText = partialLinkText + "prefix";
		final String notMatchingLink = "not matching";
		when( identifyingAttribues.getType() ).thenReturn( "a" );
		when( element.getAttributeValue( TEXT ) ).thenReturn( linkText );
		when( element.getIdentifyingAttributes() ).thenReturn( identifyingAttribues );

		assertThat( Has.partialLinkText( notMatchingLink ).test( element ) ).isEqualTo( false );
	}

	@Test
	void should_not_match_pseudo_element() throws Exception {
		final String value = "value";
		when( element.getAttributeValue( "pseudo" ) ).thenReturn( "true" );

		assertThat( Has.linkText( value ).test( element ) ).isFalse();
	}
}

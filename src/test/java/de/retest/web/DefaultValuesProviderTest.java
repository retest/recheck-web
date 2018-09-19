package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import de.retest.ui.Path;
import de.retest.ui.descriptors.IdentifyingAttributes;

class DefaultValuesProviderTest {

	@Test
	void default_values_loaded_from_yaml() {
		final DefaultValuesProvider cut = new DefaultValuesProvider();
		assertThat( cut.isDefault( "blockquote", "display", "block" ) ).isTrue();
		assertThat( cut.isDefault( "blockquote", "margin-left", "40px" ) ).isTrue();
		assertThat( cut.isDefault( "a:link", "cursor", "auto" ) ).isTrue();
	}

	@Test
	void have_fallbacks_and_general_defaults() {
		final DefaultValuesProvider cut = new DefaultValuesProvider();
		assertThat( cut.isDefault( "foo", "bar", "0px" ) ).isTrue();
		assertThat( cut.isDefault( "fox", "bus", null ) ).isTrue();
		assertThat( cut.isDefault( "fox", "trot", "normal" ) ).isTrue();
		assertThat( cut.isDefault( "fug", "bos", "auto" ) ).isTrue();
		assertThat( cut.isDefault( "fuzz", "bug", "" ) ).isTrue();
		assertThat( cut.isDefault( "gin", "fitz", " " ) ).isTrue();
		assertThat( cut.isDefault( "fizz", "bug", "normal" ) ).isTrue();
	}

	@Test
	void getDefaultValue_should_be_CASE_INSENSITIVE() {
		final DefaultValuesProvider cut = new DefaultValuesProvider();
		final IdentifyingAttributes IDENT = IdentifyingAttributes.create( Path.fromString( "/HTML/BODY/DIV/" ), "P" );
		assertThat( cut.getDefaultValue( IDENT, "DISPLAY" ) ).isEqualTo( "block" );

		final IdentifyingAttributes ident = IdentifyingAttributes.create( Path.fromString( "/html/body/div/" ), "p" );
		assertThat( cut.getDefaultValue( ident, "display" ) ).isEqualTo( "block" );
	}

	@Test
	void getDefaultValue_should_prefer_specific_over_general_values() {
		final DefaultValuesProvider cut = new DefaultValuesProvider();
		assertThat( cut.getDefaultValue( "body", "margin-top" ) ).isEqualTo( "8px" );
		assertThat( cut.getDefaultValue( "boody", "margin-top" ) ).isEqualTo( "0px" );
	}

	@Test
	void isDefault_handled_two_null_values() {
		final DefaultValuesProvider cut = new DefaultValuesProvider();
		assertThat( cut.isDefault( "booody", "align-content", null ) ).isTrue();
		assertThat( cut.isDefault( "fox", "color", null ) ).isTrue();
		assertThat( cut.isDefault( "div", "outline-width", null ) ).isTrue();
		assertThat( cut.isDefault( "body", "outline-width", null ) ).isTrue();
		assertThat( cut.isDefault( "fox", "null", null ) ).isTrue();
		assertThat( cut.isDefault( "fox", "resize", null ) ).isTrue();
		assertThat( cut.isDefault( "div", "animation-delay", null ) ).isTrue();
		assertThat( cut.isDefault( "P", "margin-top", null ) ).isTrue();
	}
}

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
		assertThat( cut.isDefault( "foo", "box-shadow", "none" ) ).isTrue();
		assertThat( cut.isDefault( "foo", "bar", null ) ).isTrue();
		assertThat( cut.isDefault( "foo", "bar", "" ) ).isTrue();
		assertThat( cut.isDefault( "foo", "bar", " " ) ).isTrue();
		assertThat( cut.isDefault( "foo", "bar", "0px" ) ).isTrue();
		assertThat( cut.isDefault( "foo", "bar", "auto" ) ).isTrue();
		assertThat( cut.isDefault( "foo", "bar", "normal" ) ).isTrue();
		assertThat( cut.isDefault( "foo", "bar", "rgb(0, 0, 0)" ) ).isTrue();
		assertThat( cut.isDefault( "foo", "bar", "0px 0px" ) ).isTrue();
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
	void if_we_have_a_default_value_then_null_should_not_be_accepted() {
		// Unless we have learned otherwise, default beats null or empty
		final DefaultValuesProvider cut = new DefaultValuesProvider();
		assertThat( cut.isDefault( "a", "text-decoration", "" ) ).isFalse();
		assertThat( cut.isDefault( "a", "text-decoration", null ) ).isFalse();
	}
}

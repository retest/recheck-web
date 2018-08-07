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
	void getDefaultValue_should_return_same() {
		final DefaultValuesProvider cut = new DefaultValuesProvider();
		final IdentifyingAttributes identifyingAttributes =
				IdentifyingAttributes.create( Path.fromString( "/HTML/BODY/DIV/" ), "p" );
		assertThat( cut.getDefaultValue( identifyingAttributes, "display" ) ).isEqualTo( "block" );
	}
}

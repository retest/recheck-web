package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;

class DefaultWebValueFinderTest {

	@Test
	void default_values_loaded_from_yaml() {
		final DefaultWebValueFinder cut = new DefaultWebValueFinder();
		final IdentifyingAttributes ident0 = mock( IdentifyingAttributes.class );
		when( ident0.getType() ).thenReturn( "blockquote" );
		assertThat( cut.isDefaultValue( ident0, "display", "block" ) ).isTrue();
		assertThat( cut.isDefaultValue( ident0, "margin-left", "40px" ) ).isTrue();
		final IdentifyingAttributes ident1 = mock( IdentifyingAttributes.class );
		when( ident1.getType() ).thenReturn( "a:link" );
		assertThat( cut.isDefaultValue( ident1, "cursor", "auto" ) ).isTrue();
	}

	@Test
	void have_fallbacks_and_general_defaults() {
		final DefaultWebValueFinder cut = new DefaultWebValueFinder();
		final IdentifyingAttributes ident = mock( IdentifyingAttributes.class );
		when( ident.getType() ).thenReturn( "foo" );
		assertThat( cut.isDefaultValue( ident, "box-shadow", "none" ) ).isTrue();
		assertThat( cut.isDefaultValue( ident, "bar", null ) ).isTrue();
		assertThat( cut.isDefaultValue( ident, "bar", "" ) ).isTrue();
		assertThat( cut.isDefaultValue( ident, "bar", " " ) ).isTrue();
		assertThat( cut.isDefaultValue( ident, "bar", "0px" ) ).isTrue();
		assertThat( cut.isDefaultValue( ident, "bar", "auto" ) ).isTrue();
		assertThat( cut.isDefaultValue( ident, "bar", "normal" ) ).isTrue();
		assertThat( cut.isDefaultValue( ident, "bar", "rgb(0, 0, 0)" ) ).isTrue();
		assertThat( cut.isDefaultValue( ident, "bar", "0px 0px" ) ).isTrue();
	}

	@Test
	void getDefaultValue_should_be_case_insensitive() {
		final DefaultWebValueFinder cut = new DefaultWebValueFinder();
		final IdentifyingAttributes identUpperCase =
				IdentifyingAttributes.create( Path.fromString( "/HTML/BODY/DIV" ), "P" );
		assertThat( cut.isDefaultValue( identUpperCase, "DISPLAY", "block" ) ).isTrue();
		final IdentifyingAttributes identLowerCase =
				IdentifyingAttributes.create( Path.fromString( "/html/body/div" ), "p" );
		assertThat( cut.isDefaultValue( identLowerCase, "display", "block" ) ).isTrue();
	}

	@Test
	void getDefaultValue_should_prefer_specific_over_general_values() {
		final DefaultWebValueFinder cut = new DefaultWebValueFinder();
		final IdentifyingAttributes ident0 = mock( IdentifyingAttributes.class );
		when( ident0.getType() ).thenReturn( "body" );
		assertThat( cut.getDefaultValue( ident0, "margin-top" ) ).isEqualTo( "8px" );
		final IdentifyingAttributes ident1 = mock( IdentifyingAttributes.class );
		when( ident1.getType() ).thenReturn( "ul" );
		assertThat( cut.getDefaultValue( ident1, "margin-top" ) ).isEqualTo( "1em" );
	}

	@Test
	void if_we_have_a_default_value_then_null_should_not_be_accepted() {
		// Unless we have learned otherwise, default beats null or empty.
		final DefaultWebValueFinder cut = new DefaultWebValueFinder();
		final IdentifyingAttributes ident = mock( IdentifyingAttributes.class );
		when( ident.getType() ).thenReturn( "a" );
		assertThat( cut.isDefaultValue( ident, "text-decoration", "" ) ).isFalse();
		assertThat( cut.isDefaultValue( ident, "text-decoration", null ) ).isFalse();
	}
}

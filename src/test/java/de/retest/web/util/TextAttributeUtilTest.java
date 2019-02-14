package de.retest.web.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class TextAttributeUtilTest {

	@Test
	void create_should_return_a_text_attribute_without_formatting() {
		final String text = "foo	\n	bar";
		final String expected = text.replaceAll( "\\s+", " " );
		assertThat( TextAttributeUtil.createTextAttribute( "", text ).getValue() ).isEqualTo( expected );
	}

	@Test
	void create_should_return_text_attribute_with_formatting() {
		final String text = "foo	\n	bar";
		assertThat( TextAttributeUtil.createTextAttribute( "/" + TextAttributeUtil.PRE_ELEMENT, text ).getValue() )
				.isEqualTo( text );
	}
}

package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttributesUtilTest {

	AttributesProvider attributesProvider;

	@BeforeEach
	void setUp() {
		attributesProvider = YamlAttributesProvider.getTestInstance();
	}

	@Test
	void identifying_attributes_should_not_be_state_attributes() throws Exception {
		assertThat( AttributesUtil.isStateAttribute( AttributesUtil.TAG_NAME, attributesProvider ) ).isFalse();
	}

	@Test
	void all_keys_should_be_state_attributes_when_all_html_attributes_is_true() throws Exception {
		/*
		 * Note that the set of CSS attributes from attributes.yaml is used to scrape the web data (see
		 * RecheckSeleniumAdapter and getAllElementsByPath.js), so no unselected CSS attributes will be part of the
		 * element's state.
		 */
		assertThat( AttributesUtil.isStateAttribute( "foo", attributesProvider ) ).isTrue();
	}

	@Test
	void only_selected_keys_should_be_state_attributes_when_all_html_attributes_is_false() throws Exception {
		final String selected = "bar";
		final String unselected = "baz";

		final AttributesProvider provider = mock( AttributesProvider.class );
		when( provider.getHtmlAttributes() ).thenReturn( new HashSet<>( Arrays.asList( selected ) ) );

		assertThat( AttributesUtil.isStateAttribute( selected, provider ) ).isTrue();
		assertThat( AttributesUtil.isStateAttribute( unselected, provider ) ).isFalse();
	}

}

package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AttributesUtilTest {

	@Test
	void identifying_attributes_should_not_be_state_attributes() throws Exception {
		assertThat( AttributesUtil.isStateAttribute( AttributesUtil.TAG_NAME ) ).isFalse();
	}

	@Test
	void selected_css_attributes_should_be_state_attributes() throws Exception {
		assertThat( AttributesUtil.isStateAttribute( "align-content" ) ).isTrue();
	}

	@Test
	void all_html_attributes_should_be_state_attributes() throws Exception {
		assertThat( AttributesUtil.isStateAttribute( "foo" ) ).isTrue();
	}

}

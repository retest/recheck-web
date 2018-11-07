package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class WebDataFilterTest {

	@Test
	void should_ignore_invisible_elements() {
		final WebData input = createInput( "a" );
		assertThat( WebDataFilter.shouldIgnore( input ) ).isTrue();
	}

	@Test
	void should_not_ignore_head_element() {
		final WebData input = createInput( "head" );
		assertThat( WebDataFilter.shouldIgnore( input ) ).isFalse();
	}

	@Test
	void should_not_ignore_title_element() {
		final WebData input = createInput( "title" );
		assertThat( WebDataFilter.shouldIgnore( input ) ).isFalse();
	}

	private WebData createInput( final String tagName ) {
		final Map<String, Object> wrappedData = new HashMap<>();
		wrappedData.put( "x", "0" );
		wrappedData.put( "y", "0" );
		wrappedData.put( "width", "0" );
		wrappedData.put( "height", "0" );
		wrappedData.put( "tagName", tagName );
		final WebData input = new WebData( wrappedData );
		return input;
	}

}

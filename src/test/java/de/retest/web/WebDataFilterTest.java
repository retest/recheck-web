package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class WebDataFilterTest {

	@Test
	void should_ignore_invisible_elements() {
		final WebData input = createInvisibleWebDataForTag( "a" );
		assertThat( WebDataFilter.shouldIgnore( input ) ).isTrue();
	}

	@Test
	void should_not_ignore_visible_elements() throws Exception {
		final WebData input = createVisibleWebDataForTag( "a" );
		assertThat( WebDataFilter.shouldIgnore( input ) ).isFalse();
	}

	@Test
	void should_not_ignore_head_element() {
		final WebData input = createInvisibleWebDataForTag( "head" );
		assertThat( WebDataFilter.shouldIgnore( input ) ).isFalse();
	}

	@Test
	void should_not_ignore_title_element() {
		final WebData input = createInvisibleWebDataForTag( "title" );
		assertThat( WebDataFilter.shouldIgnore( input ) ).isFalse();
	}

	private WebData createInvisibleWebDataForTag( final String tagName ) {
		final Map<String, Object> wrappedData = createWrappedData( tagName );
		wrappedData.put( "x", 0 );
		wrappedData.put( "y", 0 );
		wrappedData.put( "width", 0 );
		wrappedData.put( "height", 0 );
		final WebData webData = new WebData( wrappedData );
		return webData;
	}

	private WebData createVisibleWebDataForTag( final String tagName ) {
		final Map<String, Object> wrappedData = createWrappedData( tagName );
		wrappedData.put( "x", 1 );
		wrappedData.put( "y", 1 );
		wrappedData.put( "width", 1 );
		wrappedData.put( "height", 1 );
		wrappedData.put( "shown", true );
		final WebData webData = new WebData( wrappedData );
		return webData;
	}

	private Map<String, Object> createWrappedData( final String tagName ) {
		final Map<String, Object> wrappedData = new HashMap<>();
		wrappedData.put( "tagName", tagName );
		return wrappedData;
	}

}

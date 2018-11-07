package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
		final WebData webData = createWebDataMock( tagName );
		when( webData.isShown() ).thenReturn( false );
		return webData;
	}

	private WebData createVisibleWebDataForTag( final String tagName ) {
		final WebData webData = createWebDataMock( tagName );
		when( webData.isShown() ).thenReturn( true );
		return webData;
	}

	private WebData createWebDataMock( final String tagName ) {
		final WebData webData = mock( WebData.class );
		when( webData.getTag() ).thenReturn( tagName );
		return webData;
	}

}

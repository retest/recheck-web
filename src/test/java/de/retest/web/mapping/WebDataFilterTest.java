package de.retest.web.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import de.retest.web.mapping.WebData;
import de.retest.web.mapping.WebDataFilter;

class WebDataFilterTest {

	@Test
	void should_ignore_invisible_elements() {
		final WebData webData = createInvisibleWebDataForTag( "a" );
		assertThat( WebDataFilter.shouldIgnore( webData ) ).isTrue();
	}

	@Test
	void should_not_ignore_visible_elements() throws Exception {
		final WebData webData = createVisibleWebDataForTag( "a" );
		assertThat( WebDataFilter.shouldIgnore( webData ) ).isFalse();
	}

	@ParameterizedTest
	@MethodSource( "specialTags" )
	void testName( final String specialTag ) throws Exception {
		final WebData webData = createVisibleWebDataForTag( specialTag );
		assertThat( WebDataFilter.shouldIgnore( webData ) ).isFalse();
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

	static Stream<String> specialTags() {
		return WebDataFilter.specialTags.stream();
	}

}

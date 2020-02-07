package de.retest.web.selenium.css;

import static org.mockito.Mockito.when;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.retest.recheck.ui.descriptors.Element;

@ExtendWith( MockitoExtension.class )
@ExtendWith( SoftAssertionsExtension.class )
class RegexTransformerTest {

	private static final String SELECTOR_PATTERN = "([a-z;]+;)";
	private static final String SELECTED_PART = "abc;";
	private static final String REMAINING_PART = "b l u b";
	private static final String TRIMMED_REMAINING_PART = REMAINING_PART.trim();

	@Mock
	private PredicateFactory factory;
	@Mock
	private Element element;

	@Test
	void should_not_match_regex( final SoftAssertions softly ) throws Exception {
		final Transformer transformer = RegexTransformer.of( SELECTOR_PATTERN, factory );

		final String selectorString = "002;blub";
		final Selector cssSelector = transformer.transform( selectorString );

		softly.assertThat( cssSelector.getPredicate().test( element ) ).isFalse();
		softly.assertThat( cssSelector.getRemainingSelector() ).isEqualTo( selectorString );
	}

	@Test
	void should_match_regex( final SoftAssertions softly ) throws Exception {
		when( factory.create( SELECTED_PART ) ).thenReturn( e -> true );
		final Transformer transformer = RegexTransformer.of( SELECTOR_PATTERN, factory );

		final Selector cssSelector = transformer.transform( SELECTED_PART + REMAINING_PART );

		softly.assertThat( cssSelector.getPredicate().test( element ) ).isTrue();
		softly.assertThat( cssSelector.getRemainingSelector() ).isEqualTo( TRIMMED_REMAINING_PART );
	}
}

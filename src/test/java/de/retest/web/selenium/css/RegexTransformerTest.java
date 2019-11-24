package de.retest.web.selenium.css;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import de.retest.recheck.ui.descriptors.Element;

@ExtendWith( MockitoExtension.class )
class RegexTransformerTest {

	private static final String SELECTOR_PATTERN = "[a-z;]+";
	private static final String SELECTED_PART = "abc;";
	private static final String REMAINING_PART = "b l u b";
	private static final String TRIMMED_REMAINING_PART = REMAINING_PART.trim();
	@Mock
	private Function<String, Predicate<Element>> factory;
	@Mock
	private Element element;

	@Test
	void does_not_match() throws Exception {
		final Pattern cssPattern = Pattern.compile( "^(" + SELECTOR_PATTERN + ";)(.*)" );
		final RegexTransformer transformer = new RegexTransformer( cssPattern, factory );

		final String selectorString = "002;blub";
		final Selector cssSelector = transformer.transform( selectorString );

		assertAll( () -> assertFalse( cssSelector.matches() ),
				() -> assertFalse( cssSelector.predicate().test( element ) ),
				() -> assertThat( cssSelector.remainingSelector() ).isEqualTo( selectorString ) );
	}

	@Test
	void matches_regex() throws Exception {
		when( factory.apply( SELECTED_PART ) ).thenReturn( e -> true );
		final RegexTransformer transformer = newTransformer();

		final Selector cssSelector = transformer.transform( SELECTED_PART + REMAINING_PART );

		assertAll( () -> assertTrue( cssSelector.matches() ),
				() -> assertTrue( cssSelector.predicate().test( element ) ),
				() -> assertThat( cssSelector.remainingSelector() ).isEqualTo( TRIMMED_REMAINING_PART ) );
	}

	private RegexTransformer newTransformer() {
		final Pattern cssPattern = Pattern.compile( "^(" + SELECTOR_PATTERN + ";)(.*)" );
		return new RegexTransformer( cssPattern, factory );
	}
}

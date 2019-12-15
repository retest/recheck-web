package de.retest.web.selenium.css;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import de.retest.recheck.ui.descriptors.Element;

@ExtendWith( MockitoExtension.class )
@ExtendWith( SoftAssertionsExtension.class )
public class PredicateBuilderTest {

	private static final String CSS_SELECTOR_NOT_SUPPORTED =
			"Unbreakable tests are not implemented for all CSS selectors.";
	@Mock
	private Element element;
	@Mock
	private Element otherElement;
	private List<ILoggingEvent> logsList;

	@BeforeEach
	private void beforeEach() {
		final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		((Logger) LoggerFactory.getLogger( PredicateBuilder.class )).addAppender( listAppender );
		logsList = listAppender.list;
	}

	@Test
	void build_should_parse_empty_selector( final SoftAssertions softly ) throws Exception {
		final String origSelector = "";
		final List<Transformer> transformers = emptyList();

		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		softly.assertThat( predicate.map( p -> p.test( element ) ) ).hasValue( true );
		softly.assertThat( logsList ).isEmpty();
	}

	@Test
	void should_log_warning_when_parsing_unknown_selector( final SoftAssertions softly ) throws Exception {
		final String origSelector = "abc";
		final List<Transformer> transformers = singletonList( s -> Selector.unsupported( origSelector ) );

		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		softly.assertThat( predicate ).isEmpty();
		softly.assertThat( logsList.get( 0 ).getMessage() ).startsWith( CSS_SELECTOR_NOT_SUPPORTED );
		softly.assertThat( logsList.get( 0 ).getArgumentArray()[0] ).isEqualTo( origSelector );
	}

	@Test
	void should_log_warning_when_parsing_selector_with_unknown_part( final SoftAssertions softly ) throws Exception {
		final String remainingPart = "remaining";
		final Selector matchingSelector = Selector.unsupported( remainingPart );
		final String origSelector = "abc";
		final List<Transformer> transformers = singletonList( s -> matchingSelector );

		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		softly.assertThat( predicate ).isEmpty();
		softly.assertThat( logsList.get( 0 ).getMessage() ).startsWith( CSS_SELECTOR_NOT_SUPPORTED );
		softly.assertThat( logsList.get( 0 ).getArgumentArray()[0] ).isEqualTo( remainingPart );
	}

	@Test
	void should_parse_matching_selector( final SoftAssertions softly ) throws Exception {
		final String origSelector = "abc";
		final String remainingPart = "";
		final Selector matchingSelector = Selector.supported( remainingPart, element::equals );
		final List<Transformer> transformers = singletonList( s -> matchingSelector );

		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		softly.assertThat( predicate.map( p -> p.test( element ) ) ).hasValue( true );
		softly.assertThat( predicate.map( p -> p.test( otherElement ) ) ).hasValue( false );
		softly.assertThat( logsList ).isEmpty();
	}

	@Test
	void should_parse_multiple_matching_selectors( final SoftAssertions softly ) throws Exception {
		final String origSelector = "abcdef";
		final Selector someSelector = Selector.supported( "def", e -> true );
		final Selector otherSelector = Selector.supported( "", e -> false );
		final List<Transformer> transformers = asList( s -> someSelector, s -> otherSelector );

		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		softly.assertThat( predicate.map( p -> p.test( element ) ) ).hasValue( false );
		softly.assertThat( logsList ).isEmpty();
	}

	private Optional<Predicate<Element>> buildPredicate( final String origSelector,
			final List<Transformer> transformers ) {
		return new PredicateBuilder( transformers, origSelector ).build();
	}

}

package de.retest.web.selenium.css;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

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
	void build_parse_empty_selector() throws Exception {
		final String origSelector = "";
		final List<Transformer> transformers = emptyList();
		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		assertAll( () -> assertThat( predicate.map( p -> p.test( element ) ) ).hasValue( true ),
				() -> assertThat( logsList ).isEmpty() );

	}

	@Test
	void parse_unknown_selector() throws Exception {
		final String origSelector = "abc";
		final List<Transformer> transformers = singletonList( s -> new EmptySelector( origSelector ) );
		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		assertAll( () -> assertThat( predicate ).isEmpty(),
				() -> assertThat( logsList.get( 0 ).getMessage() ).startsWith( CSS_SELECTOR_NOT_SUPPORTED ),
				() -> assertThat( logsList.get( 0 ).getArgumentArray()[0] ).isEqualTo( origSelector ) );

	}

	@Test
	void parse_selector_with_unknown_part() throws Exception {
		final String remainingPart = "remaining";
		final Selector matchingSelector = createSelectorWith( remainingPart, e -> false );
		final String origSelector = "abc";
		final List<Transformer> transformers = singletonList( s -> matchingSelector );
		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		assertAll( () -> assertThat( predicate ).isEmpty(),
				() -> assertThat( logsList.get( 0 ).getMessage() ).startsWith( CSS_SELECTOR_NOT_SUPPORTED ),
				() -> assertThat( logsList.get( 0 ).getArgumentArray()[0] ).isEqualTo( remainingPart ) );

	}

	@Test
	void parse_matching_selector() throws Exception {
		final String origSelector = "abc";
		final String remainingPart = "";
		final Selector matchingSelector = createSelectorWith( remainingPart, element::equals );
		final List<Transformer> transformers = singletonList( s -> matchingSelector );
		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		assertAll( () -> assertThat( predicate.map( p -> p.test( element ) ) ).hasValue( true ),
				() -> assertThat( predicate.map( p -> p.test( otherElement ) ) ).hasValue( false ),
				() -> assertThat( logsList ).isEmpty() );

	}

	@Test
	void parse_multiple_matching_selectors() throws Exception {
		final String origSelector = "abc";
		final Selector someSelector = createSelectorWith( "def", e -> true );
		final Selector otherSelector = createSelectorWith( "", e -> false );
		final List<Transformer> transformers = asList( s -> someSelector, s -> otherSelector );
		final Optional<Predicate<Element>> predicate = buildPredicate( origSelector, transformers );

		assertAll( () -> assertThat( predicate.map( p -> p.test( element ) ) ).hasValue( false ),
				() -> assertThat( logsList ).isEmpty() );
		verify( someSelector ).predicate();
		verify( otherSelector ).predicate();

	}

	private Optional<Predicate<Element>> buildPredicate( final String origSelector,
			final List<Transformer> transformers ) {
		return new PredicateBuilder( transformers, origSelector ).build();
	}

	private Selector createSelectorWith( final String remainingPart, final Predicate<Element> predicate ) {
		final Selector matchingSelector = mock( Selector.class );
		when( matchingSelector.matches() ).thenReturn( true, false );
		when( matchingSelector.remainingSelector() ).thenReturn( remainingPart );
		when( matchingSelector.predicate() ).thenReturn( predicate );
		return matchingSelector;
	}

}

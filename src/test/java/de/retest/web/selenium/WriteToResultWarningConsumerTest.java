package de.retest.web.selenium;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.retest.recheck.ui.diff.AttributeDifference;
import de.retest.recheck.ui.diff.ElementDifference;
import de.retest.recheck.ui.diff.ElementIdentificationWarning;
import de.retest.web.selenium.WriteToResultWarningConsumer.ElementDifferenceRetriever;

class WriteToResultWarningConsumerTest {

	ElementDifferenceRetriever retriever;
	WriteToResultWarningConsumer cut;

	ElementIdentificationWarning warning;
	QualifiedElementWarning qualifiedWarning;

	@BeforeEach
	void setUp() {
		retriever = mock( ElementDifferenceRetriever.class );

		cut = new WriteToResultWarningConsumer( retriever );

		warning = new ElementIdentificationWarning( "file.java", 0, "id", "File" );
		qualifiedWarning = new QualifiedElementWarning( "id", "id", warning );
	}

	@Test
	void accept_should_find_warning_and_add_to_result() throws Exception {
		final AttributeDifference attribute = mock( AttributeDifference.class );
		when( attribute.getKey() ).thenReturn( "id" );

		final ElementDifference difference = mock( ElementDifference.class );
		when( difference.getRetestId() ).thenReturn( "id" );
		when( difference.getAttributeDifferences() ).thenReturn( Collections.singletonList( attribute ) );

		when( retriever.getDifferences() ).thenReturn( Stream.of( difference ) );

		cut.accept( qualifiedWarning );

		verify( attribute ).addElementIdentificationWarning( warning );
	}

	@Test
	void accept_should_not_add_if_attribute_does_not_match() throws Exception {
		final AttributeDifference attribute = mock( AttributeDifference.class );
		when( attribute.getKey() ).thenReturn( "foo" );

		final ElementDifference difference = mock( ElementDifference.class );
		when( difference.getRetestId() ).thenReturn( "id" );
		when( difference.getAttributeDifferences() ).thenReturn( Collections.singletonList( attribute ) );

		when( retriever.getDifferences() ).thenReturn( Stream.of( difference ) );

		cut.accept( qualifiedWarning );

		verify( attribute, never() ).addElementIdentificationWarning( warning );
	}

	@Test
	void accept_should_not_add_if_retest_id_does_not_match() throws Exception {
		final AttributeDifference attribute = mock( AttributeDifference.class );
		when( attribute.getKey() ).thenReturn( "id" );

		final ElementDifference difference = mock( ElementDifference.class );
		when( difference.getRetestId() ).thenReturn( "foo" );
		when( difference.getAttributeDifferences() ).thenReturn( Collections.singletonList( attribute ) );

		when( retriever.getDifferences() ).thenReturn( Stream.of( difference ) );

		cut.accept( qualifiedWarning );

		verify( attribute, never() ).addElementIdentificationWarning( warning );
	}
}

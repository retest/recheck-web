package de.retest.web.selenium;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import de.retest.recheck.ui.diff.AttributeDifference;
import de.retest.recheck.ui.diff.ElementDifference;
import de.retest.recheck.ui.diff.ElementIdentificationWarning;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WriteToResultWarningConsumer implements Consumer<QualifiedElementWarning> {

	private final ElementDifferenceRetriever retriever;

	@Override
	public void accept( final QualifiedElementWarning warning ) {
		retriever.getDifferences() //
				.filter( matchesRetestId( warning.getRetestId() ) ) //
				.map( ElementDifference::getAttributeDifferences ) //
				.flatMap( Collection::stream ) //
				.filter( matchesAttributeKey( warning.getAttributeKey() ) ) // Should only be one difference
				.forEach( addWarning( warning.getWarning() ) );
	}

	private Predicate<ElementDifference> matchesRetestId( final String retestId ) {
		return difference -> difference.getRetestId().equals( retestId );
	}

	private Predicate<AttributeDifference> matchesAttributeKey( final String attributeKey ) {
		return difference -> difference.getKey().equals( attributeKey );
	}

	private Consumer<AttributeDifference> addWarning( final ElementIdentificationWarning warning ) {
		return difference -> difference.addElementIdentificationWarning( warning );
	}

	@FunctionalInterface
	public interface ElementDifferenceRetriever {

		Stream<ElementDifference> getDifferences();
	}
}

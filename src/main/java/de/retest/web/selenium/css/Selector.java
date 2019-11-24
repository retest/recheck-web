package de.retest.web.selenium.css;

import java.util.function.Predicate;

import de.retest.recheck.ui.descriptors.Element;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor( access = AccessLevel.PRIVATE )
class Selector {

	private final String remainingSelector;
	private final Predicate<Element> predicate;

	static Selector supported( final String remainingSelector, final Predicate<Element> predicate ) {
		return new Selector( remainingSelector, predicate );
	}

	static Selector unsupported( final String remainingSelector ) {
		return new Selector( remainingSelector, e -> false );
	}

}

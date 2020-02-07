package de.retest.web.selenium.css;

import java.util.function.Predicate;

import de.retest.recheck.ui.descriptors.Element;

@FunctionalInterface
public interface PredicateFactory {

	Predicate<Element> create( String attribute );

}

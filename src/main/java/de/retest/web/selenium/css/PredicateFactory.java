package de.retest.web.selenium.css;

import java.util.function.Predicate;

import de.retest.recheck.ui.descriptors.Element;

public interface PredicateFactory {

	Predicate<Element> create( String attribute );

}

package de.retest.web;

import java.util.Set;

public interface AttributesProvider {

	/**
	 * Set of CSS attributes that are added to an element's state. Can be a set of selected attributes or empty ("all"
	 * not supported).
	 *
	 * @return Possibly empty set of selected CSS attributes.
	 */
	Set<String> getCssAttributes();

	/**
	 * Set of HTML attributes that are added to an element's state. Can be a set of selected attributes, empty, or
	 * "all".
	 *
	 * @return Possibly empty set of selected HTML attributes or {@code null} in the case of "all".
	 */
	Set<String> getHtmlAttributes();

	/**
	 * @return {@code true} if all HTML attributes should be used, otherwise false.
	 */
	boolean allHtmlAttributes();

}

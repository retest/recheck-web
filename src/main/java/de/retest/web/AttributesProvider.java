package de.retest.web;

import java.util.Set;

public interface AttributesProvider {
	Set<String> getCssAttributes();

	Set<String> getHtmlAttributes();

	boolean allHtmlAttributes();
}

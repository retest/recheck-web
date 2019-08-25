package de.retest.web.mapping;

import java.awt.Rectangle;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

public interface WebDataProvider {

	String getPath();

	Rectangle getAbsoluteOutline();

	Rectangle getOutline();

	String getText();

	String getTag();

	Stream<Pair<String, String>> getHTMLAttributes();

	Stream<Pair<String, String>> getCSSAttributes();
}

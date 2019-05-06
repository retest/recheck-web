package de.retest.web.mapping;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

// TODO This should be part of recheck and somehow use RecheckAdapter#convert to filter the result.
public class WebDataFilter {

	/**
	 * Tags that will not be ignored even if they are not shown.
	 */
	static final Set<String> specialTags = new HashSet<>( Arrays.asList( "meta", "option", "title" ) );

	private WebDataFilter() {}

	public static boolean shouldIgnore( final WebData webData ) {
		return isNotShown( webData ) && isNotSpecialTag( webData );
	}

	private static boolean isNotShown( final WebData webData ) {
		return !webData.isShown();
	}

	private static boolean isNotSpecialTag( final WebData webData ) {
		final String lowerCaseTag = webData.getTag().toLowerCase();
		return !specialTags.contains( lowerCaseTag );
	}

}

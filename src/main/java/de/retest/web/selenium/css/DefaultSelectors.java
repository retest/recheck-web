package de.retest.web.selenium.css;

import static java.util.stream.Collectors.toList;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import de.retest.recheck.ui.descriptors.Element;
import lombok.RequiredArgsConstructor;

public class DefaultSelectors {

	@RequiredArgsConstructor
	private static class Rule {
		private final String pattern;
		private final Function<String, Predicate<Element>> factory;

		private RegexTransformer createTransformer() {
			final Pattern cssTag = Pattern.compile( START_OF_LINE + pattern + REMAINING );
			return new RegexTransformer( cssTag, factory );
		}
	}

	private static final String CHARACTERSET = "a-zA-Z0-9\\-_";
	private static final String ALLOWED_CHARACTERS = "[" + CHARACTERSET + "]+";
	private static final String TAG_PATTERN = "(" + ALLOWED_CHARACTERS + ")";
	private static final String ID_PATTERN = "\\#(" + ALLOWED_CHARACTERS + ")";
	private static final String CLASS_PATTERN = "\\.(" + ALLOWED_CHARACTERS + ")";
	private static final String ATTRIBUTE_PATTERN = attributePattern( "" );
	private static final String ATTRIBUTE_CONTAINING_PATTERN = attributePattern( "~" );
	private static final String ATTRIBUTE_STARTING_PATTERN = attributePattern( "\\|" );
	private static final String ATTRIBUTE_BEGINNING_PATTERN = attributePattern( "\\^" );
	private static final String ATTRIBUTE_ENDING_PATTERN = attributePattern( "\\$" );
	private static final String ATTRIBUTE_CONTAINING_SUBSTRING_PATTERN = attributePattern( "\\*" );

	private static String attributePattern( final String selectorChar ) {
		return "\\[([" + CHARACTERSET + selectorChar + " =\"]+)\\]";
	}

	private static final String REMAINING = "(.*)$";
	private static final String START_OF_LINE = "^";

	public static List<Transformer> all() {
		final LinkedList<Rule> tupels = new LinkedList<>();
		tupels.add( new Rule( TAG_PATTERN, Has::cssTag ) );
		tupels.add( new Rule( ID_PATTERN, Has::cssId ) );
		tupels.add( new Rule( CLASS_PATTERN, Has::cssClass ) );
		tupels.add( new Rule( ATTRIBUTE_PATTERN, Has::attribute ) );
		tupels.add( new Rule( ATTRIBUTE_CONTAINING_PATTERN, Has::attributeContaining ) );
		tupels.add( new Rule( ATTRIBUTE_STARTING_PATTERN, Has::attributeStarting ) );
		tupels.add( new Rule( ATTRIBUTE_BEGINNING_PATTERN, Has::attributeBeginning ) );
		tupels.add( new Rule( ATTRIBUTE_ENDING_PATTERN, Has::attributeEnding ) );
		tupels.add( new Rule( ATTRIBUTE_CONTAINING_SUBSTRING_PATTERN, Has::attributeContainingSubstring ) );
		return tupels.stream().map( Rule::createTransformer ).collect( toList() );
	}

}

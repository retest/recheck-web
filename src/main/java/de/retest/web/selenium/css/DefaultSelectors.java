package de.retest.web.selenium.css;

import java.util.LinkedList;
import java.util.List;

public class DefaultSelectors {

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
	private static final String PSEUDO_CLASS_PATTERN = ":((?!not\\()" + ALLOWED_CHARACTERS + ")";

	private static String attributePattern( final String selectorChar ) {
		return "\\[([" + CHARACTERSET + selectorChar + " =\"']+)\\]";
	}

	private DefaultSelectors() {}

	public static List<Transformer> all() {
		final List<Transformer> transformers = new LinkedList<>();
		transformers.add( RegexTransformer.of( TAG_PATTERN, Has::cssTag ) );
		transformers.add( RegexTransformer.of( ID_PATTERN, Has::cssId ) );
		transformers.add( RegexTransformer.of( CLASS_PATTERN, Has::cssClass ) );
		transformers.add( RegexTransformer.of( ATTRIBUTE_PATTERN, Has::attribute ) );
		transformers.add( RegexTransformer.of( ATTRIBUTE_CONTAINING_PATTERN, Has::attributeContaining ) );
		transformers.add( RegexTransformer.of( ATTRIBUTE_STARTING_PATTERN, Has::attributeStarting ) );
		transformers.add( RegexTransformer.of( ATTRIBUTE_BEGINNING_PATTERN, Has::attributeBeginning ) );
		transformers.add( RegexTransformer.of( ATTRIBUTE_ENDING_PATTERN, Has::attributeEnding ) );
		transformers.add( RegexTransformer.of( ATTRIBUTE_CONTAINING_SUBSTRING_PATTERN, Has::attributeContainingSubstring ) );
		transformers.add( RegexTransformer.of( PSEUDO_CLASS_PATTERN, Has::cssPseudoClass ) );
		return transformers;
	}

}

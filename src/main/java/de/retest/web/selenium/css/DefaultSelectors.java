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
		final LinkedList<Transformer> transformers = new LinkedList<>();
		transformers.add( newTransformer( TAG_PATTERN, Has::cssTag ) );
		transformers.add( newTransformer( ID_PATTERN, Has::cssId ) );
		transformers.add( newTransformer( CLASS_PATTERN, Has::cssClass ) );
		transformers.add( newTransformer( ATTRIBUTE_PATTERN, Has::attribute ) );
		transformers.add( newTransformer( ATTRIBUTE_CONTAINING_PATTERN, Has::attributeContaining ) );
		transformers.add( newTransformer( ATTRIBUTE_STARTING_PATTERN, Has::attributeStarting ) );
		transformers.add( newTransformer( ATTRIBUTE_BEGINNING_PATTERN, Has::attributeBeginning ) );
		transformers.add( newTransformer( ATTRIBUTE_ENDING_PATTERN, Has::attributeEnding ) );
		transformers.add( newTransformer( ATTRIBUTE_CONTAINING_SUBSTRING_PATTERN, Has::attributeContainingSubstring ) );
		transformers.add( newTransformer( PSEUDO_CLASS_PATTERN, Has::cssPseudoClass ) );
		return transformers;
	}

	private static RegexTransformer newTransformer( final String pattern, final PredicateFactory factory ) {
		return new RegexTransformer( pattern, factory );
	}

}

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
	private static class Tupel {
		private final String pattern;
		private final Function<String, Predicate<Element>> factory;

		private Transformer createTransformer() {
			final Pattern cssTag = Pattern.compile( START_OF_LINE + pattern + REMAINING );
			return new Transformer( cssTag, factory );
		}
	}

	private static final String TAG_PATTERN = "([a-zA-Z0-9\\-]+)";
	private static final String ID_PATTERN = "\\#([a-zA-Z0-9\\-]+)";
	private static final String CLASS_PATTERN = "\\.([a-zA-Z0-9\\-]+)";
	private static final String ATTRIBUTE_PATTERN = "\\[([a-zA-Z0-9\\-=\"]+)\\]";
	private static final String REMAINING = "(.*)$";
	private static final String START_OF_LINE = "^";

	public static List<Transformer> all() {
		final LinkedList<Tupel> tupels = new LinkedList<>();
		tupels.add( new Tupel( TAG_PATTERN, Has::cssTag ) );
		tupels.add( new Tupel( ID_PATTERN, Has::cssId ) );
		tupels.add( new Tupel( CLASS_PATTERN, Has::cssClass ) );
		tupels.add( new Tupel( ATTRIBUTE_PATTERN, Has::cssAttribute ) );
		return tupels.stream().map( Tupel::createTransformer ).collect( toList() );
	}

}

package de.retest.web.mapping;

import java.awt.Rectangle;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.WebElement;

import de.retest.web.AttributesProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebElementWebDataProvider implements WebDataProvider {

	private final WebElement element;
	private final String path;

	private final AttributesProvider attributesProvider;

	@Override
	public Rectangle getAbsoluteOutline() {
		return null;
	}

	@Override
	public Rectangle getOutline() {
		final org.openqa.selenium.Rectangle origin = element.getRect();
		return new Rectangle( origin.getX(), origin.getY(), origin.getWidth(), origin.getHeight() );
	}

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public String getText() {
		return element.getText();
	}

	@Override
	public String getTag() {
		return element.getTagName();
	}

	@Override
	public Stream<Pair<String, String>> getHTMLAttributes() {
		if ( attributesProvider.allHtmlAttributes() ) {
			return Stream.empty(); // TODO: Do your best to gather all available html attributes.
		}
		return attributesProvider.getHtmlAttributes().stream() //
				.map( key -> Pair.of( key, element.getAttribute( key ) ) );
	}

	@Override
	public Stream<Pair<String, String>> getCSSAttributes() {
		return attributesProvider.getCssAttributes().stream() //
				.map( key -> Pair.of( key, element.getCssValue( key ) ) );
	}
}

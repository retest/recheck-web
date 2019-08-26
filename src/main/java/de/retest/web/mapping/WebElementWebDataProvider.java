package de.retest.web.mapping;

import java.awt.Rectangle;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;

import de.retest.web.AttributesProvider;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WebElementWebDataProvider implements WebDataProvider {

	private static final String GET_ALL_HTML_ATTRIBUTES = "var element = arguments[0];" + "var items = {};" //
			+ "for (i = 0; i < element.attributes.length; ++i) {" //
			+ "items[element.attributes[i].name] = element.attributes[i].value" //
			+ "};" //
			+ "return items;";

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
			return getAllHTMLAttributes();
		}
		return attributesProvider.getHtmlAttributes().stream() //
				.map( key -> Pair.of( key, element.getAttribute( key ) ) );
	}

	private Stream<Pair<String, String>> getAllHTMLAttributes() {
		if ( element instanceof WrapsDriver ) {
			final WebDriver driver = ((WrapsDriver) element).getWrappedDriver();
			if ( driver instanceof JavascriptExecutor ) {
				final Map<String, String> attributes = executeScript( (JavascriptExecutor) driver );
				return attributes.entrySet().stream() //
						.map( entry -> Pair.of( entry.getKey(), entry.getValue() ) );
			}
		}
		return Stream.empty(); // TODO: Do your best to gather all available html attributes.
	}

	@SuppressWarnings( "unchecked" )
	private Map<String, String> executeScript( final JavascriptExecutor executor ) {
		return (Map<String, String>) executor.executeScript( GET_ALL_HTML_ATTRIBUTES, element );
	}

	@Override
	public Stream<Pair<String, String>> getCSSAttributes() {
		return attributesProvider.getCssAttributes().stream() //
				.map( key -> Pair.of( key, element.getCssValue( key ) ) );
	}
}

package de.retest.web.mapping;

import java.awt.Rectangle;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;

import de.retest.web.AttributesProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
public class JavaScriptWebDataProvider implements WebDataProvider {

	private static final String GET_ALL_HTML_ATTRIBUTES = "var element = arguments[0];" + "var items = {};" //
			+ "for (i = 0; i < element.attributes.length; ++i) {" //
			+ "items[element.attributes[i].name] = element.attributes[i].value" //
			+ "};" //
			+ "return items;";

	private static final String CONVERT_ELEMENT_JS_PATH = "/javascript/convertElement.js";

	private final String path;
	private final WebData data;

	private final AttributesProvider attributesProvider;
	private final WebElement element;

	@Override
	public String getPath() {
		return path;
	}

	@Override
	public Rectangle getAbsoluteOutline() {
		return data.getAbsoluteOutline();
	}

	@Override
	public Rectangle getOutline() {
		return data.getOutline();
	}

	@Override
	public String getText() {
		return data.getText();
	}

	@Override
	public String getTag() {
		return data.getTag();
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
			final WebDriver driver = ( (WrapsDriver) element ).getWrappedDriver();
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

	@Slf4j
	@RequiredArgsConstructor
	public static class JavaScriptWebDataProviderFactory {

		private final AttributesProvider attributesProvider;

		private final String script = loadScript();

		private String loadScript() {
			try {
				final Path file = Paths.get( getClass().getResource( CONVERT_ELEMENT_JS_PATH ).toURI() );
				return String.join( "\n", Files.readAllLines( file ) );
			} catch ( final URISyntaxException | IOException e ) {
				log.error( "Could not read script {}.", CONVERT_ELEMENT_JS_PATH, e );
				return null;
			}
		}

		public <E extends WebElement & WrapsDriver> Optional<WebDataProvider> execute( final E element,
				final String path ) {
			final WebDriver driver = element.getWrappedDriver();
			return execute( driver, element, path );
		}

		public Optional<WebDataProvider> execute( final WebDriver driver, final WebElement element,
				final String path ) {
			if ( driver instanceof JavascriptExecutor ) {
				return execute( (JavascriptExecutor) driver, element, path );
			}
			return Optional.empty();
		}

		public Optional<WebDataProvider> execute( final JavascriptExecutor executor, final WebElement element,
				final String path ) {
			return executeScript( executor, element ) //
					.map( WebData::new ) //
					.map( data -> new JavaScriptWebDataProvider( path, data, attributesProvider, element ) );
		}

		@SuppressWarnings( "unchecked" )
		private Optional<Map<String, Object>> executeScript( final JavascriptExecutor executor,
				final WebElement element ) {
			if ( script == null ) {
				return Optional.empty();
			}
			return Optional.of( (Map<String, Object>) executor
					.executeScript( script, attributesProvider.getCssAttributes(), element ) );
		}
	}
}

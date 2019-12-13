package de.retest.web.testutils;

import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

public class PageFactory {

	private static final String BASE_PATH = "src/test/resources/pages/";

	public enum Page {
		SHOWCASE,
		SIMPLE_PAGE,
		FORM_PAGE,
		CENTER
	}

	public static String page( final Page page ) {
		switch ( page ) {
			case SHOWCASE: {
				return toPageUrlString( "showcase/retest.html" );
			}
			case SIMPLE_PAGE: {
				return toPageUrlString( "simple-page.html" );
			}
			case FORM_PAGE: {
				return toPageUrlString( "form-page.html" );
			}
			case CENTER: {
				return toPageUrlString( "centered.html" );
			}
			default:
				throw new IllegalArgumentException( "No \"" + page + "\" page available." );
		}
	}

	public static final String toPageUrlString( final String relativePath ) {
		try {
			return Paths.get( BASE_PATH, relativePath ).toUri().toURL().toString();
		} catch ( final MalformedURLException e ) {
			return null;
		}
	}

	public static Stream<String> pages() {
		return Arrays.stream( Page.values() ).map( PageFactory::page );
	}

}

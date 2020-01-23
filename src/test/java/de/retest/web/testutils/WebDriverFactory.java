package de.retest.web.testutils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

public class WebDriverFactory {

	public enum Driver {
		CHROME,
		FIREFOX
	}

	public static RemoteWebDriver driver( final Driver driver ) {
		switch ( driver ) {
			case CHROME: {
				return new ChromeDriver( new ChromeOptions().addArguments( commonArguments() ) );
			}
			case FIREFOX: {
				return new FirefoxDriver( new FirefoxOptions().addArguments( commonArguments() ) );
			}
			default:
				throw new IllegalArgumentException( "No \"" + driver + "\" driver available." );
		}
	}

	public static Stream<Arguments> drivers() {
		return Arrays.stream( Driver.values() ).map( WebDriverFactory::driver ).map( WebDriverFactory::toArguments );
	}

	private static Arguments toArguments( final WebDriver driver ) {
		return Arguments.of( driver, driver.getClass().getSimpleName() );
	}

	public static List<String> commonArguments() {
		return Arrays.asList(
				// Enable headless mode for faster execution.
				"--headless",
				// Fix window size for stable results.
				"--window-size=1200,800" );
	}

}

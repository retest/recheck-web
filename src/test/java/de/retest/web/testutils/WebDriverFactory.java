package de.retest.web.testutils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverFactory {

	public enum Driver {
		CHROME,
		FIREFOX;
	}

	public static WebDriver driver( final Driver driver ) {
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

	public static Stream<WebDriver> drivers() {
		return Arrays.stream( Driver.values() ).map( WebDriverFactory::driver );
	}

	public static List<String> commonArguments() {
		return Arrays.asList(
				// Enable headless mode for faster execution.
				"--headless",
				// Use Chrome in container-based Travis CI environment (see https://docs.travis-ci.com/user/chrome#Sandboxing).
				"--no-sandbox",
				// Fix window size for stable results.
				"--window-size=1200,800" );
	}

}

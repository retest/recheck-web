package de.retest.web.testutils;

import java.util.stream.Stream;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class WebDriverFactory {

	public enum Driver {
		CHROME_DRIVER,
		FIREFOX_DRIVER,
		EGE_DRIVER,
		IE_DRIVER,
		SAFARI_DRIVER
	}

	public static WebDriver driver( final Driver driver ) {
		switch ( driver ) {
			case CHROME_DRIVER: {
				return new ChromeDriver( new ChromeOptions().addArguments(
						// Enable headless mode for faster execution.
						"--headless",
						// Use Chrome in container-based Travis CI environment (see https://docs.travis-ci.com/user/chrome#Sandboxing).
						"--no-sandbox",
						// Fix window size for stable results.
						"--window-size=1200,800" ) );
			}
			case FIREFOX_DRIVER: {
				return new FirefoxDriver( new FirefoxOptions().addArguments(
						// Enable headless mode for faster execution.
						"--headless",
						// Use Firefox in container-based Travis CI environment.
						"--no-sandbox",
						// Fix window size for stable results.
						"--window-size=1200,800" ) );
			}
			default:
				throw new RuntimeException( "There is not a supported web-driver named " + driver );
		}
	}

	public static Stream<WebDriver> drivers() {
		return Stream.of( //
				(WebDriverFactory.driver( WebDriverFactory.Driver.CHROME_DRIVER )), //
				(WebDriverFactory.driver( WebDriverFactory.Driver.FIREFOX_DRIVER )) );
	}

}

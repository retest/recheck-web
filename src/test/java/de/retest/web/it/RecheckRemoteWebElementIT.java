package de.retest.web.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.junit.RecheckExtension;
import de.retest.web.selenium.By;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.PageFactory.Page;

@ExtendWith( RecheckExtension.class )
public class RecheckRemoteWebElementIT {

	private static final String HTML_ELEMENT = "html-element-";
	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@ParameterizedTest( name = HTML_ELEMENT + "{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void complete_html_should_be_same_as_driver( final WebDriver driver, final String name ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.tagName( "html" ) ), "open" );
	}

	@ParameterizedTest( name = HTML_ELEMENT + "{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void complete_html_should_be_same_as_web_element( final WebDriver driver, final String name ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		Thread.sleep( 1000 );

		re.check( driver, "open" );
	}

	@ParameterizedTest( name = "simple-webelement-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void no_children_webelement_should_be_checked( final WebDriver driver, final String name ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.id( "email" ) ), "open" );
	}

	@ParameterizedTest( name = "empty-article-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void empty_article_should_be_checked( final WebDriver driver, final String name ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		((JavascriptExecutor) driver).executeScript(
				"document.getElementsByTagName('body')[0].appendChild(document.createElement('article'))" );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.tagName( "article" ) ), "open" );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

}

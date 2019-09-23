package de.retest.web.it;

import static org.junit.jupiter.api.Assertions.fail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.web.selenium.By;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.PageFactory.Page;

public class RecheckRemoteWebElementIT {

	private static final String MISSING_ASSERTION_MSG = "An assertion error should have been produced";
	private static final String HTML_ELEMENT = "html-element-";
	private static final String SELECT_ELEMENT = "select-element-";
	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void complete_html_should_be_same_as_driver( final WebDriver driver ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		re.startTest( HTML_ELEMENT + driver.getClass().getSimpleName() );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.tagName( "html" ) ), "open" );

		re.capTest();
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void complete_html_should_be_same_as_web_element( final WebDriver driver ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		re.startTest( HTML_ELEMENT + driver.getClass().getSimpleName() );

		Thread.sleep( 1000 );

		re.check( driver, "open" );

		re.capTest();
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void no_children_webelement_should_be_checked( final WebDriver driver ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		re.startTest( "simple-webelement-" + driver.getClass().getSimpleName() );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.id( "email" ) ), "open" );

		re.capTest();
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void inserted_child_in_webelement_should_be_checked( final WebDriver driver ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		((JavascriptExecutor) driver)
				.executeScript( "document.getElementById('multi').add(document.createElement('option'))" );

		re.startTest( SELECT_ELEMENT + driver.getClass().getSimpleName() );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.id( "multi" ) ), "open" );

		try {
			re.capTest();
			fail( MISSING_ASSERTION_MSG );
		} catch ( final AssertionError e ) {
			Assertions.assertThat( e ).hasMessageContaining(
					"option at 'html[1]/body[1]/form[3]/select[2]/option[5]':\n		was inserted" );
		}
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void deleted_child_in_webelement_should_be_checked( final WebDriver driver ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		((JavascriptExecutor) driver).executeScript( "document.getElementById('multi').remove(3)" );

		re.startTest( SELECT_ELEMENT + driver.getClass().getSimpleName() );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.id( "multi" ) ), "open" );

		try {
			re.capTest();
			fail( MISSING_ASSERTION_MSG );
		} catch ( final AssertionError e ) {
			Assertions.assertThat( e ).hasMessageContaining(
					"option [Onion gravy] at 'html[1]/body[1]/form[3]/select[2]/option[4]':\n		was deleted" );
		}
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void empty_article_should_be_checked( final WebDriver driver ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.PAGE_FRAME ) );

		((JavascriptExecutor) driver).executeScript(
				"document.getElementsByTagName('body')[0].appendChild(document.createElement('article'))" );

		re.startTest( "empty-article-" + driver.getClass().getSimpleName() );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.tagName( "article" ) ), "open" );

		re.capTest();
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

}

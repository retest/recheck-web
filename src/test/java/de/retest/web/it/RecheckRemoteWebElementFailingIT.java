package de.retest.web.it;

import static org.junit.jupiter.api.Assertions.fail;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.web.selenium.By;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.PageFactory.Page;

class RecheckRemoteWebElementFailingIT {

	private static final String MISSING_ASSERTION_MSG = "An assertion error should have been produced";
	private static final String SELECT_ELEMENT = "select-element-";
	WebDriver driver;
	Recheck re;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	@Disabled("Temporarily disabled due to bug")
	void inserted_child_in_webelement_should_be_checked( final WebDriver driver ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.FORM_PAGE ) );

		((JavascriptExecutor) driver)
				.executeScript( "document.getElementById('multi').add(document.createElement('option'))" );

		re.startTest( SELECT_ELEMENT + driver.getClass().getSimpleName() );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.id( "multi" ) ), "open" );

		try {
			re.capTest();
			fail( MISSING_ASSERTION_MSG );
		} catch ( final AssertionError e ) {
			Assertions.assertThat( e )
					.hasMessageContaining( "option (option) at 'html[1]/body[1]/form[3]/select[2]/option[5]':\n" // 
							+ "\t\twas inserted" );
		}
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	@Disabled("Temporarily disabled due to bug")
	void deleted_child_in_webelement_should_be_checked( final WebDriver driver ) throws Exception {
		this.driver = driver;
		driver.get( PageFactory.page( Page.FORM_PAGE ) );

		((JavascriptExecutor) driver).executeScript( "document.getElementById('multi').remove(3)" );

		re.startTest( SELECT_ELEMENT + driver.getClass().getSimpleName() );

		Thread.sleep( 1000 );

		re.check( driver.findElement( By.id( "multi" ) ), "open" );

		try {
			re.capTest();
			fail( MISSING_ASSERTION_MSG );
		} catch ( final AssertionError e ) {
			Assertions.assertThat( e ).hasMessageContaining(
					"option (onion_gravy) at 'html[1]/body[1]/form[3]/select[2]/option[4]':\n		was deleted" );
		}
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

}

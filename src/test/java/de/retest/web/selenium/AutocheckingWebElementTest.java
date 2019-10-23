package de.retest.web.selenium;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

class AutocheckingWebElementTest {

	@Test
	void check_is_skiped_when_skipCheck_is_called() throws Exception {
		final WebElement delegate = mock( WebElement.class );
		final AutocheckingRecheckDriver driver = mock( AutocheckingRecheckDriver.class );
		final AutocheckingWebElement autoCheckElement = new AutocheckingWebElement( delegate, driver );

		autoCheckElement.skipCheck().clear();
		verify( delegate, times( 1 ) ).clear();
		verify( driver, never() ).check( any(), any() );

		autoCheckElement.skipCheck().submit();
		verify( delegate, times( 1 ) ).submit();
		verify( driver, never() ).check( any(), any() );

		autoCheckElement.skipCheck().sendKeys();
		verify( delegate, times( 1 ) ).sendKeys();
		verify( driver, never() ).check( any(), any() );

		autoCheckElement.skipCheck().click();
		verify( delegate, times( 1 ) ).click();
		verify( driver, never() ).check( any(), any() );

	}

	@Test
	void changing_methods_should_check_and_delegate_calls() {
		final WebElement delegate = mock( WebElement.class );
		final AutocheckingRecheckDriver driver = mock( AutocheckingRecheckDriver.class );
		final AutocheckingWebElement autoCheckElement = new AutocheckingWebElement( delegate, driver );

		autoCheckElement.clear();
		verify( delegate, times( 1 ) ).clear();
		verify( driver, times( 1 ) ).check( any(), any() );

		autoCheckElement.submit();
		verify( delegate, times( 1 ) ).submit();
		verify( driver, times( 2 ) ).check( any(), any() );

		autoCheckElement.sendKeys();
		verify( delegate, times( 1 ) ).sendKeys();
		verify( driver, times( 3 ) ).check( any(), any() );

		autoCheckElement.click();
		verify( delegate, times( 1 ) ).click();
		verify( driver, times( 4 ) ).check( any(), any() );
	}

	@Test
	void other_methods_should_delegate_calls() {
		final WebElement delegate = mock( WebElement.class );
		final AutocheckingWebElement autoCheckElement =
				new AutocheckingWebElement( delegate, mock( AutocheckingRecheckDriver.class ) );

		autoCheckElement.getAttribute( "test" );
		verify( delegate, times( 1 ) ).getAttribute( "test" );

		autoCheckElement.getCssValue( "css" );
		verify( delegate, times( 1 ) ).getCssValue( "css" );

		autoCheckElement.getLocation();
		verify( delegate, times( 1 ) ).getLocation();

		autoCheckElement.getRect();
		verify( delegate, times( 1 ) ).getRect();

		autoCheckElement.getSize();
		verify( delegate, times( 1 ) ).getSize();

		autoCheckElement.getTagName();
		verify( delegate, times( 1 ) ).getTagName();

		autoCheckElement.getText();
		verify( delegate, times( 1 ) ).getText();

		autoCheckElement.isDisplayed();
		verify( delegate, times( 1 ) ).isDisplayed();

		autoCheckElement.isEnabled();
		verify( delegate, times( 1 ) ).isEnabled();

		autoCheckElement.isSelected();
		verify( delegate, times( 1 ) ).isSelected();
	}
}

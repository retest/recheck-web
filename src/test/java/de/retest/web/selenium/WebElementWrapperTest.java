package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebElement;

class WebElementWrapperTest {

	@Test
	void toStep_should_return_simple_description_for_id() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() )
				.thenReturn( "[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> id: signupEmail]" );
		assertThat( WebElementWrapper.toStep( "click", mock ) ).isEqualTo( "click_signupEmail" );
	}

	@Test
	void toStep_should_return_simple_description_for_name() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() )
				.thenReturn( "[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> name: logo]" );
		assertThat( WebElementWrapper.toStep( "click", mock ) ).isEqualTo( "click_logo" );
	}

	@Test
	void toStep_should_return_simple_description_for_class() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() ).thenReturn(
				"[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> class name: sign-up-submit]" );
		assertThat( WebElementWrapper.toStep( "click", mock ) ).isEqualTo( "click_sign-up-submit" );
	}

	@Test
	void toStep_should_return_simple_description_for_link() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() ).thenReturn(
				"[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> link text: Contact]" );
		assertThat( WebElementWrapper.toStep( "click", mock ) ).isEqualTo( "click_Contact" );
	}

	@Test
	void toStep_should_contain_text_for_sendKeys() {
		final WebElement delegate = mock( WebElement.class );
		when( delegate.toString() ).thenReturn(
				"[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> link text: Contact]" );
		final AutocheckingRecheckDriver driver = mock( AutocheckingRecheckDriver.class );
		final WebElementWrapper wrapper = new WebElementWrapper( delegate, driver );

		wrapper.sendKeys( "this very long text was sent" );
		verify( driver, times( 1 ) ).check( "enter_[this ve...]_into_Contact" );
	}

	@Test
	void changing_methods_should_check_and_delegate_calls() {
		final WebElement delegate = mock( WebElement.class );
		final AutocheckingRecheckDriver driver = mock( AutocheckingRecheckDriver.class );
		final WebElementWrapper wrapper = new WebElementWrapper( delegate, driver );

		wrapper.clear();
		verify( delegate, times( 1 ) ).clear();
		verify( driver, times( 1 ) ).check( Mockito.any() );

		wrapper.submit();
		verify( delegate, times( 1 ) ).submit();
		verify( driver, times( 2 ) ).check( Mockito.any() );

		wrapper.sendKeys();
		verify( delegate, times( 1 ) ).sendKeys();
		verify( driver, times( 3 ) ).check( Mockito.any() );

		wrapper.click();
		verify( delegate, times( 1 ) ).click();
		verify( driver, times( 4 ) ).check( Mockito.any() );
	}

	@Test
	void other_methods_should_delegate_calls() {
		final WebElement delegate = mock( WebElement.class );
		final WebElementWrapper wrapper = new WebElementWrapper( delegate, mock( AutocheckingRecheckDriver.class ) );

		wrapper.getAttribute( "test" );
		verify( delegate, times( 1 ) ).getAttribute( "test" );

		wrapper.getCssValue( "css" );
		verify( delegate, times( 1 ) ).getCssValue( "css" );

		wrapper.getLocation();
		verify( delegate, times( 1 ) ).getLocation();

		wrapper.getRect();
		verify( delegate, times( 1 ) ).getRect();

		wrapper.getSize();
		verify( delegate, times( 1 ) ).getSize();

		wrapper.getTagName();
		verify( delegate, times( 1 ) ).getTagName();

		wrapper.getText();
		verify( delegate, times( 1 ) ).getText();

		wrapper.isDisplayed();
		verify( delegate, times( 1 ) ).isDisplayed();

		wrapper.isEnabled();
		verify( delegate, times( 1 ) ).isEnabled();

		wrapper.isSelected();
		verify( delegate, times( 1 ) ).isSelected();
	}
}

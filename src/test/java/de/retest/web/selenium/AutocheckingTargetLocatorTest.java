package de.retest.web.selenium;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

class AutocheckingTargetLocatorTest {

	WebDriver.TargetLocator delegate;
	AutocheckingRecheckDriver driver;

	AutocheckingTargetLocator cut;

	@BeforeEach
	void setUp() {
		delegate = mock( WebDriver.TargetLocator.class );
		driver = mock( AutocheckingRecheckDriver.class );

		cut = new AutocheckingTargetLocator( delegate, driver );
	}

	@Test
	void frame_int_should_call_delegate_method() {
		cut.frame( 0 );

		verify( delegate, only() ).frame( 0 );
	}

	@Test
	void frame_string_should_call_delegate_method() {
		cut.frame( "name" );

		verify( delegate, only() ).frame( "name" );
	}

	@Test
	void parentFrame_should_call_delegate_method() {
		cut.parentFrame();

		verify( delegate, only() ).parentFrame();
	}

	@Test
	void window_should_call_delegate_method() {
		cut.window( "name" );

		verify( delegate, only() ).window( "name" );
	}

	@Test
	void defaultContent_should_call_delegate_method() {
		cut.defaultContent();

		verify( delegate, only() ).defaultContent();
	}

	@Test
	void activeElement_should_call_delegate_method() {
		cut.activeElement();

		verify( delegate, only() ).activeElement();
	}

	@Test
	void alert_should_call_delegate_method() {
		cut.alert();

		verify( delegate, only() ).alert();
	}

	@Test
	void window_should_perform_check() {
		cut.window( "name" );

		verify( driver, only() ).check( "switch-window" );
	}
}

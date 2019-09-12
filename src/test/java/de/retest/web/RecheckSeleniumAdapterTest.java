package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.safari.SafariDriver;

import de.retest.recheck.RecheckAdapter;
import de.retest.web.selenium.AutocheckingRecheckDriver;
import de.retest.web.selenium.RecheckDriver;
import de.retest.web.selenium.UnbreakableDriver;

class RecheckSeleniumAdapterTest {

	RecheckAdapter cut;

	@BeforeEach
	void setUp() throws Exception {
		cut = new RecheckSeleniumAdapter();
	}

	@ParameterizedTest
	@ValueSource( classes = { ChromeDriver.class, EdgeDriver.class, FirefoxDriver.class, InternetExplorerDriver.class,
			OperaDriver.class, SafariDriver.class } )
	void canCheck_should_accept_common_drivers( final Class<?> clazz ) {
		assertThat( cut.canCheck( mock( clazz ) ) ).isTrue();
	}

	@ParameterizedTest
	@ValueSource( classes = { UnbreakableDriver.class, AutocheckingRecheckDriver.class, RecheckDriver.class } )
	void canCheck_should_accept_recheck_drivers( final Class<WrapsDriver> clazz ) {
		final WrapsDriver mock = mock( clazz );
		when( mock.getWrappedDriver() ).thenReturn( mock( RemoteWebDriver.class ) );
		assertThat( cut.canCheck( mock ) ).isTrue();
	}

	@Test
	void canCheck_should_reject_Object() {
		assertThat( cut.canCheck( new Object() ) ).isFalse();
	}

	@Test
	void convert_invalid_should_throw_exception() {
		assertThatThrownBy( () -> cut.convert( new Object() ) ).isExactlyInstanceOf( IllegalArgumentException.class )
				.hasMessageContaining( "Cannot convert objects of type 'java.lang.Object'." );
	}

	@Test
	void canCheck_should_handle_WrapsElements() throws Exception {
		final WrappingRemoteWebElement inner = mock( WrappingRemoteWebElement.class );
		when( inner.getWrappedElement() ).thenReturn( mock( RemoteWebElement.class ) );

		final WrappingRemoteWebElement outer = mock( WrappingRemoteWebElement.class );
		when( outer.getWrappedElement() ).thenReturn( inner );

		assertThat( cut.canCheck( outer ) ).isTrue();
		verify( outer ).getWrappedElement();
		verify( inner ).getWrappedElement();

		verify( outer, never() ).getWrappedDriver();
		verify( inner, never() ).getWrappedDriver();
	}

	@Test
	void canCheck_should_handle_WrapsDriver() throws Exception {
		final WrappingRemoteWebDriver inner = mock( WrappingRemoteWebDriver.class );
		when( inner.getWrappedDriver() ).thenReturn( mock( RemoteWebDriver.class ) );

		final WrappingRemoteWebDriver outer = mock( WrappingRemoteWebDriver.class );
		when( outer.getWrappedDriver() ).thenReturn( inner );

		assertThat( cut.canCheck( outer ) ).isTrue();
		verify( outer ).getWrappedDriver();
		verify( inner ).getWrappedDriver();
	}

	@Test
	void canCheck_should_accept_RemoteWebElement() throws Exception {
		assertThat( cut.canCheck( mock( RemoteWebElement.class ) ) ).isTrue();
	}

	@Test
	void canCheck_should_accept_RemoteWebDriver() throws Exception {
		assertThat( cut.canCheck( mock( RemoteWebDriver.class ) ) ).isTrue();
	}

	@Test
	void canCheck_should_reject_WebElement() throws Exception {
		assertThat( cut.canCheck( mock( WebElement.class ) ) ).isFalse();
	}

	@Test
	void canCheck_should_reject_WebDriver() throws Exception {
		assertThat( cut.canCheck( mock( WebDriver.class ) ) ).isFalse();
	}

	class WrappingRemoteWebElement extends RemoteWebElement implements WrapsElement {

		@Override
		public WebElement getWrappedElement() {
			return null;
		}

	}

	class WrappingRemoteWebDriver extends RemoteWebDriver implements WrapsDriver {

		@Override
		public WebDriver getWrappedDriver() {
			return null;
		}
	}

}

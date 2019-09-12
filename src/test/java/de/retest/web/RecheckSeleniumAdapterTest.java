package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import de.retest.recheck.RecheckAdapter;
import de.retest.web.selenium.WebElementWrapper;

class RecheckSeleniumAdapterTest {

	RecheckAdapter cut;

	@BeforeEach
	void setUp() throws Exception {
		cut = new RecheckSeleniumAdapter();
	}

	@Test
	void seleniumAdapter_canCheck_Driver() {
		assertThat( cut.canCheck( mock( ChromeDriver.class ) ) ).isTrue();
		assertThat( cut.canCheck( mock( FirefoxDriver.class ) ) ).isTrue();
		assertThat( cut.canCheck( mock( EdgeDriver.class ) ) ).isTrue();
		assertThat( cut.canCheck( mock( InternetExplorerDriver.class ) ) ).isTrue();
		assertThat( cut.canCheck( mock( RemoteWebDriver.class ) ) ).isTrue();
	}

	@Test
	void seleniumAdapter_canCheck_WebElements() {
		assertThat( cut.canCheck( mock( RemoteWebElement.class ) ) ).isTrue();

		final WebElementWrapper mock = mock( WebElementWrapper.class );
		when( mock.getWrappedElement() ).thenReturn( mock( RemoteWebElement.class ) );
		assertThat( cut.canCheck( mock ) ).isTrue();

		final WrapsElement mockedWrap = mock( WrapsElement.class );
		when( mockedWrap.getWrappedElement() ).thenReturn( mock( RemoteWebElement.class ) );
		assertThat( cut.canCheck( mockedWrap ) ).isTrue();
	}

	@Test
	void seleniumAdapter_can_NOT_Check_Object() {
		assertThat( cut.canCheck( new Object() ) ).isFalse();
	}

	@Test
	void convert_invalid_should_throw_exception() {
		assertThatThrownBy( () -> {
			cut.convert( new Object() );
		} ).isInstanceOf( IllegalArgumentException.class )
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

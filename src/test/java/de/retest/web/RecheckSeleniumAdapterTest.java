package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import de.retest.web.selenium.WebElementWrapper;

class RecheckSeleniumAdapterTest {

	@Test
	void seleniumAdapter_canCheck_Driver() {
		final RecheckSeleniumAdapter cut = new RecheckSeleniumAdapter();

		assertThat( cut.canCheck( mock( ChromeDriver.class ) ) ).isTrue();
		assertThat( cut.canCheck( mock( FirefoxDriver.class ) ) ).isTrue();
		assertThat( cut.canCheck( mock( EdgeDriver.class ) ) ).isTrue();
		assertThat( cut.canCheck( mock( InternetExplorerDriver.class ) ) ).isTrue();
		assertThat( cut.canCheck( mock( RemoteWebDriver.class ) ) ).isTrue();
	}

	@Test
	void seleniumAdapter_canCheck_WebElements() {
		final RecheckSeleniumAdapter cut = new RecheckSeleniumAdapter();
		assertThat( cut.canCheck( mock( RemoteWebElement.class ) ) ).isTrue();

		final WebElementWrapper mock = mock( WebElementWrapper.class );
		Mockito.when( mock.getWrappedElement() ).thenReturn( mock( RemoteWebElement.class ) );
		assertThat( cut.canCheck( mock ) ).isTrue();

		final WrapsElement mockedWrap = mock( WrapsElement.class );
		Mockito.when( mockedWrap.getWrappedElement() ).thenReturn( mock( RemoteWebElement.class ) );
		assertThat( cut.canCheck( mockedWrap ) ).isTrue();
	}

	@Test
	void seleniumAdapter_can_NOT_Check_Object() {
		final RecheckSeleniumAdapter cut = new RecheckSeleniumAdapter();
		assertThat( cut.canCheck( new Object() ) ).isFalse();
	}

	@Test
	void convert_invalid_should_throw_exception() {
		final RecheckSeleniumAdapter cut = new RecheckSeleniumAdapter();
		assertThatThrownBy( () -> {
			cut.convert( new Object() );
		} ).isInstanceOf( IllegalArgumentException.class )
				.hasMessageContaining( "Cannot convert objects of class java.lang.Object" );
	}
}

package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.safari.SafariDriver;

import de.retest.recheck.RecheckAdapter;
import de.retest.web.selenium.AutocheckingRecheckDriver;
import de.retest.web.selenium.ImplicitDriverWrapper;
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

	@ParameterizedTest
	@ValueSource( classes = { WrapsDriver.class, org.openqa.selenium.WrapsDriver.class } )
	void canCheck_should_accept_new_and_old_driver_wrappers( final Class<?> clazz ) {
		final org.openqa.selenium.WrapsDriver mock = (org.openqa.selenium.WrapsDriver) mock( clazz );
		when( mock.getWrappedDriver() ).thenReturn( mock( RemoteWebDriver.class ) );
		assertThat( cut.canCheck( mock ) ).isTrue();
	}

	@ParameterizedTest
	@ValueSource( classes = { WrapsElement.class, org.openqa.selenium.WrapsElement.class } )
	void canCheck_should_accept_new_and_old_element_wrappers( final Class<?> clazz ) {
		final org.openqa.selenium.WrapsElement mock = (org.openqa.selenium.WrapsElement) mock( clazz );
		when( mock.getWrappedElement() ).thenReturn( mock( RemoteWebElement.class ) );
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
		final WrappingRemoteWebElement inner = createInnerWrappingElement();

		final WrappingRemoteWebElement outer = createOuterWrappingElement( inner );

		assertThat( cut.canCheck( outer ) ).isTrue();
		verify( outer ).getWrappedElement();
		verify( inner ).getWrappedElement();

		verify( outer, never() ).getWrappedDriver();
		verify( inner, never() ).getWrappedDriver();
	}

	@Test
	void canCheck_should_handle_WrapsDriver() throws Exception {
		final WrappingRemoteWebDriver inner = createInnerWrappingDriver();
		final WrappingRemoteWebDriver outer = createOuterWrappingDriver( inner );

		assertThat( cut.canCheck( outer ) ).isTrue();
		verify( outer ).getWrappedDriver();
		verify( inner ).getWrappedDriver();
	}

	@Test
	void canCheck_should_not_unwrap_UnbreakableDriver() {
		final UnbreakableDriver driver = mock( UnbreakableDriver.class );

		assertThat( cut.canCheck( driver ) ).isTrue();
		verify( driver, never() ).getWrappedDriver();
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

	@Test
	void convert_should_handle_WrapsElement() {
		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter() );
		doReturn( Collections.emptySet() ).when( cut ).convertWebElement( any() );

		final WrappingRemoteWebElement inner = createInnerWrappingElement();
		final WrappingRemoteWebElement outer = createOuterWrappingElement( inner );

		assertThat( cut.convert( outer ) ).isEmpty();
		verify( cut ).convertWebElement( (RemoteWebElement) inner.getWrappedElement() );
	}

	@Test
	void convert_should_handle_WrapsDriver() {
		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter() );
		doReturn( Collections.emptySet() ).when( cut ).convertWebDriver( any() );

		final WrappingRemoteWebDriver inner = createInnerWrappingDriver();
		final WrappingRemoteWebDriver outer = createOuterWrappingDriver( inner );

		assertThat( cut.convert( outer ) ).isEmpty();
		verify( cut ).convertWebDriver( inner.getWrappedDriver() );
	}

	@Test
	void convert_should_not_unwrap_UnbreakableDriver() {
		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter() );
		doReturn( Collections.emptySet() ).when( cut ).convertWebDriver( any() );

		final UnbreakableDriver driver = mock( UnbreakableDriver.class );

		assertThat( cut.convert( driver ) ).isEmpty();
		verify( driver, never() ).getWrappedDriver();
	}

	@Test
	void convert_should_accept_RemoteWebElement() {
		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter() );
		doReturn( Collections.emptySet() ).when( cut ).convertWebElement( any() );

		assertThat( cut.convert( mock( RemoteWebElement.class ) ) ).isEmpty();
		verify( cut, never() ).convertWebDriver( any() );
	}

	@Test
	void convert_should_accept_RemoteWebDriver() {
		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter() );
		doReturn( Collections.emptySet() ).when( cut ).convertWebDriver( any() );

		assertThat( cut.convert( mock( RemoteWebDriver.class ) ) ).isEmpty();
		verify( cut, never() ).convertWebElement( any() );
	}

	@Test
	void convert_should_reject_WebElement() throws Exception {
		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter() );
		doReturn( Collections.emptySet() ).when( cut ).convertWebElement( any() );

		assertThatCode( () -> cut.convert( mock( WebElement.class ) ) ).isInstanceOf( IllegalArgumentException.class );
	}

	@Test
	void convert_should_reject_WebDriver() throws Exception {
		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter() );
		doReturn( Collections.emptySet() ).when( cut ).convertWebDriver( any() );

		assertThatCode( () -> cut.convert( mock( WebDriver.class ) ) ).isInstanceOf( IllegalArgumentException.class );
	}

	@Test
	void convert_should_throw_with_autochecking_driver() throws Exception {
		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter() );
		doReturn( Collections.emptySet() ).when( cut ).convertWebDriver( any() );

		final AutocheckingRecheckDriver driver = mock( AutocheckingRecheckDriver.class );

		assertThatThrownBy( () -> cut.convert( driver ) ) //
				.isInstanceOf( UnsupportedOperationException.class ) //
				.hasMessageStartingWith( String.format(
						"Mixing implicit checking used by '%s' and explicit checking with 'Recheck#check' is not supported.",
						driver.getClass().getSimpleName() ) );
	}

	@Test
	void convert_should_not_walk_through_implicit_checking_if_unbreakable() throws Exception {
		final UnbreakableDriver unbreakableDriver = mock( UnbreakableDriver.class );

		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter() );
		doReturn( Collections.emptySet() ).when( cut ).convertWebDriver( unbreakableDriver );

		final ImplicitDriverWrapper wrapper = mock( ImplicitDriverWrapper.class );
		when( wrapper.getWrappedDriver() ).thenReturn( unbreakableDriver );

		assertThat( cut.convert( wrapper ) ).isNotNull();
	}

	private WrappingRemoteWebElement createOuterWrappingElement( final WrappingRemoteWebElement inner ) {
		final WrappingRemoteWebElement outer = mock( WrappingRemoteWebElement.class );
		when( outer.getWrappedElement() ).thenReturn( inner );
		return outer;
	}

	private WrappingRemoteWebElement createInnerWrappingElement() {
		final WrappingRemoteWebElement inner = mock( WrappingRemoteWebElement.class );
		when( inner.getWrappedElement() ).thenReturn( mock( RemoteWebElement.class ) );
		return inner;
	}

	private WrappingRemoteWebDriver createOuterWrappingDriver( final WrappingRemoteWebDriver inner ) {
		final WrappingRemoteWebDriver outer = mock( WrappingRemoteWebDriver.class );
		when( outer.getWrappedDriver() ).thenReturn( inner );
		return outer;
	}

	private WrappingRemoteWebDriver createInnerWrappingDriver() {
		final WrappingRemoteWebDriver inner = mock( WrappingRemoteWebDriver.class );
		when( inner.getWrappedDriver() ).thenReturn( mock( RemoteWebDriver.class ) );
		return inner;
	}

	static class WrappingRemoteWebElement extends RemoteWebElement implements WrapsElement {

		@Override
		public WebElement getWrappedElement() {
			return null;
		}

	}

	static class WrappingRemoteWebDriver extends RemoteWebDriver implements WrapsDriver {

		@Override
		public WebDriver getWrappedDriver() {
			return null;
		}
	}

}

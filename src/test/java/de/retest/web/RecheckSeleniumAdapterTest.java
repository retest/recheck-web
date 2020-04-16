package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
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
import de.retest.recheck.report.ActionReplayResult;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.diff.ElementIdentificationWarning;
import de.retest.web.screenshot.NoScreenshot;
import de.retest.web.selenium.AutocheckingRecheckDriver;
import de.retest.web.selenium.ImplicitDriverWrapper;
import de.retest.web.selenium.QualifiedElementWarning;
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
						"The '%s' does implicit checking after each action, therefore no explicit check with 'Recheck#check' is needed",
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

	@Test
	void convert_and_notify_should_allow_for_warnings_to_be_issued() throws Exception {
		final ElementIdentificationWarning warning = new ElementIdentificationWarning( "test.java", 0, "id", "Test" );
		final QualifiedElementWarning qualifiedElementWarning = new QualifiedElementWarning( "id", "id", warning );

		final UnbreakableDriver unbreakableDriver = mock( UnbreakableDriver.class );
		when( unbreakableDriver.executeScript( any(), any() ) ).thenReturn( Collections.emptyList() );

		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter( RecheckWebOptions.builder() //
				.screenshotProvider( new NoScreenshot() ) //
				.build() ) );
		doReturn( mock( RootElement.class ) ).when( cut ).convert( anyList(), nullable( String.class ),
				nullable( String.class ), nullable( BufferedImage.class ) );

		cut.convert( unbreakableDriver );

		final ArgumentCaptor<Consumer<QualifiedElementWarning>> captor = ArgumentCaptor.forClass( Consumer.class );
		verify( unbreakableDriver ).setWarningConsumer( captor.capture() );

		cut.notifyAboutDifferences( mock( ActionReplayResult.class ) );

		final Consumer<QualifiedElementWarning> warningConsumer = captor.getValue();

		assertThatCode( () -> {
			warningConsumer.accept( qualifiedElementWarning );
		} ).doesNotThrowAnyException();
	}

	@Test
	void convert_without_notify_should_not_throw_exception() throws Exception {
		final ElementIdentificationWarning warning = new ElementIdentificationWarning( "test.java", 0, "id", "Test" );
		final QualifiedElementWarning qualifiedElementWarning = new QualifiedElementWarning( "id", "id", warning );

		final UnbreakableDriver unbreakableDriver = mock( UnbreakableDriver.class );
		when( unbreakableDriver.executeScript( any(), any() ) ).thenReturn( Collections.emptyList() );

		final RecheckSeleniumAdapter cut = spy( new RecheckSeleniumAdapter( RecheckWebOptions.builder() //
				.screenshotProvider( new NoScreenshot() ) //
				.build() ) );
		doReturn( mock( RootElement.class ) ).when( cut ).convert( anyList(), nullable( String.class ),
				nullable( String.class ), nullable( BufferedImage.class ) );

		cut.convert( unbreakableDriver );

		final ArgumentCaptor<Consumer<QualifiedElementWarning>> captor = ArgumentCaptor.forClass( Consumer.class );
		verify( unbreakableDriver ).setWarningConsumer( captor.capture() );

		final Consumer<QualifiedElementWarning> warningConsumer = captor.getValue();

		assertThatCode( () -> {
			warningConsumer.accept( qualifiedElementWarning );
		} ).doesNotThrowAnyException();
	}

	@Test
	void convert_should_preserve_order_of_elements() {
		final RecheckSeleniumAdapter cut = new RecheckSeleniumAdapter( RecheckWebOptions.builder() //
				.screenshotProvider( new NoScreenshot() ) //
				.build() );
		final List<List<Object>> tagMapping = new ArrayList<>();
		tagMapping.add( Arrays.asList( "//html[1]", makeShown( toMap( "tagName", "html" ) ) ) );
		tagMapping.add( Arrays.asList( "//html[1]/div[1]", makeShown( toMap( "tagName", "div" ) ) ) );
		tagMapping.add( Arrays.asList( "//html[1]/a[1]", makeShown( toMap( "tagName", "a" ) ) ) );
		final RootElement root = cut.convert( tagMapping, "url", "title", null );
		assertThat( root.getContainedElements().toString() ).isEqualTo( "[div, a]" );
	}

	private Map<String, Object> makeShown( final Map<String, Object> map ) {
		map.put( "shown", true );
		map.put( "x", 0 );
		map.put( "y", 0 );
		map.put( "width", 10 );
		map.put( "height", 10 );
		return map;
	}

	private Map<String, Object> toMap( final Object... keysAndValues ) {
		final Map<String, Object> result = new HashMap<>();
		for ( int idx = 0; idx < keysAndValues.length; ) {
			result.put( (String) keysAndValues[idx++], keysAndValues[idx++] );
		}
		return result;
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

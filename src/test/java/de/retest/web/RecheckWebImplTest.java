package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.persistence.SeparatePathsProjectLayout;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.web.selenium.ImplicitDriverWrapper;
import de.retest.web.selenium.UnbreakableDriver;

class RecheckWebImplTest {

	RecheckWebImpl re;
	RecheckAdapter adapter;

	@BeforeEach
	void setUp( @TempDir final Path project ) {
		final Path goldenMasterDirectory = project.resolve( "state" );
		final Path reportDirectory = project.resolve( "report" );
		final RecheckOptions options = RecheckWebOptions.builder()
				.projectLayout( new SeparatePathsProjectLayout( goldenMasterDirectory, reportDirectory ) ) //
				.build();

		final RootElement rootElement = mock( RootElement.class, RETURNS_MOCKS );

		re = new RecheckWebImpl( options );
		adapter = mock( RecheckAdapter.class );
		when( adapter.convert( any() ) ).thenReturn( Collections.singleton( rootElement ) );
	}

	@Test
	void check_should_properly_extract_unbreakable_driver() throws Exception {
		re.startTest();

		re.check( mock( UnbreakableDriver.class ), adapter, "unbreakable" );

		assertThat( re.getDriver() ).isNotNull();
		assertThatThrownBy( () -> re.capTest() ).isInstanceOf( AssertionError.class );
	}

	@Test
	void check_should_properly_fail_if_no_unbreakable_driver() throws Exception {
		re.startTest();

		assertThatThrownBy( () -> {
			re.check( mock( RemoteWebDriver.class ), adapter, "unbreakable" );
		} ) //
				.isInstanceOf( IllegalStateException.class ) //
				.hasMessage(
						"Must first call a check-method with an UnbreakableDriver before being able to load a Golden Master (needed for unbreakable tests)!" );

		assertThat( re.getDriver() ).isNull();
		re.capTest();
	}

	@Test
	void check_should_properly_extract_unbreakable_from_implicit_wrapper() throws Exception {
		re.startTest();

		final UnbreakableDriver unbreakableDriver = mock( UnbreakableDriver.class );
		final ImplicitDriverWrapper wrapper = mock( ImplicitDriverWrapper.class );
		when( wrapper.getWrappedDriver() ).thenReturn( unbreakableDriver );

		re.check( wrapper, adapter, "unbreakable" );

		assertThat( re.getDriver() ).isNotNull();
		assertThatThrownBy( () -> re.capTest() ).isInstanceOf( AssertionError.class );
	}

	@Test
	void check_should_properly_fail_if_no_unbreakable_driver_from_implicit_wrapper() throws Exception {
		re.startTest();

		final RemoteWebDriver driver = mock( RemoteWebDriver.class );
		final ImplicitDriverWrapper wrapper = mock( ImplicitDriverWrapper.class );
		when( wrapper.getWrappedDriver() ).thenReturn( driver );

		assertThatThrownBy( () -> {
			re.check( wrapper, adapter, "unbreakable" );
		} ) //
				.isInstanceOf( IllegalStateException.class ) //
				.hasMessage(
						"Must first call a check-method with an UnbreakableDriver before being able to load a Golden Master (needed for unbreakable tests)!" );

		assertThat( re.getDriver() ).isNull();
		re.capTest();
	}

	@Test
	void check_should_allow_for_web_elements_to_be_passed_which_extracts_the_driver() {
		re.startTest();

		final WebDriver driver = mock( UnbreakableDriver.class );

		final RemoteWebElement element = mock( RemoteWebElement.class );
		when( element.getWrappedDriver() ).thenReturn( driver );

		re.check( element, adapter, "element_should_not_throw_exception" );

		assertThat( re.getDriver() ).isEqualTo( driver );
		assertThatThrownBy( () -> re.capTest() ).isInstanceOf( AssertionError.class );
	}

	@Test
	void check_should_allow_for_web_elements_to_be_passed_which_extracts_the_wrapped_driver() {
		re.startTest();

		final WebDriver wrapped = mock( UnbreakableDriver.class );

		final WrappingDriver driver = mock( WrappingDriver.class );
		when( driver.getWrappedDriver() ).thenReturn( wrapped );

		final RemoteWebElement element = mock( RemoteWebElement.class );
		when( element.getWrappedDriver() ).thenReturn( driver );

		re.check( element, adapter, "element_should_not_throw_exception" );

		assertThat( re.getDriver() ).isEqualTo( wrapped );
		assertThatThrownBy( () -> re.capTest() ).isInstanceOf( AssertionError.class );
	}

	@Test
	void check_should_allow_for_web_elements_to_be_passed_which_extracts_the_legacy_wrapped_driver() {
		re.startTest();

		final WebDriver wrapped = mock( UnbreakableDriver.class );

		final LegacyWrappingDriver driver = mock( LegacyWrappingDriver.class );
		when( driver.getWrappedDriver() ).thenReturn( wrapped );

		final RemoteWebElement element = mock( RemoteWebElement.class );
		when( element.getWrappedDriver() ).thenReturn( driver );

		re.check( element, adapter, "element_should_not_throw_exception" );

		assertThat( re.getDriver() ).isEqualTo( wrapped );
		assertThatThrownBy( () -> re.capTest() ).isInstanceOf( AssertionError.class );
	}

	interface WrappingDriver extends WebDriver, WrapsDriver {}

	interface LegacyWrappingDriver extends WebDriver, org.openqa.selenium.internal.WrapsDriver {}
}

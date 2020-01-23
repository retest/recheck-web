package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.remote.RemoteWebDriver;

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

	@AfterEach
	void tearDown() {
		re.cap();
	}

	@Test
	void check_should_properly_extract_unbreakable_driver() throws Exception {
		re.startTest();

		re.check( mock( UnbreakableDriver.class ), adapter, "unbreakable" );

		assertThat( re.getDriver() ).isNotNull();

		capTestSilently();
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

		capTestSilently();
	}

	@Test
	void check_should_properly_extract_unbreakable_from_implicit_wrapper() throws Exception {
		re.startTest();

		final UnbreakableDriver unbreakableDriver = mock( UnbreakableDriver.class );
		final ImplicitDriverWrapper wrapper = mock( ImplicitDriverWrapper.class );
		when( wrapper.getWrappedDriver() ).thenReturn( unbreakableDriver );

		re.check( wrapper, adapter, "unbreakable" );

		assertThat( re.getDriver() ).isNotNull();

		capTestSilently();
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

		capTestSilently();
	}

	private void capTestSilently() {
		try {
			re.capTest();
		} catch ( final AssertionError e ) {
			// expected
		}
	}
}

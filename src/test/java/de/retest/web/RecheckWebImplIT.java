package de.retest.web;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import de.retest.recheck.RecheckLifecycle;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.persistence.SeparatePathsProjectLayout;
import de.retest.web.selenium.RecheckDriver;
import de.retest.web.testutils.WebDriverFactory;
import de.retest.web.testutils.WebDriverFactory.Driver;

class RecheckWebImplIT {

	RecheckWebImpl re;

	RecheckDriver driver;

	@BeforeEach
	void setUp( @TempDir final Path project ) {
		final Path goldenMasterDirectory = project.resolve( "state" );
		final Path reportDirectory = project.resolve( "report" );
		final RecheckOptions options = RecheckWebOptions.builder()
				.projectLayout( new SeparatePathsProjectLayout( goldenMasterDirectory, reportDirectory ) ) //
				.build();

		re = new RecheckWebImpl( options );
		driver = new RecheckDriver( WebDriverFactory.driver( Driver.CHROME ), options );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

	@Test
	void check_should_throw_if_explicit_checking() throws Exception {
		re.startTest();
		driver.startTest();

		assertThatThrownBy( () -> {
			re.check( driver, "explicit-implicit-mixed" );
		} ) //
				.isInstanceOf( UnsupportedOperationException.class ) //
				.hasMessageStartingWith(
						"The 'RecheckDriver' does implicit checking after each action, therefore no explicit check with 'Recheck#check' is needed" );

		capTestSilently( re );
		capTestSilently( driver );
	}

	@Test
	void check_should_not_throw_if_implicit_checking() throws Exception {
		driver.startTest();

		assertThatCode( () -> driver.get( "https://retest.de" ) ).doesNotThrowAnyException();

		capTestSilently( driver );
	}

	private void capTestSilently( final RecheckLifecycle re ) {
		try {
			re.capTest();
		} catch ( final AssertionError e ) {
			// expected
		}
	}
}

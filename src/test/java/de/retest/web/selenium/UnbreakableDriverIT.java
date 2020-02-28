package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.RecheckOptions;
import de.retest.recheck.persistence.SeparatePathsProjectLayout;
import de.retest.web.RecheckWebImpl;
import de.retest.web.RecheckWebOptions;
import de.retest.web.testutils.WebDriverFactory;

class UnbreakableDriverIT {

	RecheckWebImpl re;
	WebDriver driver;

	@BeforeEach
	void setUp( @TempDir final Path project ) {
		final Path goldenMasterDirectory = project.resolve( "state" );
		final Path reportDirectory = project.resolve( "report" );
		final RecheckOptions options = RecheckWebOptions.builder()
				.projectLayout( new SeparatePathsProjectLayout( goldenMasterDirectory, reportDirectory ) ) //
				.build();

		re = new RecheckWebImpl( options );
		driver = new UnbreakableDriver( WebDriverFactory.driver( WebDriverFactory.Driver.CHROME ) );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

	@Test
	void check_with_web_element_should_properly_identify_the_unbreakable_driver() {
		re.startTest();

		driver.get( getClass().getResource( "UnbreakableDriverIT.html" ).toExternalForm() );

		assertThatCode( () -> re.check( driver.findElement( By.id( "html" ) ), "element" ) ).doesNotThrowAnyException();

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

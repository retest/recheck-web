package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.spy;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.interactions.Actions;

import de.retest.recheck.RecheckOptions;
import de.retest.recheck.junit.jupiter.RecheckExtension;
import de.retest.recheck.persistence.SeparatePathsProjectLayout;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.WebDriverFactory;
import de.retest.web.testutils.WebDriverFactory.Driver;

@ExtendWith( RecheckExtension.class )
class AutocheckingWebElementIT {

	AutocheckingRecheckDriver driver;
	AutocheckingWebElement cut;

	@BeforeEach
	void setUp( @TempDir Path temp ) {
		driver = new AutocheckingRecheckDriver( WebDriverFactory.driver( Driver.CHROME ), RecheckOptions.builder() //
				// No Golden Master should be created with this test, thus it will never fail in Recheck#capTest
				.projectLayout( new SeparatePathsProjectLayout( temp.resolve( "states" ), temp.resolve( "reports" ) ) ) //
				.build() );

		driver.skipCheck().get( PageFactory.page( PageFactory.Page.CENTER ) );

		cut = spy( driver.findElement( By.id( "center" ) ) );
	}

	@Test
	void actions_should_be_able_to_move_to_element() {
		final Actions actions = new Actions( driver ) //
				.moveToElement( cut );

		// Apparently, this does not call Location#getCoordinates
		assertThatCode( actions::perform ).doesNotThrowAnyException();
	}
}

package de.retest.web.it;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.junit.jupiter.RecheckExtension;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.WebDriverFactory;

@ExtendWith( RecheckExtension.class )
class InteractionPseudoElementIT {

	Recheck re;
	WebDriver driver;

	@BeforeEach
	void setUp( @TempDir final Path temp ) {
		re = new RecheckImpl( RecheckOptions.builder() //
				.disableReportUpload() //
				.setIgnore( "interaction-pseudo-elements.filter" ) //
				.build() );

		driver = WebDriverFactory.driver( WebDriverFactory.Driver.CHROME );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

	@Test
	void ghost_pseudo_elements_should_not_appear_because_of_css_changes() {
		driver.get( PageFactory.toPageUrlString( "pseudo/index.html" ) );

		final WebElement withoutPseudo = driver.findElement( By.id( "link-without-pseudo" ) );
		re.check( driver, "click" ); // Check before click
		withoutPseudo.click();
		re.check( driver, "click" ); // This should not capture any differences
	}
}

package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.persistence.SeparatePathsProjectLayout;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.WebDriverFactory;

class InteractionPseudoElementIT {

	Recheck re;
	WebDriver driver;

	@BeforeEach
	void setUp( @TempDir final Path temp ) {
		re = new RecheckImpl( RecheckOptions.builder() //
				.disableReportUpload() //
				.projectLayout( new SeparatePathsProjectLayout( temp.resolve( "state" ), temp.resolve( "reports" ) ) ) //
				.setIgnore( "interaction-pseudo-elements.filter" ) //
				.build() );

		driver = WebDriverFactory.driver( WebDriverFactory.Driver.CHROME );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

	@Test
	void ghost_pseudo_elements_should_not_appear_because_of_css_changes() {
		re.startTest();

		driver.get( PageFactory.toPageUrlString( "pseudo/index.html" ) );

		final WebElement withoutPseudo = driver.findElement( By.id( "link-without-pseudo" ) );
		re.check( driver, "click" );
		withoutPseudo.click();
		re.check( driver, "click" ); // This should not capture any differences

		assertThatThrownBy( re::capTest ).hasMessageEndingWith(
				"2 check(s) in 'de.retest.web.it.InteractionPseudoElementIT' found the following difference(s):\n"
						+ "Test 'ghost_pseudo_elements_should_not_appear_because_of_css_changes' has 1 difference(s) in 2 state(s):\n"
						+ "click resulted in:\n"
						+ "\tNo Golden Master found. First time test was run? Created new Golden Master, so don't forget to commit..." );
	}
}

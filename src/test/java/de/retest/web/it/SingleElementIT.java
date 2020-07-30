package de.retest.web.it;

import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.RecheckOptions;
import de.retest.web.testutils.PageFactory;
import de.retest.web.testutils.WebDriverFactory;
import de.retest.web.testutils.WebDriverFactory.Driver;

class SingleElementIT {

	WebDriver driver;
	Recheck re;

	@BeforeEach
	public void setup() {
		final RecheckOptions reopts = RecheckOptions.builder().ignoreNothing().build();
		re = new RecheckImpl( reopts );
		driver = WebDriverFactory.driver( Driver.CHROME );
	}

	@Test
	public void testParentScope() throws Exception {
		// create GM
		re.startTest( "testParentScope" );
		driver.get( PageFactory.toPageUrlString( "single-element-diff.html" ) );
		re.check( driver.findElement( By.id( "redtext" ) ), "same-color" );
		Assertions.assertThatThrownBy( () -> re.capTest() ).hasMessageContaining( //
				"No Golden Master found. First time test was run?" );

		// Check GM has text with red
		re.startTest( "testParentScope" );
		driver.get( PageFactory.toPageUrlString( "single-element-diff.html" ) );
		((JavascriptExecutor) driver).executeScript( "document.body.style.color = 'green';" );
		re.check( driver.findElement( By.id( "redtext" ) ), "same-color" );
		Assertions.assertThatThrownBy( () -> re.capTest() ).hasMessageContaining( //
				"color: expected=\"rgb(255, 0, 0)\", actual=\"rgb(0, 128, 0)\"" );
	}

	@AfterEach
	public void tearDown() {
		driver.quit();
		re.cap();

		// Not standard! Ensure we delete the GM that was created
		FileUtils.deleteQuietly(
				Paths.get( "src/test/resources/retest/recheck", SingleElementIT.class.getName() ).toFile() );

	}
}

package de.retest.web.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.junit.RecheckExtension;
import de.retest.web.testutils.PageFactory;

@ExtendWith( RecheckExtension.class )
public class CenterTestIT {

	WebDriver driver;
	Recheck re;

	@BeforeEach
	public void before() {
		re = new RecheckImpl();
		final ChromeOptions opts = new ChromeOptions();
		opts.addArguments(
				// Enable headless mode for faster execution.
				"--headless",
				// Use Chrome in container-based Travis CI enviroment (see https://docs.travis-ci.com/user/chrome#Sandboxing).
				"--no-sandbox",
				// Fix window size for stable results.
				"--window-size=1200,800" );
		driver = new ChromeDriver( opts );
	}

	@Test
	public void testCenter() throws Exception {
		driver.get( PageFactory.toPageUrlString( "centered.html" ) );

		re.check( driver, "open" );
	}

	@AfterEach
	public void tearDown() {
		driver.quit();
	}
}

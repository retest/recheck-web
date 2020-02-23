package de.retest.web.it;

import static de.retest.web.testutils.WebDriverFactory.commonArguments;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.RecheckImpl;

class PseudoElementIT {

	WebDriver driver;
	RecheckImpl re;

	@BeforeEach
	void setup() {
		re = new RecheckImpl();
		driver = new ChromeDriver( new ChromeOptions().addArguments( commonArguments() ) );
	}

	@Test
	public void should_trac_pseudo_elements() throws Exception {
		re.startTest( "pseudo_elements" );
		final String filePath = "pseudo-element-changed.html";
		final String url = getClass().getResource( filePath ).toString();

		driver.get( url );

		re.check( driver, "open" );

		assertThatThrownBy( re::capTest ) //
				.hasMessageNotContaining( "link at 'html[1]/head[1]/link[1]'" ) //
				.hasMessageContaining( "h1 [This is a heading] at 'html[1]/body[1]/h1[1]'" ) //
				.hasMessageContaining( "::before at 'html[1]/body[1]/h1[1]/<pseudo::before>[1]'" ) //
				.hasMessageContaining(
						"content: expected=\"This is an invisible text before!\", actual=\"This is a changed invisible text using a before pseudo element!\"" ) //
				.hasMessageContaining( "::after at 'html[1]/body[1]/h1[1]/<pseudo::after>[1]'" ) //
				.hasMessageContaining(
						"content: expected=\"This is an invisible text after!\", actual=\"This is a changed invisible text using an after pseudo element!\"" ) //
				.hasMessageContaining( "::first-line at 'html[1]/body[1]/p[1]/<pseudo::first-line>[1]'" ) //
				.hasMessageContaining( "color: expected=\"rgb(170, 187, 204)\", actual=\"rgb(221, 238, 255)\"" ) //
				.hasMessageContaining( "::first-letter at 'html[1]/body[1]/p[1]/<pseudo::first-letter>[1]'" ) //
				.hasMessageContaining( "color: expected=\"rgb(187, 204, 170)\", actual=\"rgb(238, 255, 221)\"" ) //
		;
	}

	@Disabled
	@Test
	void should_generate_master() throws Exception {
		re.startTest( "pseudo_elements" );
		final String filePath = "pseudo-element.html";
		final String url = getClass().getResource( filePath ).toString();
		driver.get( url );
		re.check( driver, "open" );
		re.capTest();
	}

	@AfterEach
	public void tearDown() throws InterruptedException {
		driver.quit();
	}
}

package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.RecheckImpl;

class PseudoElementIT {

	WebDriver driver;
	RecheckImpl re;

	@BeforeEach
	void setup() {
		re = new RecheckImpl();
	}

	@ParameterizedTest( name = "pseudo-elements-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	public void should_trac_pseudo_elements( final WebDriver driver, final String name ) throws Exception {
		re.startTest( "pseudo-elements-" + name );
		this.driver = driver;
		final String filePath = "pseudo-element-changed.html";
		final String url = getClass().getResource( filePath ).toString();

		driver.get( url );

		re.check( driver, "open" );

		assertThatThrownBy( re::capTest ) //
				.hasMessageNotContaining( "link at 'html[1]/head[1]/link[1]'" ) //
				.hasMessageContaining( "h1 (this_is_a_heading) at 'html[1]/body[1]/h1[1]'" ) //
				.hasMessageContaining( "::before (before) at 'html[1]/body[1]/h1[1]/#pseudo::before[1]'" ) //
				.hasMessageContaining( "expected=\"This is an invisible text before!\"," ) //
				.hasMessageContaining( "actual=\"This is a changed invisible text using a before pseudo element!\"" ) //
				.hasMessageContaining( "::after (after) at 'html[1]/body[1]/h1[1]/#pseudo::after[1]'" ) //
				.hasMessageContaining( "expected=\"This is an invisible text after!\"," ) //
				.hasMessageContaining( "actual=\"This is a changed invisible text using an after pseudo element!\"" ) //
				.hasMessageContaining( "::first-line (firstline) at 'html[1]/body[1]/p[1]/#pseudo::first-line[1]'" ) //
				.hasMessageContaining( "expected=\"rgb(170, 187, 204)\"," ) //
				.hasMessageContaining( "actual=\"rgb(221, 238, 255)\"" ) //
				.hasMessageContaining(
						"::first-letter (firstletter) at 'html[1]/body[1]/p[1]/#pseudo::first-letter[1]'" ) //
				.hasMessageContaining( "expected=\"rgb(187, 204, 170)\"," ) //
				.hasMessageContaining( "actual=\"rgb(238, 255, 221)\"" ) //
		;
	}

	@Disabled
	@ParameterizedTest( name = "pseudo-elements-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void should_generate_master( final WebDriver driver, final String name ) throws Exception {
		re.startTest( "pseudo-elements-" + name );
		this.driver = driver;
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

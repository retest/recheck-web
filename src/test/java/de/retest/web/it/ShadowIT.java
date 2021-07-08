package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.persistence.SeparatePathsProjectLayout;

public class ShadowIT {

	Recheck re;

	@BeforeEach
	void setUp( @TempDir Path path ) {
		re = new RecheckImpl( RecheckOptions.builder() //
				// Move the project layout within a custom directory, so that the Golden Master is created every time
				.projectLayout( new SeparatePathsProjectLayout( path.resolve( "states" ), path.resolve( "reports" ) ) ) //
				// Ignore attribute style of button for now (pressed), as this test does not modify the styles itself
				.setIgnore( "shadow.filter" ) //
				.build() );
	}

	@AfterEach
	void tearDown() {
		re.cap();
	}

	@ParameterizedTest( name = "shadow-page-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void shadow_dom_can_only_detect_light_dom_changes( WebDriver driver, String name ) {
		re.startTest( "shadow-page-" + name );

		driver.get( ShadowIT.class.getResource( "/pages/shadow/index.html" ).toExternalForm() );

		re.check( driver, "shadow" );

		driver.findElement( By.id( "change" ) ).click();

		re.check( driver, "shadow" );

		try {
			re.capTest();
		} catch ( AssertionError e ) {
			assertThat( e ).hasMessageContaining( // 
					"\tbutton (change) at 'html[1]/body[1]/rt-layout[1]/button[1]':\n"
							+ "\t\tdisabled: expected=\"(default or absent)\", expected=\"true\"\n"
							+ "\tre-card (card1) at 'html[1]/body[1]/rt-layout[1]/rt-card-deck[1]/rt-card[1]':\n"
							+ "\t\tfooter: expected=\"First\", actual=\"Left\"\n"
							+ "\t\theader: expected=\"Card 1\", actual=\"Card Left\"\n"
							+ "\tbutton (hello_world) at 'html[1]/body[1]/rt-layout[1]/rt-card-deck[1]/rt-card[1]/div[1]/button[1]':\n"
							+ "\t\tclass: expected=\"btn btn-success\", actual=\"btn btn-primary\"\n"
							+ "\tre-card (card2) at 'html[1]/body[1]/rt-layout[1]/rt-card-deck[1]/rt-card[2]':\n"
							+ "\t\theader: expected=\"Card 2\", actual=\"Card Middle\"\n"
							+ "\t\tfooter: expected=\"(default or absent)\", actual=\"Middle\"\n"
							+ "\tbutton (hello_world-1) at 'html[1]/body[1]/rt-layout[1]/rt-card-deck[1]/rt-card[2]/div[1]/button[1]':\n"
							+ "\t\tclass: expected=\"btn btn-warning\", actual=\"btn btn-secondary\"\n"
							+ "\tre-card (card3) at 'html[1]/body[1]/rt-layout[1]/rt-card-deck[1]/rt-card[3]':\n"
							+ "\t\tfooter: expected=\"Last\", actual=\"Right\"\n"
							+ "\t\theader: expected=\"Card 3\", actual=\"Card Right\"\n"
							+ "\tbutton (hello_world-2) at 'html[1]/body[1]/rt-layout[1]/rt-card-deck[1]/rt-card[3]/div[1]/button[1]':\n"
							+ "\t\tclass: expected=\"btn btn-danger\", actual=\"btn btn-success\"\n"
							+ "\tre-copyright (copyright) at 'html[1]/body[1]/rt-layout[1]/rt-copyright[1]':\n"
							+ "\t\tyear: expected=\"2020\", actual=\"2021\"" );
		} finally {
			driver.quit();
		}
	}
}

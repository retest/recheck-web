package de.retest.web.it;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.persistence.SeparatePathsProjectLayout;
import de.retest.web.testutils.PageFactory;

class PseudoElementTest {

	Recheck re;

	@BeforeEach
	void setUp( @TempDir Path temp ) {
		re = new RecheckImpl( RecheckOptions.builder()
				.projectLayout( new SeparatePathsProjectLayout( temp.resolve( "state" ), temp.resolve( "reports" ) ) ) //
				.setIgnore( "recheck-pseudo-elements.filter" ) //
				.build() );
	}

	@ParameterizedTest( name = "links_should_be_captured_without_pseudo_elements_when_pressed-{1}" )
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void links_should_be_captured_without_pseudo_elements_when_pressed( WebDriver driver, String name ) {
		driver.get( PageFactory.toPageUrlString( "pseudo/index.html" ) );
	}
}

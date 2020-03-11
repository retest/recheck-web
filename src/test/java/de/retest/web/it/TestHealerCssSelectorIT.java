package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.net.MalformedURLException;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import de.retest.web.AttributesUtil;
import de.retest.web.selenium.AutocheckingRecheckDriver;
import de.retest.web.testutils.WebDriverFactory;
import de.retest.web.testutils.WebDriverFactory.Driver;

class TestHealerCssSelectorIT {

	static WebDriver driver;

	@BeforeAll
	static void setup() throws MalformedURLException {
		driver = new AutocheckingRecheckDriver( WebDriverFactory.driver( Driver.CHROME ) );
		final String page = "TestHealerCssSelectorIT-changed.html";
		driver.navigate().to( TestHealerCssSelectorIT.class.getResource( page ).toExternalForm() );
		driver.findElement( By.id( "visited" ) ).click();
	}

	@MethodSource( "attributeSelectors" )
	@ParameterizedTest( name = "CSS Selector: {0} expected html element ID: {1}" )
	void should_find_elements_selected_by_CSS_attribute_selectors( final String selector, final String id ) {
		assertThat( driver.findElement( By.cssSelector( selector ) ).getAttribute( AttributesUtil.ID ) )
				.isEqualTo( id );
	}

	private static Stream<Arguments> attributeSelectors() {
		return Stream.of( //
				Arguments.of( ".checkable[checked]", "checked" ), //
				Arguments.of( ".checkable[id=checked]", "checked" ), //
				Arguments.of( "[value~=selector]", "attribute-word-selector" ), //
				Arguments.of( "[value|=attribute]", "attribute-word-selector" ), //
				Arguments.of( ".checkable[id^=\"checked\"]", "checked" ), //
				Arguments.of( ".checkable[id$=\"nate\"]", "indeterminate" ), //
				Arguments.of( ".checkable[id*=\"nche\"]", "unchecked" ) //
		);
	}

	@MethodSource( "supportedPseudoClassSelectors" )
	@ParameterizedTest( name = "Pseudo class {1}" )
	void should_find_elements_selected_by_pseudo_class_using_css_selector( final String cssClass,
			final String pseudoClass ) {
		final By selector = By.cssSelector( "." + cssClass + ":" + pseudoClass );
		assertThat( driver.findElement( selector ).getAttribute( AttributesUtil.ID ) ).isEqualTo( pseudoClass );
	}

	private static Stream<Arguments> supportedPseudoClassSelectors() {
		return Stream.of( //
				Arguments.of( "checkable", "checked" ), //
				Arguments.of( "disable", "disabled" ), //
				Arguments.of( "write-access", "read-only" ) //
		);
	}

	@MethodSource( "unsupportedPseudoClassSelectors" )
	@ParameterizedTest( name = "Pseudo class {1}" )
	void should_not_find_elements_selected_by_unsupported_pseudo_class_using_css_selector( final String cssClass,
			final String pseudoClass ) {
		final By selector = By.cssSelector( "." + cssClass + ":" + pseudoClass );
		assertThatExceptionOfType( NoSuchElementException.class )
				.isThrownBy( () -> driver.findElement( selector ).getAttribute( AttributesUtil.ID ) )
				.withMessageContaining( pseudoClass );
	}

	private static Stream<Arguments> unsupportedPseudoClassSelectors() throws Exception {
		return Stream.of( //
				Arguments.of( "base", "root" ), //
				Arguments.of( "checkable", "indeterminate" ), //
				Arguments.of( "default-other", "default" ), //
				Arguments.of( "disable", "enabled" ), //
				Arguments.of( "links", "link" ), //
				Arguments.of( "links", "target" ), //
				Arguments.of( "links", "visited" ), //
				Arguments.of( "optional-required", "optional" ), //
				Arguments.of( "optional-required", "required" ), //
				Arguments.of( "range", "in-range" ), //
				Arguments.of( "range", "out-of-range" ), //
				Arguments.of( "valid-invalid", "invalid" ), //
				Arguments.of( "valid-invalid", "valid" ), //
				Arguments.of( "write-access", "read-write" ) //
		);
	}

	@AfterAll
	static void tearDown() {
		driver.quit();
	}

}

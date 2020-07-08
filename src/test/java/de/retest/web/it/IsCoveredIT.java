package de.retest.web.it;

import static de.retest.web.testutils.PageFactory.page;
import static de.retest.web.testutils.PageFactory.Page.COVERED_PAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.web.RecheckSeleniumAdapter;
import de.retest.web.testutils.WebDriverFactory;
import de.retest.web.testutils.WebDriverFactory.Driver;

class IsCoveredIT {
	RemoteWebDriver driver;
	RecheckAdapter recheckAdapter;

	@BeforeEach
	void before() {
		driver = WebDriverFactory.driver( Driver.CHROME );
		recheckAdapter = new RecheckSeleniumAdapter();
	}

	@Test
	void fully_covered_element_should_not_be_clickable() {
		driver.get( page( COVERED_PAGE ) );
		final WebElement fullyOverlappedElement = driver.findElement( By.id( "fully-overlapped-element" ) );
		final Set<RootElement> rootElement = recheckAdapter.convert( fullyOverlappedElement );
		assertThat( rootElement ).first()
				.satisfies( element -> assertThat( element.getAttributeValue( "covered" ) ).isEqualTo( "true" ) );
		assertThatCode( fullyOverlappedElement::click ).isInstanceOf( ElementClickInterceptedException.class );
	}

	@Test
	void partially_covered_element_should_be_clickable() {
		driver.get( page( COVERED_PAGE ) );
		final WebElement partiallyOverlappedElementOnRight =
				driver.findElement( By.id( "partially-overlapped-element-right" ) );
		final WebElement partiallyOverlappedElementOnLeft =
				driver.findElement( By.id( "partially-overlapped-element-left" ) );
		final Set<RootElement> rootElementOnRight = recheckAdapter.convert( partiallyOverlappedElementOnRight );
		assertThat( rootElementOnRight ).first()
				.satisfies( element -> assertThat( element.getAttributeValue( "covered" ) ).isNull() );
		final Set<RootElement> rootElementOnLeft = recheckAdapter.convert( partiallyOverlappedElementOnLeft );
		assertThat( rootElementOnLeft ).first()
				.satisfies( element -> assertThat( element.getAttributeValue( "covered" ) ).isNull() );
		partiallyOverlappedElementOnRight.click();
		partiallyOverlappedElementOnLeft.click();
	}

	@Test
	void covered_by_child_element_should_be_clickable() {
		driver.get( page( COVERED_PAGE ) );
		final WebElement coveredByChildElement = driver.findElement( By.id( "covered-by-child-element" ) );
		final Set<RootElement> rootElement = recheckAdapter.convert( coveredByChildElement );
		assertThat( rootElement ).first()
				.satisfies( element -> assertThat( element.getAttributeValue( "covered" ) ).isNull() );
		coveredByChildElement.click();
	}

	@Test
	void zero_width_element_should_not_be_clickable() {
		driver.get( page( COVERED_PAGE ) );
		final WebElement zeroWidthElement = driver.findElement( By.id( "zero-width-element" ) );
		final Set<RootElement> rootElement = recheckAdapter.convert( zeroWidthElement );
		assertThat( rootElement ).first()
				.satisfies( element -> assertThat( element.getAttributeValue( "covered" ) ).isEqualTo( "true" ) );
		assertThatCode( zeroWidthElement::click ).isInstanceOf( ElementNotInteractableException.class );
	}

	@Test
	void non_overlapped_element_should_be_clickable() {
		driver.get( page( COVERED_PAGE ) );
		final WebElement nonOverlappingElement = driver.findElement( By.id( "non-overlapping-element" ) );
		final Set<RootElement> rootElement = recheckAdapter.convert( nonOverlappingElement );
		assertThat( rootElement ).first()
				.satisfies( element -> assertThat( element.getAttributeValue( "covered" ) ).isNull() );
		nonOverlappingElement.click();
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}
}

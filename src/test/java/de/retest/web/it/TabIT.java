package de.retest.web.it;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import de.retest.recheck.junit.jupiter.RecheckExtension;
import de.retest.web.selenium.RecheckDriver;
import de.retest.web.testutils.WebDriverFactory;

@ExtendWith( RecheckExtension.class )
class TabIT {

	WebDriver driver;

	@BeforeEach
	void setUp() {
		driver = new RecheckDriver( WebDriverFactory.driver( WebDriverFactory.Driver.CHROME ) );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

	@Test
	void tab() {
		driver.get( TabIT.class.getResource( "page.html" ).toExternalForm() );

		driver.findElement( By.id( "tab" ) ).click();

		selectWindow( driver, 1 );
		driver.findElement( By.id( "hello" ) ).click();
		closeWindow( driver, 1 );
	}

	@Test
	void window() {
		driver.get( TabIT.class.getResource( "page.html" ).toExternalForm() );

		driver.findElement( By.id( "window" ) ).click();

		selectWindow( driver, 1 );
		driver.findElement( By.id( "hello" ) ).click();
		closeWindow( driver, 1 );
	}

	void selectWindow( final WebDriver driver, final int index ) {
		driver.switchTo().window( getWindowHandle( driver, index ) );
	}

	String getWindowHandle( final WebDriver driver, final int index ) {
		final List<String> tabs = new ArrayList<>( driver.getWindowHandles() );
		return tabs.get( index );
	}

	void closeWindow( final WebDriver driver, final int index ) {
		driver.close();
		if ( index > 0 ) {
			selectWindow( driver, index - 1 );
		}
	}
}

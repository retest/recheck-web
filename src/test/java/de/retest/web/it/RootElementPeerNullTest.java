package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import de.retest.recheck.RecheckImpl;
import de.retest.recheck.ui.descriptors.SutState;

class RootElementPeerNullTest {

	RecheckImpl re;
	WebDriver driver;

	@BeforeEach
	void setUp() {
		re = spy( new RecheckImpl() );
		// Pretend that there is a valid and empty sut state, so it does not get created
		doReturn( mock( SutState.class, RETURNS_MOCKS ) ).when( re ).loadExpected( any() );
		// Do nothing on cap, i.e do not save the report
		doNothing().when( re ).cap();

		final ChromeOptions opts = new ChromeOptions();
		opts.setHeadless( true );
		opts.addArguments( "--window-size=480,800" );
		driver = new ChromeDriver( opts );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

	@Test
	void root_element_invisible_should_not_cause_npe() throws Exception {
		re.startTest();

		driver.get( getClass().getResource( "RootElementPeerNullTest.html" ).toExternalForm() );

		final WebElement invisibleElement = driver.findElement( By.className( "md-tabs" ) );

		assertThatCode( () -> re.check( invisibleElement, "tabs" ) ).doesNotThrowAnyException();

		assertThatThrownBy( () -> re.capTest() ) //
				.isInstanceOf( AssertionError.class ) //
				.hasMessageContaining( "nav at 'html[1]/body[1]/div[1]/nav[1]':\n\t\twas inserted" );
	}
}

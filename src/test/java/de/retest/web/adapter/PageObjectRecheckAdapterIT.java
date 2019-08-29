package de.retest.web.adapter;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import de.retest.recheck.RecheckImpl;
import de.retest.web.util.PageObjects;

class PageObjectRecheckAdapterIT {

	RecheckImpl re;
	WebDriver driver;

	@BeforeEach
	void setUp() {
		re = new RecheckImpl();
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

	@ParameterizedTest
	@MethodSource( "de.retest.web.testutils.WebDriverFactory#drivers" )
	void page_object_should_be_converted( final WebDriver driver ) throws Exception {
		this.driver = driver;
		re.startTest( "page-object-" + driver.getClass().getSimpleName() );

		driver.get( getClass().getResource( "PageObjectRecheckAdapterIT.html" ).toExternalForm() );
		final LoginPage loginPage = PageFactory.initElements( driver, LoginPage.class );

		re.check( PageObjects.convertToList( loginPage ), "init" );
		re.check( loginPage.user( "john@doe.com" ), "email" );
		re.check( loginPage.password( "john-doe" ), "password" );
		re.check( loginPage.login(), "login" );

		re.capTest();
	}

	@PageObject
	public static class LoginPage {

		WebElement username;
		WebElement password;
		@FindBy( id = "btn-login" )
		WebElement login;
		@FindBy( tagName = "a" )
		List<WebElement> links;

		public LoginPage user( final String username ) {
			this.username.sendKeys( username );
			return this;
		}

		public LoginPage password( final String password ) {
			this.password.sendKeys( password );
			return this;
		}

		public LoginPage login() {
			login.click();
			return this;
		}
	}
}
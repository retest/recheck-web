package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SimplePageIntTest {

	WebDriver driver;

	@BeforeEach
	void setUp() {
		final ChromeOptions opts = new ChromeOptions();
		opts.addArguments( "--headless" );
		opts.addArguments( "--no-sandbox" );

		driver = new ChromeDriver( opts );
	}

	@Test
	void testName() throws Exception {
		final Path simplePage = Paths.get( "src/test/resources/pages/simple-page.html" );
		driver.get( simplePage.toUri().toURL().toString() );
		final WebElement multiline = driver.findElement( By.id( "multiline" ) );
		assertThat( multiline.getText() ).isEqualTo( "A div containing\n" //
				+ "More than one line of text\n" //
				+ "and block level elements" );
	}

	@AfterEach
	void tearDown() {
		driver.quit();
	}

}

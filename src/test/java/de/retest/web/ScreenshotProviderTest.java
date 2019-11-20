package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CommandExecutor;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.web.screenshot.NoScreenshot;
import de.retest.web.screenshot.ScreenshotProviders;

class ScreenshotProviderTest {

	@Test
	void failingDriver_should_return_null() {
		final RemoteWebDriver exceptionCausingDriver = mock( ChromeDriver.class );
		when( exceptionCausingDriver.executeScript( Mockito.anyString() ) ).thenReturn( 1.0 );
		when( exceptionCausingDriver.getCommandExecutor() ).thenReturn( mock( CommandExecutor.class ) );

		assertThat( ScreenshotProviders.shoot( exceptionCausingDriver, null, new NoScreenshot() ) ).isNull();
	}

}

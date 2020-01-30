package de.retest.web.meta.driver.capabilities;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AllCapabilities {

	public static Stream<Capabilities> capabilities() {

		// TODO static methods are deprecated and it is recommended to use ...Options, unclear how to replace android, 
		//  iphone, ipad, htmlUnit

		final List<Capabilities> capabilities = new ArrayList<>();

		// DesiredCapabilities.firefox(), // removed in Selenium 4
		final DesiredCapabilities firefox = new DesiredCapabilities( BrowserType.FIREFOX, "", Platform.ANY );
		firefox.setCapability( "acceptInsecureCerts", true );
		capabilities.add( firefox );

		// DesiredCapabilities.android(), // removed in Selenium 4
		final DesiredCapabilities android = new DesiredCapabilities( BrowserType.ANDROID, "", Platform.ANDROID );
		capabilities.add( android );

		// DesiredCapabilities.chrome(), // removed in Selenium 4
		final DesiredCapabilities chrome = new DesiredCapabilities( BrowserType.CHROME, "", Platform.ANY );
		capabilities.add( chrome );

		// DesiredCapabilities.operaBlink() // removed in Selenium 4
		final DesiredCapabilities opera = new DesiredCapabilities( BrowserType.OPERA_BLINK, "", Platform.ANY );
		capabilities.add( opera );

		// DesiredCapabilities.iphone(), // removed in Selenium 4
		final DesiredCapabilities iPhone = new DesiredCapabilities( BrowserType.IPHONE, "", Platform.MAC );
		capabilities.add( iPhone );

		//DesiredCapabilities.ipad(), // removed in Selenium 4
		final DesiredCapabilities iPad = new DesiredCapabilities( BrowserType.IPAD, "", Platform.MAC );
		capabilities.add( iPad );

		// DesiredCapabilities.edge(), // removed in Selenium 4
		final DesiredCapabilities edge = new DesiredCapabilities( BrowserType.EDGE, "", Platform.WINDOWS );
		capabilities.add( edge );

		// DesiredCapabilities.internetExplorer(), // removed in Selenium 4
		final DesiredCapabilities internetExplorer = new DesiredCapabilities( BrowserType.IE, "", Platform.WINDOWS );
		capabilities.add( internetExplorer );

		// DesiredCapabilities.safari(), // removed in Selenium 4
		final DesiredCapabilities safari = new DesiredCapabilities( BrowserType.SAFARI, "", Platform.MAC );
		capabilities.add( safari );

		// DesiredCapabilities.htmlUnit(), // set to deprecated
		final DesiredCapabilities htmlUnit = new DesiredCapabilities( BrowserType.HTMLUNIT, "", Platform.ANY );
		capabilities.add( htmlUnit );

		return capabilities.stream();
	}
}

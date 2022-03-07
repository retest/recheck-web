package de.retest.web.meta.driver.capabilities;

import java.util.logging.Logger;
import java.util.stream.Stream;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AllCapabilities {

	private static final Logger LOG = Logger.getLogger(Capabilities.class.getName());

	public static Stream<Capabilities> capabilities() {
		return Stream.of( //
				android(), //
				iphone(), //
				ipad(), //
				htmlUnit(), //

				chrome(), //
				firefox(), //

				edge(), //
				internetExplorer(), //

				safari(), //

				operaBlink() //
		);
	}

	private static Capabilities iphone() {
		return new DesiredCapabilities(BrowserType.IPHONE, "", Platform.MAC);
	}

	private static Capabilities ipad() {
		return new DesiredCapabilities(BrowserType.IPAD, "", Platform.MAC);
	}

	private static DesiredCapabilities android() {
		return new DesiredCapabilities(BrowserType.ANDROID, "", Platform.ANDROID);
	}

	public static DesiredCapabilities chrome() {
		LOG.info("Using `new ChromeOptions()` is preferred to `DesiredCapabilities.chrome()`");
		return new DesiredCapabilities(BrowserType.CHROME, "", Platform.ANY);
	}

	public static DesiredCapabilities firefox() {
		LOG.info("Using `new FirefoxOptions()` is preferred to `DesiredCapabilities.firefox()`");
		DesiredCapabilities capabilities = new DesiredCapabilities(
				BrowserType.FIREFOX,
				"",
				Platform.ANY);
		capabilities.setCapability("acceptInsecureCerts", true);

		return capabilities;
	}

	public static DesiredCapabilities htmlUnit() {
		return new DesiredCapabilities(BrowserType.HTMLUNIT, "", Platform.ANY);
	}

	public static DesiredCapabilities edge() {
		LOG.info("Using `new EdgeOptions()` is preferred to `DesiredCapabilities.edge()`");
		return new DesiredCapabilities(BrowserType.EDGE, "", Platform.WINDOWS);
	}
	public static DesiredCapabilities internetExplorer() {
		DesiredCapabilities capabilities = new DesiredCapabilities(
				BrowserType.IE, "", Platform.WINDOWS);
		capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
		return capabilities;
	}

	public static DesiredCapabilities operaBlink() {
		LOG.info("Using `new OperaOptions()` is preferred to `DesiredCapabilities.operaBlink()`");
		return new DesiredCapabilities(BrowserType.OPERA, "", Platform.ANY);
	}

	public static DesiredCapabilities safari() {
		LOG.info("Using `new SafariOptions()` is preferred to `DesiredCapabilities.safari()`");
		return new DesiredCapabilities(BrowserType.SAFARI, "", Platform.MAC);
	}

}

package de.retest.web.meta.driver.capabilities;

import java.util.stream.Stream;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;

public class AllCapabilities {

	public static Stream<Capabilities> capabilities() {
		return Stream.of( // 
				DesiredCapabilities.android(), //
				DesiredCapabilities.iphone(), //
				DesiredCapabilities.ipad(), //
				DesiredCapabilities.htmlUnit(), //

				DesiredCapabilities.chrome(), //
				DesiredCapabilities.firefox(), //

				DesiredCapabilities.edge(), //
				DesiredCapabilities.internetExplorer(), //

				DesiredCapabilities.safari(), //

				DesiredCapabilities.operaBlink() //
		);
	}
}

package de.retest.web.meta;

import de.retest.recheck.meta.global.OSMetadataProvider;

public final class SeleniumMetadata {

	// Generic

	public static final String CHECK_TYPE = "check.type";

	// Driver

	public static final String BROWSER_NAME = "browser.name";
	public static final String BROWSER_VERSION = "browser.version";

	public static final String OS_NAME = OSMetadataProvider.OS_NAME;
	public static final String OS_VERSION = OSMetadataProvider.OS_VERSION;

	public static final String DRIVER_TYPE = "driver.type";

	public static final String WINDOW_WIDTH = "window.width";
	public static final String WINDOW_HEIGHT = "window.height";

	public static final String URL = "url";
}

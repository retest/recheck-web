package de.retest.web.meta.driver.capabilities;

import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.meta.MetadataProvider;
import de.retest.recheck.meta.MultiMetadataProvider;
import de.retest.web.util.SeleniumWrapperUtil;
import de.retest.web.util.SeleniumWrapperUtil.WrapperOf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class CapabilityMetadataProvider implements MetadataProvider {

	private final MetadataProvider provider;

	CapabilityMetadataProvider( final Capabilities capabilities ) {
		provider = MultiMetadataProvider.of( //
				new BrowserMetadataProvider( capabilities ), //
				new PlatformMetadataProvider( capabilities ) // 
		);
	}

	public static MetadataProvider of( final WebDriver driver ) {
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, driver ) ) {
			return of( (WebDriver) SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, driver ) );
		}
		if ( driver instanceof RemoteWebDriver ) {
			return of( (RemoteWebDriver) driver );
		}
		log.debug( "Cannot retrieve capabilities from driver {}. Driver must be of '{}'. Returning empty metadata.",
				driver, RemoteWebDriver.class );
		return MetadataProvider.empty();
	}

	public static CapabilityMetadataProvider of( final RemoteWebDriver driver ) {
		return of( driver.getCapabilities() );
	}

	private static CapabilityMetadataProvider of( final Capabilities capabilities ) {
		return new CapabilityMetadataProvider( capabilities );
	}

	@Override
	public Map<String, String> retrieve() {
		return provider.retrieve();
	}
}

package de.retest.web.meta.element;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import de.retest.recheck.meta.MetadataProvider;
import de.retest.web.meta.driver.WebDriverMetadataProvider;
import de.retest.web.util.SeleniumWrapperUtil;
import de.retest.web.util.SeleniumWrapperUtil.WrapperOf;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class WebElementMetadataProvider {

	public static MetadataProvider of( final WebElement element ) {
		return extractDriverMetadataProvider( element );
	}

	private static MetadataProvider extractDriverMetadataProvider( final WebElement element ) {
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, element ) ) {
			final Object wrapped = SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, element );
			return WebDriverMetadataProvider.of( (WebDriver) wrapped );
		}
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.ELEMENT, element ) ) {
			final Object wrapped = SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, element );
			return extractDriverMetadataProvider( (WebElement) wrapped );
		}
		log.debug( "Cannot retrieve driver from element {}. Element must be of '{}'. Returning empty metadata.",
				element, RemoteWebElement.class );
		return MetadataProvider.empty();
	}
}

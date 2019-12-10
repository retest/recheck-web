package de.retest.web.meta;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.retest.recheck.meta.MetadataProvider;
import de.retest.recheck.meta.MultiMetadataProvider;
import de.retest.web.meta.driver.WebDriverMetadataProvider;
import de.retest.web.meta.element.WebElementMetadataProvider;
import de.retest.web.util.SeleniumWrapperUtil;
import de.retest.web.util.SeleniumWrapperUtil.WrapperOf;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( access = AccessLevel.PACKAGE )
public final class SeleniumMetadataProvider implements MetadataProvider {

	public static final String TYPE_DRIVER = "driver";
	public static final String TYPE_ELEMENT = "element";

	private final String type;

	public static MetadataProvider of( final Object object ) {
		if ( object instanceof WebElement ) {
			return of( (WebElement) object );
		}
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.ELEMENT, object ) ) {
			return of( SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, object ) );
		}
		if ( object instanceof WebDriver ) {
			return of( (WebDriver) object );
		}
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, object ) ) {
			return of( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, object ) );
		}
		throw new IllegalArgumentException(
				String.format( "Cannot retrieve metadata from objects of type '%s'.", object.getClass().getName() ) );
	}

	private static MetadataProvider of( final WebDriver object ) {
		return MultiMetadataProvider.of( // 
				WebDriverMetadataProvider.of( object ), //
				new SeleniumMetadataProvider( TYPE_DRIVER ) //
		);
	}

	private static MetadataProvider of( final WebElement object ) {
		return MultiMetadataProvider.of( //
				WebElementMetadataProvider.of( object ), //
				new SeleniumMetadataProvider( TYPE_ELEMENT ) //
		);
	}

	@Override
	public Map<String, String> retrieve() {
		final Map<String, String> map = new HashMap<>();

		map.put( SeleniumMetadata.CHECK_TYPE, type );

		return map;
	}
}

package de.retest.web.adapter;

import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;

import com.assertthat.selenium_shutterbug.core.Shutterbug;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.recheck.ui.image.ImageUtils;
import de.retest.recheck.util.RetestIdProviderUtil;
import de.retest.web.AttributesProvider;
import de.retest.web.DefaultWebValueFinder;
import de.retest.web.YamlAttributesProvider;
import de.retest.web.mapping.WebDataProvider;
import de.retest.web.mapping.WebElementConverter;
import de.retest.web.mapping.WebElementWebDataProvider;
import de.retest.web.mapping.path.XPathGenerator;

public class WebElementRecheckAdapter implements RecheckAdapter {

	private final RetestIdProvider id = RetestIdProviderUtil.getConfiguredRetestIdProvider();
	private final AttributesProvider attributes = YamlAttributesProvider.getInstance();
	private final DefaultValueFinder defaults = new DefaultWebValueFinder();

	private final WebElementConverter converter = new WebElementConverter( id, attributes, defaults );

	@Override
	public boolean canCheck( final Object toVerify ) {
		return toVerify instanceof WebElement || toVerify instanceof WrapsElement;
	}

	@Override
	public Set<RootElement> convert( final Object toVerify ) {
		return convertWebElement( extract( toVerify ) );
	}

	private WebElement extract( final Object toVerify ) {
		if ( toVerify instanceof WrapsElement ) { // Have this before, since it is more important
			return extract( ( (WrapsElement) toVerify ).getWrappedElement() ); // Do we need the recursion here?
		}
		if ( toVerify instanceof WebElement ) {
			return (WebElement) toVerify;
		}
		throw new UnsupportedOperationException( "Cannot extract WebElement of " + toVerify.getClass() + "." );
	}

	private Set<RootElement> convertWebElement( final WebElement element ) {
		id.reset();
		final Element wrapped = convertWebElement( createDummyParent(), new XPathGenerator(), element );
		// WHY?
		final RootElement root = new RootElement( //
				wrapped.getRetestId(), //
				wrapped.getIdentifyingAttributes(), //
				wrapped.getAttributes(), //
				ImageUtils.image2Screenshot( wrapped.getRetestId(), shootElement( element ) ), //
				"", //
				0, //
				"" //
		);
		root.addChildren( wrapped.getContainedElements() );
		return Collections.singleton( root );
	}

	private Element convertWebElement( final Element parent, final XPathGenerator path, final WebElement element ) {
		final XPathGenerator childXPath = path.next( element.getTagName() );
		final WebDataProvider provider = new WebElementWebDataProvider( element, childXPath.getPath(), attributes );
		final Element child = converter.convert( parent, provider );
		element.findElements( By.xpath( "*" ) ).stream() //
				.map( c -> convertWebElement( child, childXPath, c ) ) //
				.forEach( child::addChildren );
		return child;
	}

	private BufferedImage shootElement( final WebElement element ) {
		if ( element instanceof WrapsDriver ) {
			final WebDriver driver = ( (WrapsDriver) element ).getWrappedDriver();
			return Shutterbug.shootElement( driver, element ).getImage();
		}
		return null;
	}

	@Override
	public DefaultValueFinder getDefaultValueFinder() {
		return defaults;
	}

	// ...
	private Element createDummyParent() {
		try {
			final Constructor<Element> constructor = Element.class.getDeclaredConstructor();
			constructor.setAccessible( true );
			return constructor.newInstance();
		} catch ( final Exception e ) {
			return null;
		}
	}
}

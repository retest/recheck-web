package de.retest.web.selenium;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Interactive;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.web.RecheckWebImpl;
import de.retest.web.util.SeleniumWrapperUtil;
import de.retest.web.util.SeleniumWrapperUtil.WrapperOf;
import lombok.Getter;
import lombok.Setter;

/**
 * A wrapper for a given {@code RemoteWebDriver}, which can be used with e.g. {@code ChromeDriver}, {@code GeckoDriver},
 * or any other. It enables recheck-web's "Unbreakable Selenium" feature, where it must be used along with
 * {@link RecheckWebImpl}.
 */
@Getter
@Setter
public class UnbreakableDriver implements WebDriver, JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText,
		FindsByName, FindsByCssSelector, FindsByTagName, FindsByXPath, HasInputDevices, HasCapabilities, Interactive,
		TakesScreenshot, WrapsDriver {

	private final RemoteWebDriver wrappedDriver;
	private RootElement lastExpectedState;
	private RootElement lastActualState;
	private Consumer<QualifiedElementWarning> warningConsumer;

	/**
	 * @param wrappedDriver
	 *            We use RemoteWebDriver instead of WebDriver, because we need some of the other interfaces
	 *            RemoteWebDriver implements as well... and there is no single common super type.
	 */
	public UnbreakableDriver( final RemoteWebDriver wrappedDriver ) {
		this.wrappedDriver = wrappedDriver;
	}

	public WebElement findElement( final ByBestMatchToRetestId by ) {
		final Element searchedFor = by.findElement( lastExpectedState, lastActualState );
		final WebElement element =
				wrappedDriver.findElement( By.xpath( searchedFor.getIdentifyingAttributes().getPath() ) );
		return wrap( element );
	}

	protected WebElement wrap( final WebElement element ) {
		return WrappingWebElement.wrap( this, element );
	}

	private List<WebElement> wrap( final List<WebElement> elements ) {
		return elements.stream() //
				.map( this::wrap ) //
				.collect( Collectors.toList() );
	}

	public WebElement findElementByRetestId( final String retestId ) {
		return findElement( new ByBestMatchToRetestId( retestId ) );
	}

	@Override
	public WebElement findElement( final By by ) {
		if ( by instanceof ByBestMatchToRetestId ) {
			return findElement( (ByBestMatchToRetestId) by );
		}
		try {
			return wrap( wrappedDriver.findElement( by ) );
		} catch ( final NoSuchElementException e ) {
			final WebElement matchedOld = TestHealer.findElement( by, this );
			if ( matchedOld == null ) {
				throw e;
			}
			return wrap( matchedOld );
		}
	}

	@Override
	public List<WebElement> findElements( final By by ) {
		return wrap( wrappedDriver.findElements( by ) );
	}

	@Override
	public WebElement findElementById( final String using ) {
		return wrap( wrappedDriver.findElementById( using ) );
	}

	@Override
	public List<WebElement> findElementsById( final String using ) {
		return wrap( wrappedDriver.findElementsById( using ) );
	}

	@Override
	public WebElement findElementByClassName( final String using ) {
		return wrap( wrappedDriver.findElementByClassName( using ) );
	}

	@Override
	public List<WebElement> findElementsByClassName( final String using ) {
		return wrap( wrappedDriver.findElementsByClassName( using ) );
	}

	@Override
	public WebElement findElementByLinkText( final String using ) {
		return wrap( wrappedDriver.findElementByLinkText( using ) );
	}

	@Override
	public List<WebElement> findElementsByLinkText( final String using ) {
		return wrap( wrappedDriver.findElementsByLinkText( using ) );
	}

	@Override
	public WebElement findElementByPartialLinkText( final String using ) {
		return wrap( wrappedDriver.findElementByPartialLinkText( using ) );
	}

	@Override
	public List<WebElement> findElementsByPartialLinkText( final String using ) {
		return wrap( wrappedDriver.findElementsByPartialLinkText( using ) );
	}

	@Override
	public WebElement findElementByName( final String using ) {
		return wrap( wrappedDriver.findElementByName( using ) );
	}

	@Override
	public List<WebElement> findElementsByName( final String using ) {
		return wrap( wrappedDriver.findElementsByName( using ) );
	}

	@Override
	public WebElement findElementByCssSelector( final String using ) {
		return wrap( wrappedDriver.findElementByCssSelector( using ) );
	}

	@Override
	public List<WebElement> findElementsByCssSelector( final String using ) {
		return wrap( wrappedDriver.findElementsByCssSelector( using ) );
	}

	@Override
	public WebElement findElementByTagName( final String using ) {
		return wrap( wrappedDriver.findElementByTagName( using ) );
	}

	@Override
	public List<WebElement> findElementsByTagName( final String using ) {
		return wrap( wrappedDriver.findElementsByTagName( using ) );
	}

	@Override
	public WebElement findElementByXPath( final String using ) {
		return wrap( wrappedDriver.findElementByXPath( using ) );
	}

	@Override
	public List<WebElement> findElementsByXPath( final String using ) {
		return wrap( wrappedDriver.findElementsByXPath( using ) );
	}

	@Override
	public void close() {
		wrappedDriver.close();
	}

	@Override
	public void get( final String url ) {
		wrappedDriver.get( url );
	}

	@Override
	public String getCurrentUrl() {
		return wrappedDriver.getCurrentUrl();
	}

	@Override
	public String getPageSource() {
		return wrappedDriver.getPageSource();
	}

	@Override
	public String getTitle() {
		return wrappedDriver.getTitle();
	}

	@Override
	public String getWindowHandle() {
		return wrappedDriver.getWindowHandle();
	}

	@Override
	public Set<String> getWindowHandles() {
		return wrappedDriver.getWindowHandles();
	}

	@Override
	public Options manage() {
		return wrappedDriver.manage();
	}

	@Override
	public Navigation navigate() {
		return wrappedDriver.navigate();
	}

	@Override
	public void quit() {
		wrappedDriver.quit();
	}

	@Override
	public TargetLocator switchTo() {
		return wrappedDriver.switchTo();
	}

	@Override
	public Object executeScript( final String script, final Object... args ) {
		return wrappedDriver.executeScript( script, args );
	}

	@Override
	public Object executeAsyncScript( final String script, final Object... args ) {
		return wrappedDriver.executeAsyncScript( script, args );
	}

	@Override
	public <X> X getScreenshotAs( final OutputType<X> target ) {
		return wrappedDriver.getScreenshotAs( target );
	}

	@Override
	public Keyboard getKeyboard() {
		return wrappedDriver.getKeyboard();
	}

	@Override
	public Mouse getMouse() {
		return wrappedDriver.getMouse();
	}

	@Override
	public Capabilities getCapabilities() {
		return wrappedDriver.getCapabilities();
	}

	@Override
	public void perform( final Collection<Sequence> actions ) {
		wrappedDriver.perform( actions );
	}

	@Override
	public void resetInputState() {
		wrappedDriver.resetInputState();
	}

	@Override
	public WebDriver getWrappedDriver() {
		if ( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, wrappedDriver ) ) {
			return (WebDriver) SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, wrappedDriver );
		}
		return wrappedDriver;
	}

	/**
	 * Skip checks for actions performed on this web driver. Alias for {@link #getWrappedDriver()}.
	 *
	 * @return the {@link WebDriver} wrapped by this instance
	 */
	public WebDriver skipCheck() {
		return getWrappedDriver();
	}
}

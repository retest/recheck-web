package de.retest.web.selenium;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.Location;
import org.openqa.selenium.html5.LocationContext;
import org.openqa.selenium.html5.SessionStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.retest.ui.descriptors.Element;
import de.retest.ui.descriptors.RootElement;
import de.retest.web.RecheckWebImpl;

public class RecheckDriver implements WebDriver, JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText,
		FindsByName, FindsByCssSelector, FindsByTagName, FindsByXPath, HasInputDevices, HasCapabilities,
		TakesScreenshot, LocationContext, WebStorage, HasTouchScreen {

	private static final Logger logger = LoggerFactory.getLogger( RecheckDriver.class );

	private final WebDriver wrapped;
	private RootElement lastExpectedState;
	private RootElement lastActualState;

	public RecheckDriver( final WebDriver wrapped ) {
		this.wrapped = wrapped;
	}

	public void setLastExpectedState( final RootElement lastExpectedState ) {
		this.lastExpectedState = lastExpectedState;
	}

	public RootElement getLastExpectedState() {
		return lastExpectedState;
	}

	public void setLastActualState( final RootElement lastActualState ) {
		this.lastActualState = lastActualState;
	}

	public RootElement getLastActualState() {
		return lastActualState;
	}

	public WebElement findElement( final ByRetestId by ) {
		if ( lastExpectedState == null ) {
			throw new IllegalStateException( "You must use the " + RecheckWebImpl.class.getSimpleName()
					+ " and first check the state before being able to use the retest ID locator." );
		}
		final Element searchedFor = by.findElement( lastExpectedState, lastActualState );
		return wrapped.findElement( By.xpath( searchedFor.getIdentifyingAttributes().getPath() ) );
	}

	public WebElement findElementByRetestId( final String retestId ) {
		return findElement( new ByRetestId( retestId ) );
	}

	@Override
	public WebElement findElement( final By by ) {
		if ( by instanceof ByRetestId ) {
			return findElement( (ByRetestId) by );
		}
		try {
			return wrapped.findElement( by );
		} catch ( final NoSuchElementException e ) {
			try {
				return TestHealer.findElement( by, this );
			} catch ( final Exception recheckE ) {
				logger.error( "Error trying to locate element in old state, throwing original exception.", recheckE );
				throw e;
			}
		}
	}

	@Override
	public List<WebElement> findElements( final By by ) {
		return wrapped.findElements( by );
	}

	@Override
	public WebElement findElementById( final String using ) {
		return ((FindsById) wrapped).findElementById( using );
	}

	@Override
	public List<WebElement> findElementsById( final String using ) {
		return ((FindsById) wrapped).findElementsById( using );
	}

	@Override
	public WebElement findElementByClassName( final String using ) {
		return ((FindsByClassName) wrapped).findElementByClassName( using );
	}

	@Override
	public List<WebElement> findElementsByClassName( final String using ) {
		return ((FindsByClassName) wrapped).findElementsByClassName( using );
	}

	@Override
	public WebElement findElementByLinkText( final String using ) {
		return ((FindsByLinkText) wrapped).findElementByLinkText( using );
	}

	@Override
	public List<WebElement> findElementsByLinkText( final String using ) {
		return ((FindsByLinkText) wrapped).findElementsByLinkText( using );
	}

	@Override
	public WebElement findElementByPartialLinkText( final String using ) {
		return ((FindsByLinkText) wrapped).findElementByPartialLinkText( using );
	}

	@Override
	public List<WebElement> findElementsByPartialLinkText( final String using ) {
		return ((FindsByLinkText) wrapped).findElementsByPartialLinkText( using );
	}

	@Override
	public WebElement findElementByName( final String using ) {
		return ((FindsByName) wrapped).findElementByName( using );
	}

	@Override
	public List<WebElement> findElementsByName( final String using ) {
		return ((FindsByName) wrapped).findElementsByName( using );
	}

	@Override
	public WebElement findElementByCssSelector( final String using ) {
		return ((FindsByCssSelector) wrapped).findElementByCssSelector( using );
	}

	@Override
	public List<WebElement> findElementsByCssSelector( final String using ) {
		return ((FindsByCssSelector) wrapped).findElementsByCssSelector( using );
	}

	@Override
	public WebElement findElementByTagName( final String using ) {
		return ((FindsByTagName) wrapped).findElementByTagName( using );
	}

	@Override
	public List<WebElement> findElementsByTagName( final String using ) {
		return ((FindsByTagName) wrapped).findElementsByTagName( using );
	}

	@Override
	public WebElement findElementByXPath( final String using ) {
		return ((FindsByXPath) wrapped).findElementByXPath( using );
	}

	@Override
	public List<WebElement> findElementsByXPath( final String using ) {
		return ((FindsByXPath) wrapped).findElementsByXPath( using );
	}

	@Override
	public void close() {
		wrapped.close();
	}

	@Override
	public void get( final String url ) {
		wrapped.get( url );
	}

	@Override
	public String getCurrentUrl() {
		return wrapped.getCurrentUrl();
	}

	@Override
	public String getPageSource() {
		return wrapped.getPageSource();
	}

	@Override
	public String getTitle() {
		return wrapped.getTitle();
	}

	@Override
	public String getWindowHandle() {
		return wrapped.getWindowHandle();
	}

	@Override
	public Set<String> getWindowHandles() {
		return wrapped.getWindowHandles();
	}

	@Override
	public Options manage() {
		return wrapped.manage();
	}

	@Override
	public Navigation navigate() {
		return wrapped.navigate();
	}

	@Override
	public void quit() {
		wrapped.quit();
	}

	@Override
	public TargetLocator switchTo() {
		return wrapped.switchTo();
	}

	@Override
	public Object executeScript( final String script, final Object... args ) {
		return ((JavascriptExecutor) wrapped).executeScript( script, args );
	}

	@Override
	public Object executeAsyncScript( final String script, final Object... args ) {
		return ((JavascriptExecutor) wrapped).executeAsyncScript( script, args );
	}

	@Override
	public <X> X getScreenshotAs( final OutputType<X> target ) throws WebDriverException {
		return ((TakesScreenshot) wrapped).getScreenshotAs( target );
	}

	@Override
	public Keyboard getKeyboard() {
		return ((HasInputDevices) wrapped).getKeyboard();
	}

	@Override
	public Mouse getMouse() {
		return ((HasInputDevices) wrapped).getMouse();
	}

	@Override
	public Capabilities getCapabilities() {
		return ((HasCapabilities) wrapped).getCapabilities();
	}

	@Override
	public Location location() {
		return ((LocationContext) wrapped).location();
	}

	@Override
	public void setLocation( final Location location ) {
		((LocationContext) wrapped).setLocation( location );
	}

	@Override
	public LocalStorage getLocalStorage() {
		return ((WebStorage) wrapped).getLocalStorage();
	}

	@Override
	public SessionStorage getSessionStorage() {
		return ((WebStorage) wrapped).getSessionStorage();
	}

	@Override
	public TouchScreen getTouch() {
		return ((HasTouchScreen) wrapped).getTouch();
	}
}

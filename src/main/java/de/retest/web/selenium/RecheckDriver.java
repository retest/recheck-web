package de.retest.web.selenium;

import java.util.Collection;
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
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.web.RecheckWebImpl;

public class RecheckDriver implements WebDriver, JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText,
		FindsByName, FindsByCssSelector, FindsByTagName, FindsByXPath, HasInputDevices, HasCapabilities, Interactive,
		TakesScreenshot {

	private final RemoteWebDriver wrapped;
	private RootElement lastExpectedState;
	private RootElement lastActualState;

	public RecheckDriver( final RemoteWebDriver wrapped ) {
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

	public WebElement findElement( final ByBestMatchToRetestId by ) {
		if ( lastExpectedState == null ) {
			throw new IllegalStateException( "You must use the " + RecheckWebImpl.class.getSimpleName()
					+ " and first check the state before being able to use the retest ID locator." );
		}
		final Element searchedFor = by.findElement( lastExpectedState, lastActualState );
		return wrapped.findElement( By.xpath( searchedFor.getIdentifyingAttributes().getPath() ) );
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
			return wrapped.findElement( by );
		} catch ( final NoSuchElementException e ) {
			final WebElement matchedOld = TestHealer.findElement( by, this );
			if ( matchedOld == null ) {
				throw e;
			}
			return matchedOld;
		}
	}

	@Override
	public List<WebElement> findElements( final By by ) {
		return wrapped.findElements( by );
	}

	@Override
	public WebElement findElementById( final String using ) {
		return wrapped.findElementById( using );
	}

	@Override
	public List<WebElement> findElementsById( final String using ) {
		return wrapped.findElementsById( using );
	}

	@Override
	public WebElement findElementByClassName( final String using ) {
		return wrapped.findElementByClassName( using );
	}

	@Override
	public List<WebElement> findElementsByClassName( final String using ) {
		return wrapped.findElementsByClassName( using );
	}

	@Override
	public WebElement findElementByLinkText( final String using ) {
		return wrapped.findElementByLinkText( using );
	}

	@Override
	public List<WebElement> findElementsByLinkText( final String using ) {
		return wrapped.findElementsByLinkText( using );
	}

	@Override
	public WebElement findElementByPartialLinkText( final String using ) {
		return wrapped.findElementByPartialLinkText( using );
	}

	@Override
	public List<WebElement> findElementsByPartialLinkText( final String using ) {
		return wrapped.findElementsByPartialLinkText( using );
	}

	@Override
	public WebElement findElementByName( final String using ) {
		return wrapped.findElementByName( using );
	}

	@Override
	public List<WebElement> findElementsByName( final String using ) {
		return wrapped.findElementsByName( using );
	}

	@Override
	public WebElement findElementByCssSelector( final String using ) {
		return wrapped.findElementByCssSelector( using );
	}

	@Override
	public List<WebElement> findElementsByCssSelector( final String using ) {
		return wrapped.findElementsByCssSelector( using );
	}

	@Override
	public WebElement findElementByTagName( final String using ) {
		return wrapped.findElementByTagName( using );
	}

	@Override
	public List<WebElement> findElementsByTagName( final String using ) {
		return wrapped.findElementsByTagName( using );
	}

	@Override
	public WebElement findElementByXPath( final String using ) {
		return wrapped.findElementByXPath( using );
	}

	@Override
	public List<WebElement> findElementsByXPath( final String using ) {
		return wrapped.findElementsByXPath( using );
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
		return wrapped.executeScript( script, args );
	}

	@Override
	public Object executeAsyncScript( final String script, final Object... args ) {
		return wrapped.executeAsyncScript( script, args );
	}

	@Override
	public <X> X getScreenshotAs( final OutputType<X> target ) {
		return wrapped.getScreenshotAs( target );
	}

	@Override
	public Keyboard getKeyboard() {
		return wrapped.getKeyboard();
	}

	@Override
	public Mouse getMouse() {
		return wrapped.getMouse();
	}

	@Override
	public Capabilities getCapabilities() {
		return wrapped.getCapabilities();
	}

	@Override
	public void perform( final Collection<Sequence> actions ) {
		wrapped.perform( actions );
	}

	@Override
	public void resetInputState() {
		wrapped.resetInputState();
	}
}

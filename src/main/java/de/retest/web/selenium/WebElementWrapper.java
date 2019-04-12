package de.retest.web.selenium;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class WebElementWrapper implements WebElement {

	private final WebElement delegate;
	private final AutocheckingRecheckDriver driver;

	public WebElementWrapper( final WebElement delegate, final AutocheckingRecheckDriver driver ) {
		if ( delegate instanceof WebElementWrapper ) {
			throw new IllegalArgumentException( "Cannot wrap WebElementWrapper inside WebElementWrapper." );
		}
		this.delegate = delegate;
		this.driver = driver;
	}

	@Override
	public <X> X getScreenshotAs( final OutputType<X> target ) throws WebDriverException {
		return delegate.getScreenshotAs( target );
	}

	protected static String toStep( final String action, final WebElement delegate ) {
		String result = delegate.toString();
		if ( result.contains( "->" ) ) {
			// remove driver info
			result = result.substring( result.lastIndexOf( "->" ) + 2 ).trim();
			// remove trailing ]
			result = result.substring( 0, result.length() - 1 );
			// remove identification criterion (e.g. id, class, ...)
			result = result.substring( result.lastIndexOf( ':' ) + 1 ).trim();
			return action + "_" + result;
		}
		return result;
	}

	@Override
	public void click() {
		driver.check( toStep( "click", delegate ) );
		delegate.click();
	}

	@Override
	public void submit() {
		driver.check( toStep( "submit", delegate ) );
		delegate.submit();
	}

	@Override
	public void sendKeys( final CharSequence... keysToSend ) {
		driver.check( toStep( "enter", delegate ) );
		delegate.sendKeys( keysToSend );
	}

	@Override
	public void clear() {
		driver.check( toStep( "clear", delegate ) );
		delegate.clear();
	}

	@Override
	public String getTagName() {
		return delegate.getTagName();
	}

	@Override
	public String getAttribute( final String name ) {
		return delegate.getAttribute( name );
	}

	@Override
	public boolean isSelected() {
		return delegate.isSelected();
	}

	@Override
	public boolean isEnabled() {
		return delegate.isEnabled();
	}

	@Override
	public String getText() {
		return delegate.getText();
	}

	@Override
	public List<WebElement> findElements( final By by ) {
		return delegate.findElements( by ).stream() //
				.map( element -> new WebElementWrapper( element, driver ) ) //
				.collect( Collectors.toList() );
	}

	@Override
	public WebElement findElement( final By by ) {
		return new WebElementWrapper( delegate.findElement( by ), driver );
	}

	@Override
	public boolean isDisplayed() {
		return delegate.isDisplayed();
	}

	@Override
	public Point getLocation() {
		return delegate.getLocation();
	}

	@Override
	public Dimension getSize() {
		return delegate.getSize();
	}

	@Override
	public Rectangle getRect() {
		return delegate.getRect();
	}

	@Override
	public String getCssValue( final String propertyName ) {
		return delegate.getCssValue( propertyName );
	}
}

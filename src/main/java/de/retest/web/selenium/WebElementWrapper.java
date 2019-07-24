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

	@Override
	public void click() {
		delegate.click();
		driver.check( "click", delegate );
	}

	@Override
	public void submit() {
		delegate.submit();
		driver.check( "submit", delegate );
	}

	@Override
	public void sendKeys( final CharSequence... keysToSend ) {
		delegate.sendKeys( keysToSend );
		driver.check( "enter", delegate, (Object[]) keysToSend );
	}

	@Override
	public void clear() {
		delegate.clear();
		driver.check( "clear", delegate );
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
        
        public WebElement getWrappedElement() {
                return delegate;
        }
}

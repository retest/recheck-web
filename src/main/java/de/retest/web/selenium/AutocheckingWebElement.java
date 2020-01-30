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
import org.openqa.selenium.WrapsElement;

public class AutocheckingWebElement implements WebElement, WrapsElement {

	private final WebElement wrappedElement;
	private final AutocheckingRecheckDriver driver;

	public AutocheckingWebElement( final WebElement wrappedElement, final AutocheckingRecheckDriver driver ) {
		if ( wrappedElement instanceof AutocheckingWebElement ) {
			throw new IllegalArgumentException( "Cannot wrap WebElementWrapper inside WebElementWrapper." );
		}
		this.wrappedElement = wrappedElement;
		this.driver = driver;
	}

	/**
	 * Skip checks for actions performed on this web element.
	 *
	 * @return returns the plain WebElement
	 */
	public WebElement skipCheck() {
		return wrappedElement;
	}

	@Override
	public <X> X getScreenshotAs( final OutputType<X> target ) throws WebDriverException {
		return wrappedElement.getScreenshotAs( target );
	}

	@Override
	public void click() {
		wrappedElement.click();
		driver.check( "click", wrappedElement );
	}

	@Override
	public void submit() {
		wrappedElement.submit();
		driver.check( "submit", wrappedElement );
	}

	@Override
	public void sendKeys( final CharSequence... keysToSend ) {
		wrappedElement.sendKeys( keysToSend );
		driver.check( "enter", wrappedElement, (Object[]) keysToSend );
	}

	@Override
	public void clear() {
		wrappedElement.clear();
		driver.check( "clear", wrappedElement );
	}

	@Override
	public String getTagName() {
		return wrappedElement.getTagName();
	}

	@Override
	public String getAttribute( final String name ) {
		return wrappedElement.getAttribute( name );
	}

	@Override
	public boolean isSelected() {
		return wrappedElement.isSelected();
	}

	@Override
	public boolean isEnabled() {
		return wrappedElement.isEnabled();
	}

	@Override
	public String getText() {
		return wrappedElement.getText();
	}

	@Override
	public List<WebElement> findElements( final By by ) {
		return wrappedElement.findElements( by ).stream() //
				.map( element -> new AutocheckingWebElement( element, driver ) ) //
				.collect( Collectors.toList() );
	}

	@Override
	public WebElement findElement( final By by ) {
		return new AutocheckingWebElement( wrappedElement.findElement( by ), driver );
	}

	@Override
	public boolean isDisplayed() {
		return wrappedElement.isDisplayed();
	}

	@Override
	public Point getLocation() {
		return wrappedElement.getLocation();
	}

	@Override
	public Dimension getSize() {
		return wrappedElement.getSize();
	}

	@Override
	public Rectangle getRect() {
		return wrappedElement.getRect();
	}

	@Override
	public String getCssValue( final String propertyName ) {
		return wrappedElement.getCssValue( propertyName );
	}

	@Override
	public WebElement getWrappedElement() {
		return wrappedElement;
	}
}

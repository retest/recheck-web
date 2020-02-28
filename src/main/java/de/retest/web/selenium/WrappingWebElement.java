package de.retest.web.selenium;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class WrappingWebElement implements WebElement, WrapsDriver, WrapsElement {

	private final WebDriver driver;
	private final WebElement element;

	public static WebElement wrap( final WebDriver driver, final WebElement element ) {
		return new WrappingWebElement( driver, element );
	}

	@Override
	public WebDriver getWrappedDriver() {
		return driver;
	}

	@Override
	public WebElement getWrappedElement() {
		return element;
	}

	@Override
	public void click() {
		element.click();
	}

	@Override
	public void submit() {
		element.submit();
	}

	@Override
	public void sendKeys( final CharSequence... keysToSend ) {
		element.sendKeys( keysToSend );
	}

	@Override
	public void clear() {
		element.clear();
	}

	@Override
	public String getTagName() {
		return element.getTagName();
	}

	@Override
	public String getAttribute( final String name ) {
		return element.getAttribute( name );
	}

	@Override
	public boolean isSelected() {
		return element.isSelected();
	}

	@Override
	public boolean isEnabled() {
		return element.isEnabled();
	}

	@Override
	public String getText() {
		return element.getText();
	}

	@Override
	public List<WebElement> findElements( final org.openqa.selenium.By by ) {
		return element.findElements( by ).stream() //
				.map( element -> new WrappingWebElement( driver, element ) ) //
				.collect( Collectors.toList() );
	}

	@Override
	public WebElement findElement( final By by ) {
		return new WrappingWebElement( driver, element.findElement( by ) );
	}

	@Override
	public boolean isDisplayed() {
		return element.isDisplayed();
	}

	@Override
	public Point getLocation() {
		return element.getLocation();
	}

	@Override
	public Dimension getSize() {
		return element.getSize();
	}

	@Override
	public Rectangle getRect() {
		return element.getRect();
	}

	@Override
	public String getCssValue( final String propertyName ) {
		return element.getCssValue( propertyName );
	}

	@Override
	public <X> X getScreenshotAs( final OutputType<X> target ) throws WebDriverException {
		return element.getScreenshotAs( target );
	}

	@Override
	public String toString() {
		return element.toString();
	}
}

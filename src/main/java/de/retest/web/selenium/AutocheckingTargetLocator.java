package de.retest.web.selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AutocheckingTargetLocator implements WebDriver.TargetLocator {

	private final WebDriver.TargetLocator delegate;
	private final AutocheckingRecheckDriver driver;

	@Override
	public WebDriver frame( final int index ) {
		return delegate.frame( index );
	}

	@Override
	public WebDriver frame( final String nameOrId ) {
		return delegate.frame( nameOrId );
	}

	@Override
	public WebDriver frame( final WebElement frameElement ) {
		return delegate.frame( frameElement );
	}

	@Override
	public WebDriver parentFrame() {
		return delegate.parentFrame();
	}

	@Override
	public WebDriver window( final String nameOrHandle ) {
		final WebDriver driver = delegate.window( nameOrHandle );
		this.driver.check( "switch-window" );
		return driver;
	}

	@Override
	public WebDriver defaultContent() {
		return delegate.defaultContent();
	}

	@Override
	public WebElement activeElement() {
		return delegate.activeElement();
	}

	@Override
	public Alert alert() {
		return delegate.alert();
	}
}

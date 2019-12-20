package de.retest.web.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WrapsDriver;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor( access = AccessLevel.PACKAGE, staticName = "of" )
public class ImplicitDriverWrapper implements WrapsDriver {

	private final WebDriver wrapped;

	@Override
	public WebDriver getWrappedDriver() {
		return wrapped;
	}
}

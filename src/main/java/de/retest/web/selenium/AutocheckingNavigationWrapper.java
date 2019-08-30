package de.retest.web.selenium;

import java.net.URL;

import org.openqa.selenium.WebDriver.Navigation;

public class AutocheckingNavigationWrapper implements Navigation {

	private final Navigation delegate;
	private final AutocheckingRecheckDriver autocheckingDriver;

	public AutocheckingNavigationWrapper( final Navigation delegate,
			final AutocheckingRecheckDriver autocheckingDriver ) {
		this.delegate = delegate;
		this.autocheckingDriver = autocheckingDriver;
	}

	@Override
	public void back() {
		delegate.back();
		autocheckingDriver.check( "navigate-back" );
	}

	@Override
	public void forward() {
		delegate.forward();
		autocheckingDriver.check( "navigate-backforward" );
	}

	@Override
	public void to( final String url ) {
		delegate.to( url );
		autocheckingDriver.check( "navigate-to" );
	}

	@Override
	public void to( final URL url ) {
		delegate.to( url );
		autocheckingDriver.check( "navigate-to" );
	}

	@Override
	public void refresh() {
		delegate.refresh();
		autocheckingDriver.check( "refresh" );
	}

}

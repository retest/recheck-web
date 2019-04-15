package de.retest.web.selenium;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.web.RecheckWebImpl;

public class AutocheckingRecheckDriver extends RecheckDriver {

	private RecheckWebImpl re;
	private final AutocheckingCheckNamingStrategy namingStrategy;

	public AutocheckingRecheckDriver( final RemoteWebDriver wrapped ) {
		super( wrapped );
		namingStrategy = new CounterCheckNamingStrategy();
	}

	public void startTest() {
		namingStrategy.nextTest();
		if ( re == null ) {
			re = new RecheckWebImpl();
		}
		re.startTest();
	}

	public void startTest( final String testName ) {
		if ( re == null ) {
			re = new RecheckWebImpl();
		}
		re.startTest( testName );
	}

	public void capTest() {
		check( "final", null );
		namingStrategy.nextTest();
		re.capTest();
	}

	public void cap() {
		namingStrategy.nextTest();
		re.cap();
	}

	@Override
	public void get( final String url ) {
		super.get( url );
		check( "initial", null );
	}

	@Override
	public void close() {
		// Is this sensible? What about tests using separate sessions?
		namingStrategy.nextTest();
		re.cap();
		super.close();
	}

	@Override
	public void quit() {
		// Is this sensible? What about tests using separate sessions?
		namingStrategy.nextTest();
		re.cap();
		super.quit();
	}

	@Override
	public WebElement findElement( final ByBestMatchToRetestId by ) {
		return new WebElementWrapper( super.findElement( by ), this );
	}

	@Override
	public WebElement findElement( final By by ) {
		return new WebElementWrapper( super.findElement( by ), this );
	}

	@Override
	public List<WebElement> findElements( final By by ) {
		return super.findElements( by ).stream() //
				.map( element -> new WebElementWrapper( element, this ) ) //
				.collect( Collectors.toList() );
	}

	public void check( final String action, final WebElement target, final Object... params ) {
		re.check( this, namingStrategy.getUniqueCheckName( action, target, params ) );
	}
}

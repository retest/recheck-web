package de.retest.web.selenium;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.RecheckOptions;
import de.retest.web.RecheckWebImpl;

public class AutocheckingRecheckDriver extends RecheckDriver {

	private RecheckWebImpl re;
	private final RecheckOptions options;
	private final AutocheckingCheckNamingStrategy namingStrategy;

	public AutocheckingRecheckDriver( final RemoteWebDriver wrapped ) {
		super( wrapped );
		namingStrategy = new CounterCheckNamingStrategy();
		options = RecheckOptions.builder().build();
	}

	// TODO Use RecheckWebOptions
	public AutocheckingRecheckDriver( final RemoteWebDriver wrapped, final RecheckOptions options,
			final AutocheckingCheckNamingStrategy namingStrategy ) {
		super( wrapped );
		this.options = options;
		// TODO Incorporate AutocheckingCheckNamingStrategy into RecheckWebBuilder
		this.namingStrategy = namingStrategy; // options.getNamingStrategy();
	}

	public void startTest() {
		namingStrategy.nextTest();
		if ( re == null ) {
			re = new RecheckWebImpl( options );
		}
		re.startTest();
	}

	public void startTest( final String testName ) {
		if ( re == null ) {
			re = new RecheckWebImpl( options );
		}
		re.startTest( testName );
	}

	public void capTest() {
		re.capTest();
	}

	public void cap() {
		namingStrategy.nextTest();
		re.cap();
	}

	@Override
	public void get( final String url ) {
		super.get( url );
		check( "get", null );
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

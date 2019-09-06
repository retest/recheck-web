package de.retest.web.selenium;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.RecheckOptions;
import de.retest.web.RecheckWebImpl;

/**
 * Extends {@link UnbreakableDriver} and automagically creates a check <em>after</em> any executed action (usually
 * starting with {@link WebDriver#get(String)}). Consequently, you can omit using a {@code Recheck} instance such as
 * {@code RecheckImpl} or {@code RecheckWebImpl} instance. It utilizes the given {@link AutocheckingCheckNamingStrategy}
 * to create names for the checks.
 */
public class AutocheckingRecheckDriver extends UnbreakableDriver {

	private RecheckWebImpl re;
	private final RecheckOptions options;
	private final AutocheckingCheckNamingStrategy namingStrategy;

	public AutocheckingRecheckDriver( final RemoteWebDriver wrapped ) {
		this( wrapped, RecheckWebOptions.builder().build() );
	}

	/**
	 * Use {@link #AutocheckingRecheckDriver(RemoteWebDriver, RecheckWebOptions)} instead.
	 */
	@Deprecated
	public AutocheckingRecheckDriver( final RemoteWebDriver wrapped, final RecheckOptions options ) {
		super( wrapped );
		this.options = options;
		namingStrategy = RecheckWebOptions.builder().build().getNamingStrategy();
	}

	public AutocheckingRecheckDriver( final RemoteWebDriver wrapped, final RecheckWebOptions options ) {
		super( wrapped );
		this.options = options;
		namingStrategy = options.getNamingStrategy();
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
		try {
			// Is this sensible? What about tests using separate sessions?
			namingStrategy.nextTest();
			re.cap();
		} finally {
			super.quit();
		}
	}

	@Override
	public WebElement findElement( final ByBestMatchToRetestId by ) {
		final WebElement wrapped = super.findElement( by );
		String result = wrapped.toString();
		// replace " -> xpath: HTML[1]/BODY[1]/A[1]"
		result = result.substring( 0, result.lastIndexOf( " -> xpath: " ) );
		final String representation = result + " -> retestId: " + by.getRetestId() + "]";
		return new WebElementWrapper( wrapped, this ) {
			@Override
			public String toString() {
				return representation;
			}
		};
	}

	@Override
	public WebElement findElement( final By by ) {
		final WebElement result = super.findElement( by );
		if ( result instanceof WebElementWrapper ) {
			// Element was not found, so we already have it wrapped
			return result;
		}
		return new WebElementWrapper( result, this );
	}

	@Override
	public List<WebElement> findElements( final By by ) {
		return super.findElements( by ).stream() //
				.map( element -> new WebElementWrapper( element, this ) ) //
				.collect( Collectors.toList() );
	}

	void check( final String action, final WebElement target, final Object... params ) {
		if ( re == null ) {
			startTest();
		}
		re.check( this, namingStrategy.getUniqueCheckName( action, target, params ) );
	}

	void check( final String action ) {
		if ( re == null ) {
			startTest();
		}
		re.check( this, namingStrategy.getUniqueCheckName( action ) );
	}

	@Override
	public Navigation navigate() {
		return new AutocheckingNavigationWrapper( super.navigate(), this );
	}
}

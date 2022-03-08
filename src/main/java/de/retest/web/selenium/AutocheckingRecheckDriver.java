package de.retest.web.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.retest.recheck.Recheck;
import de.retest.recheck.RecheckLifecycle;
import de.retest.recheck.RecheckOptions;
import de.retest.web.RecheckWebImpl;
import de.retest.web.RecheckWebOptions;

/**
 * Extends {@link UnbreakableDriver} and automagically creates a check <em>after</em> any executed action (usually
 * starting with {@link WebDriver#get(String)}). Consequently, you can omit using a {@code Recheck} instance such as
 * {@code RecheckImpl} or {@code RecheckWebImpl} instance. It utilizes the given {@link AutocheckingCheckNamingStrategy}
 * to create names for the checks.
 *
 * Also handles {@link Recheck#startTest()}, {@link Recheck#capTest()} and on browser quit {@link Recheck#cap()}.
 */
public class AutocheckingRecheckDriver extends UnbreakableDriver implements RecheckLifecycle {

	private RecheckWebImpl re;
	private final RecheckOptions options;
	private final AutocheckingCheckNamingStrategy checkNamingStrategy;
	private final long autocheckingDelayMillis;

	public AutocheckingRecheckDriver( final RemoteWebDriver wrapped ) {
		this( wrapped, RecheckWebOptions.builder().build() );
	}

	/**
	 * @param wrapped
	 *            The {@link RemoteWebDriver} to wrap.
	 * @param options
	 *            The {@link RecheckOptions} or {@link RecheckWebOptions} to use.
	 */
	public AutocheckingRecheckDriver( final RemoteWebDriver wrapped, final RecheckOptions options ) {
		super( wrapped );
		this.options = options;
		final RecheckWebOptions webOptions = options instanceof RecheckWebOptions ? (RecheckWebOptions) options
				: RecheckWebOptions.builder().build();
		checkNamingStrategy = webOptions.getCheckNamingStrategy();
		autocheckingDelayMillis = webOptions.getAutocheckingDelayMillis();
	}

	/**
	 * @deprecated use {@link #AutocheckingRecheckDriver(RemoteWebDriver, RecheckOptions)} instead.
	 *
	 * @param wrapped
	 *            The {@link RemoteWebDriver} to wrap.
	 * @param options
	 *            The {@link RecheckOptions} to use.
	 * @param checkNamingStrategy
	 *            The {@link AutocheckingCheckNamingStrategy} to use.
	 */
	@Deprecated
	public AutocheckingRecheckDriver( final RemoteWebDriver wrapped, final RecheckOptions options,
			final AutocheckingCheckNamingStrategy checkNamingStrategy ) {
		super( wrapped );
		this.options = options;
		this.checkNamingStrategy = checkNamingStrategy;
		autocheckingDelayMillis = RecheckWebOptions.builder().build().getAutocheckingDelayMillis();
	}

	@Override
	public void startTest() {
		checkNamingStrategy.nextTest();
		if ( re == null ) {
			re = new RecheckWebImpl( options );
		}
		re.startTest();
	}

	@Override
	public void startTest( final String testName ) {
		if ( re == null ) {
			re = new RecheckWebImpl( options );
		}
		re.startTest( testName );
	}

	@Override
	public void capTest() {
		re.capTest();
	}

	@Override
	public void cap() {
		checkNamingStrategy.nextTest();
		re.cap();
	}

	@Override
	public void get( final String url ) {
		super.get( url );
		check( "get", null );
	}

	@Override
	public void close() {
		super.close();
		ifBrowserIsNotRunning( this::cap ); // cap if close window quit the driver
	}

	@Override
	public void quit() {
		try {
			cap();
		} finally {
			super.quit();
		}
	}

	private void ifBrowserIsNotRunning( final Runnable call ) {
		try {
			super.getWindowHandles();
		} catch ( NullPointerException | NoSuchSessionException e ) {
			call.run();
		}
	}

	@Override
	protected WebElement wrap( final WebElement element ) {
		return AutocheckingWebElement.of( element, this );
	}

	@Override
	public AutocheckingWebElement findElement( final ByBestMatchToRetestId by ) {
		final WebElement wrapped = super.findElement( by );
		String result = wrapped.toString();
		// replace " -> xpath: HTML[1]/BODY[1]/A[1]"
		result = result.substring( 0, result.lastIndexOf( " -> xpath: " ) );
		final String representation = result + " -> retestId: " + by.getRetestId() + "]";
		return AutocheckingWebElement.of( wrapped, this, representation );
	}

	@Override
	public AutocheckingWebElement findElement( final By by ) {
		return (AutocheckingWebElement) super.findElement( by );
	}

	void check( final String action, final WebElement target, final Object... params ) {
		if ( re == null ) {
			startTest();
		}
		waitAutocheckingDelay();
		re.check( ImplicitDriverWrapper.of( this ), checkNamingStrategy.getUniqueCheckName( action, target, params ) );
	}

	public void check( final String action ) {
		if ( re == null ) {
			startTest();
		}
		waitAutocheckingDelay();
		re.check( ImplicitDriverWrapper.of( this ), checkNamingStrategy.getUniqueCheckName( action ) );
	}

	void waitAutocheckingDelay() {
		if ( autocheckingDelayMillis > 0 ) {
			try {
				Thread.sleep( autocheckingDelayMillis );
			} catch ( final InterruptedException e ) {
				// ignore
				Thread.currentThread().interrupt();
			}
		}
	}

	@Override
	public Navigation navigate() {
		return new AutocheckingNavigationWrapper( super.navigate(), this );
	}

	@Override
	public TargetLocator switchTo() {
		return new AutocheckingTargetLocator( super.switchTo(), this );
	}
}

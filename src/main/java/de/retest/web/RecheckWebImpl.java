package de.retest.web;

import java.io.File;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.ui.descriptors.SutState;
import de.retest.web.selenium.UnbreakableDriver;

/**
 * This class is specifically needed in conjunction with the {@link UnbreakableDriver}. For simple explicit calls to
 * {@link #check(Object, String)}, a {@link RecheckImpl} suffices.
 */
public class RecheckWebImpl extends RecheckImpl {

	private UnbreakableDriver driver;

	public RecheckWebImpl() {
		super();
	}

	public RecheckWebImpl( final RecheckOptions opts ) {
		super( opts );
	}

	@Override
	public void check( final Object driver, final RecheckAdapter seleniumAdapter, final String currentStep ) {
		if ( driver instanceof UnbreakableDriver ) {
			this.driver = (UnbreakableDriver) driver;
		}
		super.check( driver, seleniumAdapter, currentStep );
	}

	@Override
	public void check( final Object driver, final String currentStep ) {
		if ( driver instanceof UnbreakableDriver ) {
			this.driver = (UnbreakableDriver) driver;
		}
		super.check( driver, currentStep );
	}

	@Override
	public SutState loadExpected( final File file ) {
		final SutState result = super.loadExpected( file );
		if ( driver == null ) {
			throw new IllegalStateException(
					"Must first call a check-method with an UnbreakableDriver before being able to load a Golden Master (needed for unbreakable tests)!" );
		}
		if ( result != null ) {
			driver.setLastExpectedState( result.getRootElements().get( 0 ) );
		}
		return result;
	}

}

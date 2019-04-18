package de.retest.web;

import java.io.File;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.RecheckImpl;
import de.retest.recheck.RecheckOptions;
import de.retest.recheck.ui.descriptors.SutState;
import de.retest.web.selenium.UnbreakableDriver;

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
		this.driver = (UnbreakableDriver) driver;
		super.check( driver, seleniumAdapter, currentStep );
	}

	@Override
	public void check( final Object driver, final String currentStep ) {
		this.driver = (UnbreakableDriver) driver;
		super.check( driver, currentStep );
	}

	@Override
	public SutState loadExpected( final File file ) {
		final SutState result = super.loadExpected( file );
		if ( driver == null ) {
			throw new IllegalStateException( "Check should have been called before loadExpected!" );
		}
		if ( result != null ) {
			driver.setLastExpectedState( result.getRootElements().get( 0 ) );
		}
		return result;
	}

}

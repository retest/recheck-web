package de.retest.web;

import java.io.File;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.RecheckImpl;
import de.retest.ui.descriptors.SutState;
import de.retest.web.selenium.RecheckDriver;

public class RecheckWebImpl extends RecheckImpl {

	private RecheckDriver driver;

	@Override
	public void check( final Object driver, final RecheckAdapter seleniumAdapter, final String currentStep ) {
		this.driver = (RecheckDriver) driver;
		super.check( driver, seleniumAdapter, currentStep );
	}

	@Override
	public void check( final Object driver, final String currentStep ) {
		this.driver = (RecheckDriver) driver;
		super.check( driver, currentStep );
	}

	@Override
	protected SutState loadExpected( final File file ) {
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

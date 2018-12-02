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
	public SutState loadExpected( final File file ) {
		final SutState result = super.loadExpected( file );
		if ( driver == null ) {
			throw new IllegalStateException( "Check should have been called before loadExpected!" );
		}
		if ( result != null ) {
			// TODO use unobfuscated getRootElements in retest > 3.1.0
			driver.setLastExpectedState( result.a().get( 0 ) );
		}
		return result;
	}

}

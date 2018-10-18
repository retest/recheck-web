package de.retest.web;

import java.io.File;

import de.retest.recheck.RecheckImpl;
import de.retest.ui.descriptors.SutState;
import de.retest.web.selenium.RecheckDriver;

public class UnbreakableRecheckImpl extends RecheckImpl {

	private final RecheckDriver driver;

	public UnbreakableRecheckImpl( final RecheckDriver driver ) {
		this.driver = driver;
	}

	@Override
	protected SutState loadExpected( final File file ) {
		final SutState result = super.loadExpected( file );
		if ( result != null ) {
			driver.setLastExpectdState( result.getRootElements().get( 0 ) );
		}
		return result;
	}

}

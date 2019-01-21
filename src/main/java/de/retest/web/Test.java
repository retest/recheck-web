package de.retest.web;

import java.util.List;

public class Test {

	public boolean getFoo( final List<String> list ) {

		boolean result = false;

		for ( final String s : list ) {
			if ( s.equals( "Test" ) ) {
				result = true;
			}
		}

		return result;
	}

}

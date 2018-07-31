package de.retest.web;

public class AttributesConfigLoadException extends RuntimeException {

	public AttributesConfigLoadException( final String file, final Throwable cause ) {
		super( "Cannot read attributes file '" + file + "'.", cause );
	}
}

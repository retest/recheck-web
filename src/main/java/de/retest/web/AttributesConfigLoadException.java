package de.retest.web;

public class AttributesConfigLoadException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AttributesConfigLoadException( final String file, final Throwable cause ) {
		super( "Cannot read attributes file '" + file + "'.", cause );
	}
}

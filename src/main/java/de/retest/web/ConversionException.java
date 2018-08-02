package de.retest.web;

public class ConversionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConversionException( final String msg ) {
		super( msg );
	}

	public ConversionException( final String msg, final Exception e ) {
		super( msg, e );
	}

}

package de.retest.web.selenium;

public class NoElementWithReTestIdFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private final String retestId;

	public NoElementWithReTestIdFoundException( final String retestId ) {
		super( "No element with retest ID '" + retestId + "' found!" );
		this.retestId = retestId;
	}

	public String getRetestId() {
		return retestId;
	}
}

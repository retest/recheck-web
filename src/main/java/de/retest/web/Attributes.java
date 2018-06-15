package de.retest.web;

import java.util.List;

public class Attributes {

	private List<String> attributes;
	private List<String> identifyingAttributes;

	public List<String> getAttributes() {
		return attributes;
	}

	public void setAttributes( final List<String> attributes ) {
		this.attributes = attributes;
	}

	public List<String> getIdentifyingAttributes() {
		return identifyingAttributes;
	}

	public void setIdentifyingAttributes( final List<String> identifyingAttributes ) {
		this.identifyingAttributes = identifyingAttributes;
	}

}

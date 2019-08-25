package de.retest.web.mapping.path;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class XPathGenerator {

	private final Map<String, Integer> counter = new HashMap<>();
	@Getter
	private final String path;

	public XPathGenerator() {
		this( "" );
	}

	public XPathGenerator next( final String type ) {
		final int suffix = counter.merge( type, 1, Integer::sum );
		return new XPathGenerator( path + "/" + type + "[" + suffix + "]" );
	}

	@Override
	public String toString() {
		return path;
	}
}

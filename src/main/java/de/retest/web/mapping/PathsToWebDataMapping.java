package de.retest.web.mapping;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.openqa.selenium.JavascriptExecutor;

/**
 * Raw paths to web data mapping that is received via {@link JavascriptExecutor#executeScript(String, Object...)}.
 * Internally, it is a map of paths to maps of attributes. Example:
 *
 * <pre>
 * {
 *   /foo[1]        = { attribute0 = value0, attribute1 = value1, ... }
 *   /foo[1]/bar[1] = { attribute0 = value0, attribute1 = value1, ... }
 *   ...
 * }
 * </pre>
 */
public class PathsToWebDataMapping implements Iterable<Entry<String, WebData>> {

	private final Map<String, WebData> mapping;

	public PathsToWebDataMapping( final Map<String, Map<String, Object>> mapping ) {
		this.mapping = mapping.entrySet().stream() //
				.collect( Collectors.toMap( Entry::getKey, entry -> new WebData( entry.getValue() ) ) );
	}

	/**
	 * @param frameParentPath
	 *            The parent path of the frame.
	 * @param mapping
	 *            The raw map of paths to maps of attributes.
	 */
	public PathsToWebDataMapping( final String frameParentPath, final Map<String, Map<String, Object>> mapping ) {
		this.mapping = mapping.entrySet().stream() //
				.collect( Collectors.toMap( entry -> frameParentPath + entry.getKey().replace( "//", "/" ),
						entry -> new WebData( entry.getValue() ) ) );
	}

	public int size() {
		return mapping.size();
	}

	public WebData getWebData( final String path ) {
		return mapping.get( path );
	}

	@Override
	public Iterator<Entry<String, WebData>> iterator() {
		return mapping.entrySet().iterator();
	}

}

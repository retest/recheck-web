package de.retest.web.mapping;

import static org.apache.commons.lang3.StringUtils.countMatches;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

	private final LinkedHashMap<String, WebData> mapping;
	private final String rootPath;

	public PathsToWebDataMapping( final List<List<Object>> mapping ) {
		this( "/", mapping );
	}

	/**
	 * @param frameParentPath
	 *            The parent path of the frame.
	 * @param mapping
	 *            The raw map of paths to maps of attributes.
	 */
	public PathsToWebDataMapping( final String frameParentPath, final List<List<Object>> mapping ) {
		rootPath = frameParentPath + extractRootPath( mapping );
		this.mapping = convertMapping( frameParentPath, mapping );
	}

	@SuppressWarnings( "unchecked" )
	private LinkedHashMap<String, WebData> convertMapping( final String frameParentPath,
			final List<List<Object>> mapping ) {
		final LinkedHashMap<String, WebData> result = new LinkedHashMap<>();
		for ( final List<Object> list : mapping ) {
			result.put( frameParentPath + list.get( 0 ).toString().replace( "//", "/" ),
					new WebData( (Map<String, Object>) list.get( 1 ) ) );
		}
		return result;
	}

	private String extractRootPath( final List<List<Object>> mapping ) {
		String rootPath = null;
		for ( final List<Object> entry : mapping ) {
			if ( rootPath == null ) {
				rootPath = entry.get( 0 ).toString();
				continue;
			}
			if ( countMatches( entry.get( 0 ).toString(), "/" ) < countMatches( rootPath, "/" ) ) {
				rootPath = entry.get( 0 ).toString();
			}
		}
		return rootPath != null ? rootPath.replace( "//", "/" ) : "";
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

	public String getRootPath() {
		return rootPath;
	}
}

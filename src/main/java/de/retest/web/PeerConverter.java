package de.retest.web;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.web.mapping.PathsToWebDataMapping;
import de.retest.web.mapping.WebData;
import de.retest.web.mapping.WebDataFilter;

class PeerConverter {

	private final Map<String, WebElementPeer> converted = new HashMap<>();

	private final RetestIdProvider retestIdProvider;
	private final AttributesProvider attributesProvider;
	private final PathsToWebDataMapping mapping;
	private final String title;
	private final BufferedImage screenshot;
	private final DefaultValueFinder defaultValueFinder;
	private final String rootParentPath;

	private RootElementPeer root = null;

	public PeerConverter( final RetestIdProvider retestIdProvider, final AttributesProvider attributesProvider,
			final PathsToWebDataMapping mapping, final String title, final BufferedImage screenshot,
			final DefaultValueFinder defaultValueFinder, final String rootPath ) {
		this.retestIdProvider = retestIdProvider;
		this.attributesProvider = attributesProvider;
		this.mapping = mapping;
		this.title = title;
		this.screenshot = screenshot;
		this.defaultValueFinder = defaultValueFinder;
		rootParentPath = getRootParentPath( rootPath );
	}

	protected String getRootParentPath( final String rootPath ) {
		if ( rootPath == null ) {
			return null;
		}
		return rootPath.substring( 0, rootPath.lastIndexOf( "/" ) );
	}

	public RootElement convertToPeers() {
		retestIdProvider.reset();
		for ( final Entry<String, WebData> entry : mapping ) {
			final String path = entry.getKey();
			final WebData webData = entry.getValue();
			if ( WebDataFilter.shouldIgnore( webData ) ) {
				continue;
			}
			convertToPeer( path, webData );
		}

		if ( root == null ) {
			throw new NullPointerException( "RootElementPeer is null." );
		}

		return root.toElement( null );
	}

	private WebElementPeer convertToPeer( final String path, final WebData webData ) {
		final String parentPath = getParentPath( path );
		WebElementPeer peer = converted.get( path );

		if ( peer != null ) {
			return peer;
		}

		if ( isRoot( parentPath ) ) {
			assert root == null : "We can only have one root element!";
			root = new RootElementPeer( retestIdProvider, attributesProvider, webData, path, title, screenshot,
					defaultValueFinder );
			peer = root;
		} else {
			peer = new WebElementPeer( retestIdProvider, attributesProvider, webData, path, defaultValueFinder );
			WebElementPeer parent = converted.get( parentPath );
			if ( parent == null ) {
				parent = convertToPeer( parentPath, mapping.getWebData( parentPath ) );
			}
			parent.addChild( peer );
		}

		converted.put( path, peer );
		return peer;
	}

	protected boolean isRoot( final String parentPath ) {
		return parentPath == null || parentPath.equals( rootParentPath );
	}

	static String getParentPath( final String path ) {
		if ( !path.contains( "/" ) ) {
			return null;
		}
		final String parentPath = path.substring( 0, path.lastIndexOf( '/' ) );
		if ( parentPath.length() == 1 ) {
			return null;
		}
		return parentPath;
	}
}

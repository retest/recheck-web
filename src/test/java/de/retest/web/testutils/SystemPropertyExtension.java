package de.retest.web.testutils;

import java.util.Properties;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;

public class SystemPropertyExtension implements BeforeEachCallback, AfterEachCallback {

	private static final String BACKUP_STORE_KEY = "backup";

	@Override
	public void beforeEach( final ExtensionContext context ) throws Exception {
		final Properties backup = new Properties();
		backup.putAll( System.getProperties() );
		final Store store = context.getStore( Namespace.create( getClass(), context.getRequiredTestMethod() ) );
		store.put( BACKUP_STORE_KEY, backup );
		final SystemProperty prop = context.getTestMethod().get().getAnnotation( SystemProperty.class );
		if ( prop.value().isEmpty() ) {
			System.clearProperty( prop.key() );
		} else {
			System.setProperty( prop.key(), prop.value() );
		}
	}

	@Override
	public void afterEach( final ExtensionContext context ) throws Exception {
		final Store store = context.getStore( Namespace.create( getClass(), context.getRequiredTestMethod() ) );
		final Properties backup = store.get( BACKUP_STORE_KEY, Properties.class );
		System.setProperties( backup );
	}

}

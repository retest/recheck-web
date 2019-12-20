package de.retest.web.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Utility to handle both old (<code>org.openqa.selenium.*</code>) and new (<code>org.openqa.selenium.internal.*</code>)
 * Selenium wrappers for {@code WebElement}s and {@code WebDriver}s.
 */
public class SeleniumWrapperUtil {

	/**
	 * Determines a Selenium wrapper type (i.e. an element or a driver) and contains information for reflective access.
	 */
	@Getter
	@RequiredArgsConstructor
	public static class WrapperOf<T> {

		public static final WrapperOf<WebElement> ELEMENT = new WrapperOf<>( WebElement.class, "getWrappedElement",
				new String[] { "org.openqa.selenium.WrapsElement", "org.openqa.selenium.internal.WrapsElement" } );

		public static final WrapperOf<WebDriver> DRIVER = new WrapperOf<>( WebDriver.class, "getWrappedDriver",
				new String[] { "org.openqa.selenium.WrapsDriver", "org.openqa.selenium.internal.WrapsDriver" } );

		private final Class<? extends T> wrappedClass;
		private final String wrapperMethodName;
		private final String[] wrapperClassNames;
	}

	/**
	 * @param w
	 *            The wrapper type.
	 * @param o
	 *            The object to be checked.
	 * @return <code>true</code> if the given object is wrapper of the selected type, otherwise <code>false</code>.
	 */
	public static boolean isWrapper( final WrapperOf<?> w, final Object o ) {
		return getWrapperClass( w, o ) != null;
	}

	/**
	 * Extracts the wrapped element or driver from a given object while implementing backwards compatibility to the
	 * internal Selenium API. It is advised to call {@link #isWrapper(WrapperOf, Object)} before calling this method.
	 * 
	 * @param w
	 *            The wrapper type.
	 * @param o
	 *            The object to be used.
	 * @return The wrapped element or driver from the given object if it is instance of the selected type.
	 * 
	 * @throws RuntimeException
	 *             If the <code>getWrapped</code> method throws an exception.
	 * @throws UnsupportedOperationException
	 *             If the <code>getWrapped</code> method cannot be invoked or the object does not wrap an instance of
	 *             the selected type.
	 */
	public static <T> T getWrapped( final WrapperOf<? extends T> w, final Object o ) {
		final Class<? extends T> wrappedClass = w.getWrappedClass();
		final Class<?> clazz = getWrapperClass( w, o );
		if ( clazz == null ) {
			throw new IllegalArgumentException( "Type '" + o.getClass() + "' is not instance of any of "
					+ Arrays.toString( w.getWrapperClassNames() ) + "." );
		}
		final Object wrapped;
		try {
			wrapped = clazz.getMethod( w.getWrapperMethodName() ).invoke( o );
			if ( wrapped == null ) { // We can't determine the type of null. 
				return null;
			}
			if ( wrappedClass.isInstance( wrapped ) ) {
				return wrappedClass.cast( wrapped );
			}
		} catch ( final InvocationTargetException e ) {
			throw new RuntimeException(
					"Failed to invoke " + o.getClass().getSimpleName() + "#" + w.getWrapperMethodName() + ".",
					e.getTargetException() );
		} catch ( final NoSuchMethodException | IllegalAccessException e ) {
			throw new UnsupportedOperationException(
					"Failed to invoke " + o.getClass().getSimpleName() + "#" + w.getWrapperMethodName() + ".", e );
		}
		throw new UnsupportedOperationException( "Failed to retrieve expected type '" + wrappedClass.getSimpleName()
				+ "' of '" + w.getWrapperMethodName() + "', got '" + wrapped.getClass().getSimpleName() + "'" );
	}

	private static Class<?> getWrapperClass( final WrapperOf<?> w, final Object o ) {
		for ( final String wrapsElementClassName : w.getWrapperClassNames() ) {
			try {
				final Class<?> clazz = Class.forName( wrapsElementClassName );
				if ( clazz.isInstance( o ) ) {
					return clazz;
				}
			} catch ( final ClassNotFoundException e ) {}
		}
		return null;
	}

}

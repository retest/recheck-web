package de.retest.web.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import lombok.Getter;

/**
 * Utility to handle both old (<code>org.openqa.selenium.*</code>) and new (<code>org.openqa.selenium.internal.*</code>)
 * Selenium wrappers for {@code WebElement}s and {@code WebDriver}s.
 */
public class SeleniumWrapperUtil {

	/**
	 * Determines a Selenium wrapper type (i.e. an element or a driver) and contains information for reflective access.
	 */
	@Getter
	public static enum WrapperOf {

		ELEMENT( new String[] { "org.openqa.selenium.WrapsElement", "org.openqa.selenium.internal.WrapsElement" },
				"getWrappedElement" ),
		DRIVER( new String[] { "org.openqa.selenium.WrapsDriver", "org.openqa.selenium.internal.WrapsDriver" },
				"getWrappedDriver" ),;

		private WrapperOf( final String[] wrapperClassNames, final String wrapperMethodName ) {
			this.wrapperClassNames = wrapperClassNames;
			this.wrapperMethodName = wrapperMethodName;
		}

		private final String[] wrapperClassNames;
		private final String wrapperMethodName;

	}

	/**
	 * @param w
	 *            The wrapper type.
	 * @param o
	 *            The object to be checked.
	 * @return <code>true</code> if the given object is wrapper of the selected type, otherwise <code>false</code>.
	 */
	public static boolean isWrapper( final WrapperOf w, final Object o ) {
		return getWrapperClass( w, o ) != null;
	}

	/**
	 * @param w
	 *            The wrapper type.
	 * @param o
	 *            The object to be used.
	 * @return The wrapped element or driver from the given object if it is instance of the selected type, otherwise the
	 *         object itself.
	 */
	public static Object getWrapped( final WrapperOf w, final Object o ) {
		final Class<?> clazz = getWrapperClass( w, o );
		if ( clazz == null ) {
			throw new IllegalArgumentException( "Type '" + o.getClass() + "' is not instance of any of "
					+ Arrays.toString( w.getWrapperClassNames() ) + "." );
		}
		try {
			return clazz.getMethod( w.getWrapperMethodName() ).invoke( o );
		} catch ( final InvocationTargetException e ) {
			throw new RuntimeException(
					"Failed to invoke " + o.getClass().getSimpleName() + "#" + w.getWrapperMethodName() + ".",
					e.getTargetException() );
		} catch ( final NoSuchMethodException | IllegalAccessException e ) {
			throw new UnsupportedOperationException(
					"Failed to invoke " + o.getClass().getSimpleName() + "#" + w.getWrapperMethodName() + ".", e );
		}
	}

	private static Class<?> getWrapperClass( final WrapperOf w, final Object o ) {
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

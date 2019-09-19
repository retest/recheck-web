package de.retest.web.util;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

public class SeleniumWrapperUtil {

	@Getter
	public static enum WrapperOf {

		ELEMENT( Arrays.asList( "org.openqa.selenium.WrapsElement", "org.openqa.selenium.internal.WrapsElement" ),
				"getWrappedElement" ),
		DRIVER( Arrays.asList( "org.openqa.selenium.WrapsDriver", "org.openqa.selenium.internal.WrapsDriver" ),
				"getWrappedDriver" ),;

		private WrapperOf( final List<String> wrapperClassNames, final String wrapperMethodName ) {
			this.wrapperClassNames = wrapperClassNames;
			this.wrapperMethodName = wrapperMethodName;
		}

		private final List<String> wrapperClassNames;
		private final String wrapperMethodName;

	}

	public static boolean instanceOf( final WrapperOf wo, final Object o ) {
		return getClassIfInstanceOf( wo, o ) != null;
	}

	public static Object getWrapped( final WrapperOf wo, final Object o ) {
		final Class<?> clazz = getClassIfInstanceOf( wo, o );
		if ( clazz != null ) {
			try {
				return clazz.getMethod( wo.getWrapperMethodName() ).invoke( o );
			} catch ( final ReflectiveOperationException e ) {}
		}
		return o;
	}

	private static Class<?> getClassIfInstanceOf( final WrapperOf wo, final Object o ) {
		for ( final String wrapsElementClassName : wo.getWrapperClassNames() ) {
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

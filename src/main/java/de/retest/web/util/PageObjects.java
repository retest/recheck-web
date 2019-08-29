package de.retest.web.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.WebElement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PageObjects {

	public static List<WebElement> convertToList( final Object pageObject ) {
		final Class<?> clazz = pageObject.getClass();
		final Field[] fields = clazz.getDeclaredFields();
		return Stream.of( fields ) //
				.flatMap( field -> convertTo( pageObject, field ) ) //
				.collect( Collectors.toList() );
	}

	private static Stream<WebElement> convertTo( final Object pageObject, final Field field ) {
		final Class<?> type = field.getType();
		if ( WebElement.class.isAssignableFrom( type ) ) {
			return extractWebElement( pageObject, field );
		}
		if ( List.class.isAssignableFrom( type ) ) {
			return extractWebElementList( pageObject, field );
		}
		return Stream.empty();
	}

	private static Stream<WebElement> extractWebElement( final Object pageObject, final Field field ) {
		return getValue( pageObject, field ) //
				.map( WebElement.class::cast ) //
				.map( Stream::of ) //
				.orElseGet( Stream::empty );
	}

	private static Stream<WebElement> extractWebElementList( final Object pageObject, final Field field ) {
		final ParameterizedType generic = (ParameterizedType) field.getGenericType();
		final Type argument = generic.getActualTypeArguments()[0];
		if ( WebElement.class.isAssignableFrom( (Class<?>) argument ) ) {
			return getValue( pageObject, field ) //
					.<List<WebElement>>map( List.class::cast ) //
					.map( List::stream ) //
					.orElseGet( Stream::empty );
		}
		return Stream.empty();
	}

	private static Optional<Object> getValue( final Object pageObject, final Field field ) {
		field.setAccessible( true );
		try {
			return Optional.of( field.get( pageObject ) );
		} catch ( final IllegalAccessException e ) {
			log.error( "Could not access field {} of {}.", field, pageObject );
			return Optional.empty();
		}
	}
}

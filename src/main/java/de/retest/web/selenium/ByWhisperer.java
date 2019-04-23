package de.retest.web.selenium;

import java.lang.reflect.Field;

import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;

public class ByWhisperer {

	private ByWhisperer() {}

	public static String retrieveId( final ById by ) {
		try {
			final Field field = ById.class.getDeclaredField( "id" );
			field.setAccessible( true );
			return (String) field.get( by );
		} catch ( final ReflectiveOperationException e ) {
			throw new IllegalStateException( "ById does not have an 'id' field?", e );
		}
	}

	public static String retrieveCSSClassName( final ByClassName by ) {
		try {
			final Field field = ByClassName.class.getDeclaredField( "className" );
			field.setAccessible( true );
			return (String) field.get( by );
		} catch ( final ReflectiveOperationException e ) {
			throw new IllegalStateException( "ByClassName does not have a 'className' field?", e );
		}
	}

	public static String retrieveName( final ByName by ) {
		try {
			final Field field = ByName.class.getDeclaredField( "name" );
			field.setAccessible( true );
			return (String) field.get( by );
		} catch ( final ReflectiveOperationException e ) {
			throw new IllegalStateException( "ByName does not have a 'name' field?", e );
		}
	}

	public static String retrieveLinkText( final ByLinkText by ) {
		try {
			final Field field = ByLinkText.class.getDeclaredField( "linkText" );
			field.setAccessible( true );
			return (String) field.get( by );
		} catch ( final ReflectiveOperationException e ) {
			throw new IllegalStateException( "ByLinkText does not have a 'linkText' field?", e );
		}
	}

	public static String retrieveCssSelector( final ByCssSelector by ) {
		try {
			final Field field = ByCssSelector.class.getDeclaredField( "cssSelector" );
			field.setAccessible( true );
			return (String) field.get( by );
		} catch ( final ReflectiveOperationException e ) {
			throw new IllegalStateException( "ByCssSelector does not have a 'cssSelector' field?", e );
		}
	}
}

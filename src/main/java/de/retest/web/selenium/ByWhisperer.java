package de.retest.web.selenium;

import java.lang.reflect.Field;

import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByPartialLinkText;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.By.ByXPath;

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

	public static String retrieveCssClassName( final ByClassName by ) {
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

	public static String retrievePartialLinkText( final ByPartialLinkText by ) {
		try {
			final Field field = ByPartialLinkText.class.getDeclaredField( "partialLinkText" );
			field.setAccessible( true );
			return (String) field.get( by );
		} catch ( final ReflectiveOperationException e ) {
			throw new IllegalStateException( "ByPartialLinkText does not have a 'partialLinkText' field?", e );
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

	public static String retrieveXPath( final ByXPath byXPath ) {
		try {
			final Field field = ByXPath.class.getDeclaredField( "xpathExpression" );
			field.setAccessible( true );
			return (String) field.get( byXPath );
		} catch ( final ReflectiveOperationException e ) {
			throw new IllegalStateException( "ByXPath does not have a 'xpathExpression' field?", e );
		}
	}

	public static String retrieveTag( final ByTagName byTagName ) {
		try {
			final Field field = ByTagName.class.getDeclaredField( "tagName" );
			field.setAccessible( true );
			return (String) field.get( byTagName );
		} catch ( final ReflectiveOperationException e ) {
			throw new IllegalStateException( "ByTagName does not have a 'tagName' field?", e );
		}
	}
}

package de.retest.web.selenium;

import static de.retest.web.selenium.ByWhisperer.retrieveCssClassName;
import static de.retest.web.selenium.ByWhisperer.retrieveId;
import static de.retest.web.selenium.ByWhisperer.retrieveLinkText;
import static de.retest.web.selenium.ByWhisperer.retrieveName;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.By.ByXPath;

class ByWhispererTest {

	@Test
	void retrieveId_should_return_id() {
		final String id = "someId";
		assertThat( retrieveId( (ById) By.id( id ) ) ).isEqualTo( id );
	}

	@Test
	void retrieveCSSClassName_should_return_ClassName() {
		final String cssClass = "someClass";
		assertThat( retrieveCssClassName( (ByClassName) By.className( cssClass ) ) ).isEqualTo( cssClass );
	}

	@Test
	void retrieveName_should_return_Name() {
		final String name = "someName";
		assertThat( retrieveName( (ByName) By.name( name ) ) ).isEqualTo( name );
	}

	@Test
	void retrieveLinkText_should_return_LinkText() {
		final String linkText = "someLinkText";
		assertThat( retrieveLinkText( (ByLinkText) By.linkText( linkText ) ) ).isEqualTo( linkText );
	}

	@Test
	void retrieveCssSelector_should_return_Selector() {
		final String selector = "selector";
		assertThat( ByWhisperer.retrieveCssSelector( (ByCssSelector) By.cssSelector( selector ) ) )
				.isEqualTo( selector );
	}

	@Test
	void retrieveXPath_should_return_xpath() {
		final String xpath = "HTML[1]/DIV[1]";
		assertThat( ByWhisperer.retrieveXPath( (ByXPath) By.xpath( xpath ) ) ).isEqualTo( xpath );
	}

	@Test
	void retrieveTagName_should_return_tagName() {
		final String tag = "div";
		assertThat( ByWhisperer.retrieveTag( (ByTagName) By.tagName( tag ) ) ).isEqualTo( tag );
	}
}

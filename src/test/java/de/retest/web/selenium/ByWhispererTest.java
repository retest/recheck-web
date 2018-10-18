package de.retest.web.selenium;

import static de.retest.web.selenium.ByWhisperer.retrieveCSSClassName;
import static de.retest.web.selenium.ByWhisperer.retrieveId;
import static de.retest.web.selenium.ByWhisperer.retrieveLinkText;
import static de.retest.web.selenium.ByWhisperer.retrieveName;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By.ByClassName;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByLinkText;
import org.openqa.selenium.By.ByName;

class ByWhispererTest {

	@Test
	void retrieveId_should_return_id() {
		final String id = "someId";
		assertThat( retrieveId( (ById) By.id( id ) ) ).isEqualTo( id );
	}

	@Test
	void retrieveCSSClassName_should_return_ClassName() {
		final String cssClass = "someClass";
		assertThat( retrieveCSSClassName( (ByClassName) By.className( cssClass ) ) ).isEqualTo( cssClass );
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
}

package de.retest.web.selenium;

import static de.retest.recheck.ui.Path.fromString;
import static de.retest.recheck.ui.descriptors.Element.create;
import static de.retest.web.selenium.TestHealer.findElement;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.descriptors.RootElement;

class TestHealerTest {

	@Test
	public void ByCssSelector_machtes_elements_with_given_class() {
		final RecheckDriver wrapped = mock( RecheckDriver.class );
		final WebElement resultMarker = mock( WebElement.class );

		final RootElement state = mock( RootElement.class );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );

		final MutableAttributes attributes = new MutableAttributes();
		attributes.put( "class", "pure-button my-button menu-button" );

		final String xpath = "HTML[1]/DIV[1]";
		final IdentifyingAttributes identifying = IdentifyingAttributes.create( fromString( xpath ), "DIV" );
		final Element element = create( "id", state, identifying, attributes.immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		// assertThat( By.cssSelector( ".pure-button" ).matches( element ) ).isTrue();
		assertThat( findElement( By.cssSelector( ".pure-button" ), wrapped ) ).isEqualTo( resultMarker );
		// assertThat( By.cssSelector( ".special-class" ).matches( element ) ).isFalse();
		assertThat( findElement( By.cssSelector( ".special-class" ), wrapped ) ).isEqualTo( null );
	}

	@Test
	public void not_yet_implemented_ByCssSelector_should_throw_exception() {
		final RecheckDriver wrapped = mock( RecheckDriver.class );
		try {
			findElement( By.cssSelector( ".open > .dropdown-toggle.btn-primary" ), wrapped );
			fail( "Excpected exception" );
		} catch ( final IllegalArgumentException e ) {}

		try {
			findElement( By.cssSelector( ".btn-primary[disabled]" ), wrapped );
			fail( "Excpected exception" );
		} catch ( final IllegalArgumentException e ) {}

		try {
			findElement( By.cssSelector( "fieldset[disabled]" ), wrapped );
			fail( "Excpected exception" );
		} catch ( final IllegalArgumentException e ) {}

		try {
			findElement( By.cssSelector( ".btn-group-vertical > .btn:not(:first-child):not(:last-child)" ), wrapped );
			fail( "Excpected exception" );
		} catch ( final IllegalArgumentException e ) {}

		try {
			findElement( By.cssSelector( "[data-toggle=\"buttons\"] > .btn-group > .btn input[type=\"radio\"]" ),
					wrapped );
			fail( "Excpected exception" );
		} catch ( final IllegalArgumentException e ) {}

		try {
			findElement( By.cssSelector( ".input-group[class*=\"col-\"]" ), wrapped );
			fail( "Excpected exception" );
		} catch ( final IllegalArgumentException e ) {}
	}

}

package de.retest.web.selenium;

import static de.retest.recheck.ui.Path.fromString;
import static de.retest.recheck.ui.descriptors.Element.create;
import static de.retest.web.selenium.TestHealer.findElement;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import de.retest.recheck.report.ActionReplayResult;
import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.diff.ElementDifference;
import de.retest.recheck.ui.diff.StateDifference;

class TestHealerTest {

	RecheckDriver wrapped;
	WebElement resultMarker;
	RootElement state;
	ActionReplayResult actionReplayResult;
	ElementDifference elementDifference;
	ElementDifference otherElementDifference;
	List<ElementDifference> elementDifferences;
	StateDifference stateDifference;

	@BeforeEach
	void setUp() {
		wrapped = mock( RecheckDriver.class );
		resultMarker = mock( WebElement.class );
		state = mock( RootElement.class );
		actionReplayResult = mock( ActionReplayResult.class );
		elementDifference = mock( ElementDifference.class );
		otherElementDifference = mock( ElementDifference.class );
		stateDifference = mock( StateDifference.class );
		elementDifferences = new ArrayList<>();
		elementDifferences.add( elementDifference );
		elementDifferences.add( otherElementDifference );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );
		when( wrapped.getLastActionReplayResult() ).thenReturn( actionReplayResult );
		when( wrapped.getLastActionReplayResult().getStateDifference() ).thenReturn( stateDifference );
		when( stateDifference.getElementDifferences() ).thenReturn( elementDifferences );
		when( elementDifference.getRetestId() ).thenReturn( "retestId" );
		when( otherElementDifference.getRetestId() ).thenReturn( "retestId" );
	}

	@Test
	public void ByCssSelector_matches_elements_with_given_class() {
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

	@Test
	public void ByXPathExpression_matches_elements_with_given_xpath() {
		final String xpath = "HTML[1]/DIV[3]/DIV[3]/DIV[3]/DIV[2]";
		final IdentifyingAttributes identifying = IdentifyingAttributes.create( fromString( xpath ), "DIV" );
		final Element element = create( "id", state, identifying, new Attributes() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.xpath( "//div[3]/div[3]/div[3]/div[2]" ), wrapped ) ).isEqualTo( resultMarker );
		assertThat( findElement( By.xpath( "/html[1]/div[3]/div[3]/div[3]/div[2]" ), wrapped ) )
				.isEqualTo( resultMarker );
		assertThat( findElement( By.xpath( "//div[1]" ), wrapped ) ).isEqualTo( null );
	}

	@Test
	public void not_yet_implemented_ByXPathExpression_should_throw_exception() {
		try {
			findElement( By.xpath( "//div[@id='mw-content-text']/div[2]" ), wrapped );
			fail( "Excpected exception" );
		} catch ( final IllegalArgumentException e ) {}

		try {
			findElement( By.xpath( "//button[contains(.,'Search')]" ), wrapped );
			fail( "Excpected exception" );
		} catch ( final IllegalArgumentException e ) {}
	}
}

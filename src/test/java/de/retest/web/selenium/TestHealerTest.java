package de.retest.web.selenium;

import static de.retest.recheck.ui.Path.fromString;
import static de.retest.recheck.ui.descriptors.Element.create;
import static de.retest.web.selenium.TestHealer.findElement;
import static de.retest.web.selenium.TestHealer.isNotYetSupportedCssSelector;
import static de.retest.web.selenium.TestHealer.isNotYetSupportedXPathExpression;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import de.retest.recheck.ui.descriptors.Attribute;
import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.StringAttribute;

class TestHealerTest {

	@Test
	public void ByCssSelector_using_id_should_redirect() {
		final RecheckDriver wrapped = mock( RecheckDriver.class );
		final AutocheckingWebElement resultMarker = mock( AutocheckingWebElement.class );

		final RootElement state = mock( RootElement.class );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );

		final String xpath = "HTML[1]/DIV[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "DIV" );
		identCrit.add( new StringAttribute( "id", "special-button" ) );
		final Element element =
				create( "id", state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		// assertThat( By.cssSelector( "#special-button" ).matches( element ) ).isTrue();
		assertThat( findElement( By.cssSelector( "#special-button" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByCssSelector_using_tag_should_redirect() {
		final RecheckDriver wrapped = mock( RecheckDriver.class );
		final AutocheckingWebElement resultMarker = mock( AutocheckingWebElement.class );

		final RootElement state = mock( RootElement.class );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );

		final String xpath = "html[1]/div[1]";
		final IdentifyingAttributes identifying = IdentifyingAttributes.create( fromString( xpath ), "div" );
		final Element element = create( "id", state, identifying, new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		// assertThat( By.cssSelector( "div" ).matches( element ) ).isTrue();
		assertThat( findElement( By.cssSelector( "div" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByCssSelector_using_tag_with_hyphen_should_redirect() {
		final RecheckDriver wrapped = mock( RecheckDriver.class );
		final AutocheckingWebElement resultMarker = mock( AutocheckingWebElement.class );

		final RootElement state = mock( RootElement.class );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );

		final String xpath = "html[1]/ytd-grid-video-renderer[1]";
		final IdentifyingAttributes identifying =
				IdentifyingAttributes.create( fromString( xpath ), "ytd-grid-video-renderer" );
		final Element element = create( "id", state, identifying, new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		// assertThat( By.cssSelector( "div" ).matches( element ) ).isTrue();
		assertThat( findElement( By.cssSelector( "ytd-grid-video-renderer" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByCssSelector_matches_elements_with_given_attribute() {
		final RecheckDriver wrapped = mock( RecheckDriver.class );
		final AutocheckingWebElement resultMarker = mock( AutocheckingWebElement.class );

		final RootElement state = mock( RootElement.class );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );

		final MutableAttributes attributes = new MutableAttributes();
		attributes.put( "data-id", "myspecialID" );
		attributes.put( "disabled", "true" );

		final String xpath = "html[1]/div[1]";
		final IdentifyingAttributes identifying = IdentifyingAttributes.create( fromString( xpath ), "div" );
		final Element element = create( "id", state, identifying, attributes.immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.cssSelector( "[data-id=\"myspecialID\"]" ), wrapped ) ).isEqualTo( resultMarker );
		assertThat( findElement( By.cssSelector( "[disabled]" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByCssSelector_matches_elements_with_given_class() {
		final RecheckDriver wrapped = mock( RecheckDriver.class );
		final AutocheckingWebElement resultMarker = mock( AutocheckingWebElement.class );

		final RootElement state = mock( RootElement.class );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );

		final String xpath = "HTML[1]/DIV[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "DIV" );
		identCrit.add( new StringAttribute( "class", "pure-button my-button menu-button" ) );
		final Element element =
				create( "id", state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		// assertThat( By.cssSelector( ".pure-button" ).matches( element ) ).isTrue();
		assertThat( findElement( By.cssSelector( ".pure-button" ), wrapped ) ).isEqualTo( resultMarker );
		// assertThat( By.cssSelector( ".special-class" ).matches( element ) ).isFalse();
		assertThat( findElement( By.cssSelector( ".special-class" ), wrapped ) ).isEqualTo( null );
	}

	@Test
	public void empty_selectors_should_not_throw_exception() {
		final RecheckDriver wrapped = mock( RecheckDriver.class );
		final RootElement state = mock( RootElement.class );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );

		assertThat( findElement( By.cssSelector( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.className( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.id( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.linkText( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.name( "" ), wrapped ) ).isNull();
		// assertThat( findElement( By.partialLinkText( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.tagName( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.xpath( "" ), wrapped ) ).isNull();
	}

	@Test
	public void not_yet_implemented_ByCssSelector_should_be_logged() {
		assertThat( isNotYetSupportedCssSelector( ".open > .dropdown-toggle.btn-primary" ) ).isTrue();
		assertThat( isNotYetSupportedCssSelector( ".btn-primary[disabled] p" ) ).isTrue();
		assertThat( isNotYetSupportedCssSelector( ".btn-group-vertical > .btn:not(:first-child):not(:last-child)" ) )
				.isTrue();
		assertThat(
				isNotYetSupportedCssSelector( "[data-toggle=\"buttons\"] > .btn-group > .btn input[type=\"radio\"]" ) )
						.isTrue();
		assertThat( isNotYetSupportedCssSelector( ".input-group[class*=\"col-\"]" ) ).isTrue();
		assertThat( isNotYetSupportedCssSelector( "div~p" ) ).isTrue();
		assertThat( isNotYetSupportedCssSelector( "[href*=\"w3schools\"]" ) ).isTrue();
		assertThat( isNotYetSupportedCssSelector( "div,p" ) ).isTrue();
		assertThat( isNotYetSupportedCssSelector( ".class1.class2" ) ).isTrue();
		assertThat( isNotYetSupportedCssSelector( "p.intro" ) ).isTrue();

		// TODO
		// [attribute]			[target]				Selects all elements with a target attribute
		// [attribute=value]	[target=_blank]			Selects all elements with target="_blank"
		// [attribute~=value]	[title~=flower]			Selects all elements with a title attribute containing the word "flower"
		// [attribute|=value]	[lang|=en]				Selects all elements with a lang attribute value starting with "en"
		// [attribute^=value]	a[href^="https"]		Selects every element whose href attribute value begins with "https"
		// [attribute$=value]	a[href$=".pdf"]			Selects every element whose href attribute value ends with ".pdf"
		// [attribute*=value]	a[href*="w3schools"]	Selects every element whose href attribute value contains the substring "w3schools"

	}

	@Test
	public void ByXPathExpression_matches_elements_with_given_xpath() {
		final RecheckDriver wrapped = mock( RecheckDriver.class );
		final AutocheckingWebElement resultMarker = mock( AutocheckingWebElement.class );

		final RootElement state = mock( RootElement.class );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );

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
	public void not_yet_implemented_ByXPathExpression_should_log_warning() {
		assertThat( isNotYetSupportedXPathExpression( "//div[@id='mw-content-text']/div[2]" ) ).isTrue();
		assertThat( isNotYetSupportedXPathExpression( "//button[contains(.,'Search')]" ) ).isTrue();
	}
}

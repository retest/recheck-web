package de.retest.web.selenium;

import static de.retest.recheck.ui.Path.fromString;
import static de.retest.recheck.ui.descriptors.Element.create;
import static de.retest.web.AttributesUtil.ID;
import static de.retest.web.AttributesUtil.NAME;
import static de.retest.web.AttributesUtil.TEXT;
import static de.retest.web.selenium.TestHealer.findElement;
import static de.retest.web.selenium.TestHealer.isNotYetSupportedXPathExpression;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.openqa.selenium.By;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import de.retest.recheck.ui.descriptors.Attribute;
import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.descriptors.StringAttribute;
import de.retest.web.AttributesUtil;
import de.retest.web.selenium.css.PredicateBuilder;

class TestHealerTest {

	private RecheckDriver wrapped;
	private AutocheckingWebElement resultMarker;
	private RootElement state;

	@BeforeEach
	void setup() {
		wrapped = mock( RecheckDriver.class );
		resultMarker = mock( AutocheckingWebElement.class );
		state = mock( RootElement.class );
		when( wrapped.getLastExpectedState() ).thenReturn( state );
		when( wrapped.getLastActualState() ).thenReturn( state );
	}

	@Test
	public void ByCssSelector_using_id_should_redirect() {
		final String xpath = "HTML[1]/DIV[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "DIV" );
		identCrit.add( new StringAttribute( ID, "special-button" ) );
		final Element element =
				create( ID, state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.cssSelector( "#special-button" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByCssSelector_using_tag_should_redirect() {
		final String xpath = "html[1]/div[1]";
		final IdentifyingAttributes identifying = IdentifyingAttributes.create( fromString( xpath ), "div" );
		final Element element = create( ID, state, identifying, new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.cssSelector( "div" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByCssSelector_using_tag_with_hyphen_should_redirect() {
		final String xpath = "html[1]/ytd-grid-video-renderer[1]";
		final IdentifyingAttributes identifying =
				IdentifyingAttributes.create( fromString( xpath ), "ytd-grid-video-renderer" );
		final Element element = create( ID, state, identifying, new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.cssSelector( "ytd-grid-video-renderer" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByCssSelector_matches_elements_with_given_attribute() {
		configureByCssSelectorAttributeTests();

		assertAll( Stream.of( //
				"[data-id=\"myspecialID\"]", //
				"[disabled]", //
				"div[data-id=\"myspecialID\"]", //
				"div[disabled]", //
				".myClass[data-id=\"myspecialID\"]", //
				".myClass[disabled]", //
				"#myId[data-id=\"myspecialID\"]", //
				"#myId[disabled]" ) //
				.map( this::assertByCssSelector ) );
	}

	@Test
	public void ByCssSelector_matches_elements_with_given_attribute_value() {
		configureByCssSelectorAttributeTests();

		assertAll( assertAttributeValues( "~", "=\"special\"]" ) );
		assertAll( assertAttributeValues( "|", "=\"myspecial\"]" ) );
		assertAll( assertAttributeValues( "^", "=\"myspecial\"]" ) );
		assertAll( assertAttributeValues( "$", "=\"specialID\"]" ) );
		assertAll( assertAttributeValues( "*", "=\"special\"]" ) );
	}

	private Stream<Executable> assertAttributeValues( final String selectorChar, final String value ) {
		return Stream.of( //
				"[data-id" + selectorChar + value, //
				"div[data-id" + selectorChar + value, //
				".myClass[data-id" + selectorChar + value, //
				"#myId[data-id" + selectorChar + value ) //
				.map( this::assertByCssSelector );
	}

	private void configureByCssSelectorAttributeTests() {
		final MutableAttributes attributes = new MutableAttributes();
		attributes.put( "data-id", "myspecialID" );
		attributes.put( "disabled", "true" );

		final String xpath = "html[1]/div[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "div" );
		identCrit.add( new StringAttribute( "class", "myClass" ) );
		identCrit.add( new StringAttribute( ID, "myId" ) );
		final Element element = create( ID, state, new IdentifyingAttributes( identCrit ), attributes.immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );
	}

	@Test
	public void ByCssSelector_matches_elements_with_given_class() {
		final String xpath = "HTML[1]/DIV[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "DIV" );
		identCrit.add( new StringAttribute( "class", "pure-button my-button menu-button" ) );
		final Element element =
				create( ID, state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertAll( assertByCssSelector( ".pure-button" ), //
				assertByCssSelector( ".pure-button.my-button" ), //
				assertByCssSelector( ".pure-button .my-button" ) );

		assertThat( findElement( By.cssSelector( ".special-class" ), wrapped ) ).isEqualTo( null );
		assertThat( findElement( By.cssSelector( ".pure-button.special-class" ), wrapped ) ).isEqualTo( null );
		assertThat( findElement( By.cssSelector( ".pure-button .special-class" ), wrapped ) ).isEqualTo( null );
	}

	private Executable assertByCssSelector( final String hierarchicalClass ) {
		return () -> assertThat( findElement( By.cssSelector( hierarchicalClass ), wrapped ) )
				.isEqualTo( resultMarker );
	}

	@Test
	public void empty_selectors_should_not_throw_exception() {
		assertThat( findElement( By.cssSelector( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.className( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.id( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.linkText( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.name( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.partialLinkText( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.tagName( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.xpath( "" ), wrapped ) ).isNull();
	}

	@Test
	public void not_yet_implemented_ByCssSelector_should_be_logged() {
		final ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
		listAppender.start();
		((Logger) LoggerFactory.getLogger( PredicateBuilder.class )).addAppender( listAppender );
		final List<ILoggingEvent> logsList = listAppender.list;

		assertThat( findElement( By.cssSelector( ".open > .dropdown-toggle.btn-primary" ), wrapped ) ).isNull();
		assertThat( logsList.get( 0 ).getMessage() )
				.startsWith( "Unbreakable tests are not implemented for all CSS selectors." );
		assertThat( logsList.get( 0 ).getArgumentArray()[0] ).isEqualTo( "> .dropdown-toggle.btn-primary" );
		logsList.clear();

		assertThat( findElement( By.cssSelector( ".btn:not(:first-child):not(:last-child)" ), wrapped ) ).isNull();
		assertThat( logsList.get( 0 ).getMessage() )
				.startsWith( "Unbreakable tests are not implemented for all CSS selectors." );
		assertThat( logsList.get( 0 ).getArgumentArray()[0] ).isEqualTo( ":not(:first-child):not(:last-child)" );
		logsList.clear();

		assertThat( findElement( By.cssSelector( "div~p" ), wrapped ) ).isNull();
		assertThat( logsList.get( 0 ).getMessage() )
				.startsWith( "Unbreakable tests are not implemented for all CSS selectors." );
		assertThat( logsList.get( 0 ).getArgumentArray()[0] ).isEqualTo( "~p" );
		logsList.clear();

		assertThat( findElement( By.cssSelector( "div,p" ), wrapped ) ).isNull();
		assertThat( logsList.get( 0 ).getMessage() )
				.startsWith( "Unbreakable tests are not implemented for all CSS selectors." );
		assertThat( logsList.get( 0 ).getArgumentArray()[0] ).isEqualTo( ",p" );
		logsList.clear();
	}

	@Test
	public void ByXPathExpression_matches_elements_with_given_xpath() {
		final String xpath = "HTML[1]/DIV[3]/DIV[3]/DIV[3]/DIV[2]";
		final IdentifyingAttributes identifying = IdentifyingAttributes.create( fromString( xpath ), "DIV" );
		final Element element = create( ID, state, identifying, new Attributes() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.xpath( "//div[3]/div[3]/div[3]/div[2]" ), wrapped ) ).isEqualTo( resultMarker );
		assertThat( findElement( By.xpath( "/html[1]/div[3]/div[3]/div[3]/div[2]" ), wrapped ) )
				.isEqualTo( resultMarker );
		assertThat( findElement( By.xpath( "//div[1]" ), wrapped ) ).isEqualTo( null );
	}

	@Test
	public void ByLinkText_should_find_element() {
		final String xpath = "html[1]/a[1]";
		final String linkText = "my link text";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "a" );
		identCrit.add( new StringAttribute( TEXT, linkText ) );
		final Element element =
				create( ID, state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.linkText( linkText ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByPartialLinkText_should_find_element() {
		final String xpath = "html[1]/a[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "a" );
		identCrit.add( new StringAttribute( TEXT, "my link text" ) );
		final Element element =
				create( ID, state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.partialLinkText( "link" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByClassName_should_find_element() {
		final String xpath = "html[1]/a[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "a" );
		identCrit.add( new StringAttribute( AttributesUtil.CLASS, "myClass myOther" ) );
		final Element element =
				create( ID, state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.className( "myOther" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByTagName_should_find_element() {
		final String xpath = "html[1]/a[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "a" );
		final Element element =
				create( ID, state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.tagName( "a" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ByName_should_find_element() {
		final String xpath = "html[1]/a[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "a" );
		identCrit.add( new StringAttribute( NAME, "myName" ) );
		final Element element =
				create( ID, state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.name( "myName" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void ById_should_find_element() {
		final String xpath = "html[1]/a[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "a" );
		identCrit.add( new StringAttribute( AttributesUtil.ID, "myId" ) );
		final Element element =
				create( ID, state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.id( "myId" ), wrapped ) ).isEqualTo( resultMarker );
	}

	@Test
	public void not_yet_implemented_ByXPathExpression_should_log_warning() {
		assertThat( isNotYetSupportedXPathExpression( "//div[@id='mw-content-text']/div[2]" ) ).isTrue();
		assertThat( isNotYetSupportedXPathExpression( "//button[contains(.,'Search')]" ) ).isTrue();
	}
}

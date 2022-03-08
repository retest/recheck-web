package de.retest.web.selenium;

import static de.retest.recheck.ui.Path.fromString;
import static de.retest.recheck.ui.descriptors.Element.create;
import static de.retest.web.AttributesUtil.ID;
import static de.retest.web.AttributesUtil.NAME;
import static de.retest.web.AttributesUtil.TEXT;
import static de.retest.web.selenium.TestHealer.findElement;
import static de.retest.web.selenium.TestHealer.isNotYetSupportedXPathExpression;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
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

	@ValueSource( strings = { //
			"[data-id=\"my-special ID\"]", //
			"[disabled]", //
			"div[data-id=\"my-special ID\"]", //
			"div[disabled]", //
			".myClass[data-id=\"my-special ID\"]", //
			".myClass[disabled]", //
			"#myId[data-id=\"my-special ID\"]", //
			"#myId[disabled]" } )
	@ParameterizedTest( name = "CSS Selector: {0}" )
	public void ByCssSelecto_shouldr_match_elements_with_given_attribute( final String cssSelector ) {
		configureByCssSelectorAttributeTests();

		assertThat( findElement( By.cssSelector( cssSelector ), wrapped ) ).isEqualTo( resultMarker );
	}

	@MethodSource( "attributeValueSelectors" )
	@ParameterizedTest( name = "CSS Selector: {0}" )
	public void ByCssSelector_should_match_elements_with_given_attribute_value( final String selector ) {
		configureByCssSelectorAttributeTests();

		assertThat( findElement( By.cssSelector( selector ), wrapped ) ).isEqualTo( resultMarker );
	}

	private static Stream<String> attributeValueSelectors() {
		final List<String> attributes = asList( //
				"[data-id", //
				"div[data-id", //
				".myClass[data-id", //
				"#myId[data-id" );
		final List<String> values = asList( //
				"~=ID]", //
				"|=my]", //
				"^=\"my-special\"]", //
				"$=\"special ID\"]", //
				"*=\"special\"]" );
		return attributes.stream().flatMap( attribute -> values.stream().map( value -> attribute + value ) );
	}

	private void configureByCssSelectorAttributeTests() {
		final MutableAttributes attributes = new MutableAttributes();
		attributes.put( "data-id", "my-special ID" );
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
	public void ByCssSelector_should_match_elements_with_given_class() {
		final String xpath = "HTML[1]/DIV[1]";
		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "DIV" );
		identCrit.add( new StringAttribute( "class", "pure-button my-button menu-button" ) );
		final Element element =
				create( ID, state, new IdentifyingAttributes( identCrit ), new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );
		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		assertThat( findElement( By.cssSelector( ".pure-button" ), wrapped ) ).isEqualTo( resultMarker );
		assertThat( findElement( By.cssSelector( ".pure-button.my-button" ), wrapped ) ).isEqualTo( resultMarker );
		assertThat( findElement( By.cssSelector( ".pure-button .my-button" ), wrapped ) ).isEqualTo( resultMarker );
		assertThat( findElement( By.cssSelector( ".special-class" ), wrapped ) ).isEqualTo( null );
		assertThat( findElement( By.cssSelector( ".pure-button.special-class" ), wrapped ) ).isEqualTo( null );
		assertThat( findElement( By.cssSelector( ".pure-button .special-class" ), wrapped ) ).isEqualTo( null );
	}

	@Test
	public void empty_selectors_should_not_throw_exception() {
		assertThat( findElement( By.cssSelector( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.className( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.id( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.linkText( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.name( "" ), wrapped ) ).isNull();
		assertThat( findElement( By.partialLinkText( "" ), wrapped ) ).isNull();
		// with selenium version 4, en empty tag name throws an exception
		// assertThat( findElement( By.tagName( "" ), wrapped ) ).isNull();
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
	public void ByXPathExpression_should_match_elements_with_given_xpath() {
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

	@Test
	public void testHealing_should_trigger_warning_in_result() {
		final String retestId = "id";
		final String xpath = "html[1]/div[1]";

		final Collection<Attribute> identCrit = IdentifyingAttributes.createList( fromString( xpath ), "div" );
		identCrit.add( new StringAttribute( AttributesUtil.ID, "myId" ) );

		final IdentifyingAttributes identifying = new IdentifyingAttributes( identCrit );

		final Element element = create( retestId, state, identifying, new MutableAttributes().immutable() );
		when( state.getContainedElements() ).thenReturn( Collections.singletonList( element ) );

		when( wrapped.findElement( By.xpath( xpath ) ) ).thenReturn( resultMarker );

		final List<QualifiedElementWarning> warnings = new ArrayList<>();
		when( wrapped.getWarningConsumer() ).thenReturn( warnings::add );

		TestHealer.findElement( By.id( "myId" ), wrapped ); // Do not move this from line 325, or else change the line number below

		assertThat( warnings ).hasSize( 1 );
		assertThat( warnings.get( 0 ) ).satisfies( qualifiedWarning -> {
			assertThat( qualifiedWarning.getRetestId() ).isEqualTo( retestId );
			assertThat( qualifiedWarning.getAttributeKey() ).isEqualTo( "id" );
			assertThat( qualifiedWarning.getWarning() ).satisfies( warning -> {
				assertThat( warning.getTestFileName() ).isEqualTo( TestHealerTest.class.getSimpleName() + ".java" );
				assertThat( warning.getTestLineNumber() ).isEqualTo( 325 );
				assertThat( warning.getQualifiedTestName() ).isEqualTo( TestHealerTest.class.getName() );
			} );
		} );
	}
}

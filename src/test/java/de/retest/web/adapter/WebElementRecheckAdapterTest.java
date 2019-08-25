package de.retest.web.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.RootElement;

class WebElementRecheckAdapterTest {

	WebElementRecheckAdapter cut;

	@BeforeEach
	void setUp() {
		cut = new WebElementRecheckAdapter();
	}

	@Test
	void canCheck_should_accept_both_web_element_and_wraps_element() throws Exception {
		final WebElement element = mock( WebElement.class );
		final WrapsElement wraps = mock( WrapsElement.class );
		when( wraps.getWrappedElement() ).thenReturn( element );

		assertThat( cut.canCheck( element ) ).isTrue();
		assertThat( cut.canCheck( wraps ) ).isTrue();
	}

	@Test
	void canCheck_with_multiple_wrapped_elements_should_properly_resolve() throws Exception {
		final WebElement element = mock( WebElement.class );
		final WrappedElement wrapped = mock( WrappedElement.class );
		when( wrapped.getWrappedElement() ).thenReturn( element );
		final WrapsElement wraps = mock( WrapsElement.class );
		when( wraps.getWrappedElement() ).thenReturn( wrapped );

		assertThat( cut.canCheck( wrapped ) ).isTrue();
	}

	@Test
	void convert_should_properly_convert_root() throws Exception {
		final WebElement element = createElement( "html", Collections.emptyList() );

		final Set<RootElement> convert = cut.convert( element );
		final RootElement root = new ArrayList<>( convert ).get( 0 );

		assertThat( convert ).hasSize( 1 );

		assertThat( root.getRetestId() ).isEqualTo( "text" );
		assertThat( root.getIdentifyingAttributes().getAttributes() ).hasSize( 5 );
		assertThat( getPath( root ) ).isEqualTo( "html[1]" );
		assertThat( root.getAttributes().size() ).isEqualTo( 172 );
	}

	@Test
	void convert_with_children_should_represent_structure() throws Exception {
		final WebElement meta = createElement( "meta", Collections.emptyList() );
		final WebElement div = createElement( "div", Collections.emptyList() );
		final WebElement span = createElement( "span", Collections.emptyList() );
		final WebElement button = createElement( "button", Collections.emptyList() );

		final WebElement header = createElement( "header", Arrays.asList( meta, meta ) );
		final WebElement body = createElement( "body", Arrays.asList( div, button, span, div, span ) );

		final WebElement html = createElement( "html", Arrays.asList( header, body ) );

		final RootElement chtml = new ArrayList<>( cut.convert( html ) ).get( 0 );
		final Element cheader = chtml.getContainedElements().get( 0 );
		final Element cmeta1 = cheader.getContainedElements().get( 0 );
		final Element cmeta2 = cheader.getContainedElements().get( 1 );
		final Element cbody = chtml.getContainedElements().get( 1 );
		final Element cdiv1 = cbody.getContainedElements().get( 0 );
		final Element cbutton = cbody.getContainedElements().get( 1 );
		final Element cspan1 = cbody.getContainedElements().get( 2 );
		final Element cdiv2 = cbody.getContainedElements().get( 3 );
		final Element cspan2 = cbody.getContainedElements().get( 4 );

		assertThat( getPath( chtml ) ).isEqualTo( "html[1]" );
		assertThat( getPath( cheader ) ).isEqualTo( "html[1]/header[1]" );
		assertThat( getPath( cmeta1 ) ).isEqualTo( "html[1]/header[1]/meta[1]" );
		assertThat( getPath( cmeta2 ) ).isEqualTo( "html[1]/header[1]/meta[2]" );
		assertThat( getPath( cbody ) ).isEqualTo( "html[1]/body[1]" );
		assertThat( getPath( cdiv1 ) ).isEqualTo( "html[1]/body[1]/div[1]" );
		assertThat( getPath( cbutton ) ).isEqualTo( "html[1]/body[1]/button[1]" );
		assertThat( getPath( cspan1 ) ).isEqualTo( "html[1]/body[1]/span[1]" );
		assertThat( getPath( cdiv2 ) ).isEqualTo( "html[1]/body[1]/div[2]" );
		assertThat( getPath( cspan2 ) ).isEqualTo( "html[1]/body[1]/span[2]" );
	}

	private String getPath( final Element element ) {
		return element.getIdentifyingAttributes().getPath();
	}

	private WebElement createElement( final String tag, final List<WebElement> children ) {
		final WebElement element = mock( WebElement.class );
		when( element.getAttribute( any() ) ).thenReturn( "foo" );
		when( element.getCssValue( any() ) ).thenReturn( "bar" );
		when( element.getRect() ).thenReturn( new Rectangle( 0, 0, 0, 0 ) );
		when( element.getTagName() ).thenReturn( tag );
		when( element.getText() ).thenReturn( "text" );
		when( element.findElements( any() ) ).thenReturn( children );
		return element;
	}

	private interface WrappedElement extends WebElement, WrapsElement {}
}
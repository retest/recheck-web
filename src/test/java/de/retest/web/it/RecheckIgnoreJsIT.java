package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.retest.recheck.ignore.JSFilterImpl;
import de.retest.recheck.ui.Path;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.MutableAttributes;
import de.retest.recheck.ui.diff.AttributeDifference;

class RecheckIgnoreJsIT {

	final JSFilterImpl cut = new JSFilterImpl( Paths.get( ".retest/recheck.ignore.js" ) );
	final Element element = createElement();

	private static Element createElement() {
		return Element.create( "retestId", Mockito.mock( Element.class ),
				IdentifyingAttributes.create( Path.fromString( "html[1]/div[1]" ), "div" ),
				new MutableAttributes().immutable() );
	}

	@Test
	void should_ignore_opacity_5_diff() throws Exception {
		// opacity: expected="0", actual="0.00566082"
		assertThat( cut.matches( element, new AttributeDifference( "opacity", "0", "0.00566082" ) ) ).isTrue();
		assertThat( cut.matches( element, new AttributeDifference( "opacity", "100", "80" ) ) ).isFalse();
	}

	@Test
	void should_ignore_different_base_URLs() {
		assertThat( cut.matches( element, new AttributeDifference( "background-image",
				"url(\"https://www2.test.k8s.bigcct.be/.imaging/default/dam/clients/BT_logo.svg.png/jcr:content.png\")",
				"url(\"http://icullen-website-public-spring4-8:8080/.imaging/default/dam/clients/BT_logo.svg.png/jcr:content.png\")" ) ) )
						.isTrue();
		assertThat( cut.matches( element, new AttributeDifference( "background-image",
				"url(\"https://www2.test.k8s.bigcct.be/.imaging/default/dam/clients/BT_logo.svg.png/jcr:content.png\")",
				"url(\"http://icullen-website-public-spring4-8:8080/some-other-URL.png\")" ) ) ).isFalse();
	}

	@Test
	void should_ignore_same_font_family() {
		assertThat( cut.matches( element, new AttributeDifference( "font-family", "Arial", "system-ui" ) ) ).isTrue();
		assertThat( cut.matches( element, new AttributeDifference( "font-family", "Arial", "Courier New" ) ) )
				.isFalse();
	}

	@Test
	void null_should_not_cause_exc() {
		assertThat( cut.matches( element, //
				new AttributeDifference( "some-css", null, "12px" ) ) ).isFalse();
		assertThat( cut.matches( element, //
				new AttributeDifference( "some-css", "10px", null ) ) ).isFalse();
	}

	@Test
	void element_should_not_be_filtered() throws Exception {
		assertThat( cut.matches( element ) ).isFalse();
	}

	@Test
	void missing_expected_and_actual_should_not_cause_exception() throws Exception {
		assertThat( cut.matches( element, new AttributeDifference( "key", null, null ) ) ).isFalse();
	}

	@Test
	void missing_actual_should_not_cause_exception() throws Exception {
		assertThat( cut.matches( element, new AttributeDifference( "key", "expected", null ) ) ).isFalse();
	}

	@Test
	void missing_expected_should_not_cause_exception() throws Exception {
		assertThat( cut.matches( element, new AttributeDifference( "key", null, "actual" ) ) ).isFalse();
	}
}

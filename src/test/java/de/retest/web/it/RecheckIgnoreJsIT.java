package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.retest.recheck.ignore.JSFilterImpl;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.diff.AttributeDifference;

class RecheckIgnoreJsIT {

	final JSFilterImpl cut = new JSFilterImpl( Paths.get( ".retest/recheck.ignore.js" ) );

	@Test
	void should_ignore_opacity_5_diff() throws Exception {
		final Element element = Mockito.mock( Element.class );
		// opacity: expected="0", actual="0.00566082"
		assertThat( cut.matches( element, new AttributeDifference( "opacity", "0", "0.00566082" ) ) ).isTrue();
		assertThat( cut.matches( element, new AttributeDifference( "opacity", "100", "80" ) ) ).isFalse();
	}

	@Test
	void should_ignore_different_base_URLs() {
		final Element element = Mockito.mock( Element.class );
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
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.matches( element, new AttributeDifference( "font-family", "Arial", "system-ui" ) ) ).isTrue();
		assertThat( cut.matches( element, new AttributeDifference( "font-family", "Arial", "Courier New" ) ) )
				.isFalse();
	}

	@Test
	void null_should_not_cause_exc() {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.matches( element, //
				new AttributeDifference( "some-css", null, "12px" ) ) ).isFalse();
		assertThat( cut.matches( element, //
				new AttributeDifference( "some-css", "10px", null ) ) ).isFalse();
	}
}

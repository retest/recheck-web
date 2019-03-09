package de.retest.web.it;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Rectangle;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import de.retest.recheck.ignore.JSShouldIgnoreImpl;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.diff.AttributeDifference;

public class RecheckIgnoreJsIT {

	final JSShouldIgnoreImpl cut = new JSShouldIgnoreImpl( Paths.get( ".retest/recheck.ignore.js" ) );

	@Test
	void should_ignore_outline_5_diff() throws Exception {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.shouldIgnoreAttributeDifference( element, new AttributeDifference( "outline",
				new Rectangle( 580, 610, 200, -20 ), new Rectangle( 578, 605, 203, -16 ) ) ) ).isTrue();
		assertThat( cut.shouldIgnoreAttributeDifference( element, new AttributeDifference( "outline",
				new Rectangle( 580, 610, 200, 20 ), new Rectangle( 500, 605, 200, 20 ) ) ) ).isFalse();
	}

	@Test
	void should_ignore_different_base_URLs() {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.shouldIgnoreAttributeDifference( element, new AttributeDifference( "background-image",
				"url(\"https://www2.test.k8s.bigcct.be/.imaging/default/dam/clients/BT_logo.svg.png/jcr:content.png\")",
				"url(\"http://icullen-website-public-spring4-8:8080/.imaging/default/dam/clients/BT_logo.svg.png/jcr:content.png\")" ) ) )
						.isTrue();
		assertThat( cut.shouldIgnoreAttributeDifference( element, new AttributeDifference( "background-image",
				"url(\"https://www2.test.k8s.bigcct.be/.imaging/default/dam/clients/BT_logo.svg.png/jcr:content.png\")",
				"url(\"http://icullen-website-public-spring4-8:8080/some-other-URL.png\")" ) ) ).isFalse();
	}

	@Test
	void should_ignore_same_font_family() {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.shouldIgnoreAttributeDifference( element,
				new AttributeDifference( "font-family", "Arial", "system-ui" ) ) ).isTrue();
		assertThat( cut.shouldIgnoreAttributeDifference( element,
				new AttributeDifference( "font-family", "Arial", "Courier New" ) ) ).isFalse();
	}

	@Test
	void should_ignore_any_5px_diff() {
		final Element element = Mockito.mock( Element.class );
		assertThat( cut.shouldIgnoreAttributeDifference( element, //
				new AttributeDifference( "some-css", "10px", "12px" ) ) ).isTrue();
		assertThat( cut.shouldIgnoreAttributeDifference( element, //
				new AttributeDifference( "some-css", "10px", "200px" ) ) ).isFalse();
	}
}

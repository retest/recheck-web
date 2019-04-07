package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;

class WebElementPeerTest {

	WebElementPeer cut;

	@BeforeEach
	void setUp() {
		cut = new WebElementPeer( null, null, null );
	}

	@Test
	void converted_children_should_only_contain_non_null_elements() throws Exception {
		final Element el0 = mock( Element.class );

		final WebElementPeer child0 = mock( WebElementPeer.class );
		when( child0.toElement( any() ) ).thenReturn( el0 );

		final WebElementPeer child1 = mock( WebElementPeer.class );
		when( child1.toElement( any() ) ).thenReturn( null );

		cut.addChild( child0 );
		cut.addChild( child1 );

		assertThat( cut.convertChildren( null ) ).containsExactly( el0 );
	}

	@Test
	void converted_children_should_never_be_null() throws Exception {
		assertThat( cut.convertChildren( null ) ).isEmpty();
	}

	@Test
	void retrieveStateAttributes_should_not_contain_IdentifyingAttributes() {
		final Map<String, Object> wrappedData = new HashMap<>();
		wrappedData.put( AttributesConfig.ABSOLUTE_X, "someValue" );
		wrappedData.put( AttributesConfig.ABSOLUTE_Y, "someValue" );
		wrappedData.put( AttributesConfig.ABSOLUTE_WIDTH, "someValue" );
		wrappedData.put( AttributesConfig.ABSOLUTE_HEIGHT, "someValue" );
		wrappedData.put( AttributesConfig.X, "someValue" );
		wrappedData.put( AttributesConfig.Y, "someValue" );
		wrappedData.put( AttributesConfig.WIDTH, "someValue" );
		wrappedData.put( AttributesConfig.HEIGHT, "someValue" );
		wrappedData.put( AttributesConfig.TAG_NAME, "someValue" );
		wrappedData.put( AttributesConfig.CLASS, "someValue" );
		wrappedData.put( AttributesConfig.ID, "someValue" );
		wrappedData.put( AttributesConfig.NAME, "someValue" );
		wrappedData.put( AttributesConfig.TEXT, "someValue" );
		final WebElementPeer cut =
				new WebElementPeer( new WebData( wrappedData ), "path", mock( DefaultValueFinder.class ) );
		assertThat( cut.retrieveStateAttributes( mock( IdentifyingAttributes.class ) ).get( "id" ) ).isNull();
	}
}

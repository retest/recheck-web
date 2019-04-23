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
import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;

class WebElementPeerTest {

	AttributesProvider attributesProvider;
	WebElementPeer cut;

	@BeforeEach
	void setUp() {
		attributesProvider = YamlAttributesProvider.getInstance();
		cut = new WebElementPeer( attributesProvider, null, null, null );
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
	void retrieveStateAttributes_should_not_contain_null() {
		final Map<String, Object> wrappedData = new HashMap<>();
		wrappedData.put( null, null );
		final WebElementPeer cut =
				new WebElementPeer( attributesProvider, new WebData( wrappedData ), "path",
						mock( DefaultValueFinder.class ) );

		final Attributes attributes = cut.retrieveStateAttributes( mock( IdentifyingAttributes.class ) ).immutable();

		assertThat( attributes.size() ).isEqualTo( 0 );
	}

	@Test
	void retrieveStateAttributes_should_not_contain_identifying_attributes() {
		final Map<String, Object> wrappedData = new HashMap<>();
		wrappedData.put( AttributesUtil.ABSOLUTE_X, "someValue" );
		wrappedData.put( AttributesUtil.ABSOLUTE_Y, "someValue" );
		wrappedData.put( AttributesUtil.ABSOLUTE_WIDTH, "someValue" );
		wrappedData.put( AttributesUtil.ABSOLUTE_HEIGHT, "someValue" );
		wrappedData.put( AttributesUtil.X, "someValue" );
		wrappedData.put( AttributesUtil.Y, "someValue" );
		wrappedData.put( AttributesUtil.WIDTH, "someValue" );
		wrappedData.put( AttributesUtil.HEIGHT, "someValue" );
		wrappedData.put( AttributesUtil.TEXT, "someValue" );
		wrappedData.put( AttributesUtil.CLASS, "someValue" );
		wrappedData.put( AttributesUtil.ID, "someValue" );
		wrappedData.put( AttributesUtil.NAME, "someValue" );
		wrappedData.put( AttributesUtil.TAG_NAME, "someValue" );
		final WebElementPeer cut =
				new WebElementPeer( attributesProvider, new WebData( wrappedData ), "path",
						mock( DefaultValueFinder.class ) );

		final Attributes attributes = cut.retrieveStateAttributes( mock( IdentifyingAttributes.class ) ).immutable();

		assertThat( attributes.size() ).isEqualTo( 0 );
	}

	@Test
	void retrieveStateAttributes_should_not_contain_defaults() {
		final String attributeKey = "someKey";
		final String attributeValue = "someDefaultValue";

		final Map<String, Object> wrappedData = new HashMap<>();
		wrappedData.put( attributeKey, attributeValue );

		final IdentifyingAttributes identifyingAttributes = mock( IdentifyingAttributes.class );

		final DefaultValueFinder defaultValueFinder = mock( DefaultValueFinder.class );
		when( defaultValueFinder.isDefaultValue( identifyingAttributes, attributeKey, attributeValue ) )
				.thenReturn( true );

		final WebElementPeer cut =
				new WebElementPeer( attributesProvider, new WebData( wrappedData ), "path", defaultValueFinder );

		final Attributes attributes = cut.retrieveStateAttributes( identifyingAttributes ).immutable();

		assertThat( attributes.size() ).isEqualTo( 0 );
	}

	@Test
	void retrieveStateAttributes_should_contain_non_null_non_identifying_non_default_attributes() {
		final String attributeKey = "someKey";
		final String attributeValue = "someDefaultValue";

		final Map<String, Object> wrappedData = new HashMap<>();
		wrappedData.put( attributeKey, attributeValue );

		final WebElementPeer cut =
				new WebElementPeer( attributesProvider, new WebData( wrappedData ), "path",
						mock( DefaultValueFinder.class ) );

		final Attributes attributes = cut.retrieveStateAttributes( mock( IdentifyingAttributes.class ) ).immutable();

		assertThat( attributes.get( attributeKey ) ).isEqualTo( attributeValue );
	}

}

package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.retest.ui.descriptors.Attributes;
import de.retest.ui.descriptors.MutableAttributes;
import de.retest.ui.descriptors.OutlineAttribute;

class WebElementPeerTest {

	Map<String, String> webData;
	WebElementPeer cut;

	@BeforeEach
	void setUp() {
		webData = new HashMap<>();
		cut = new WebElementPeer( webData, null );
	}

	@Test
	void null_web_data_should_result_in_null_outline() throws Exception {
		final OutlineAttribute outline = cut.retrieveOutline();
		assertThat( outline ).isNull();
	}

	@Test
	void filled_web_data_should_result_in_outline_with_corresponding_values() throws Exception {
		webData.put( AttributesConfig.X, "1" );
		webData.put( AttributesConfig.Y, "2" );
		webData.put( AttributesConfig.WIDTH, "3" );
		webData.put( AttributesConfig.HEIGHT, "4" );

		final OutlineAttribute outline = cut.retrieveOutline();

		final Rectangle rectangle = outline.getValue();
		assertThat( rectangle.getX() ).isEqualTo( 1.0 );
		assertThat( rectangle.getY() ).isEqualTo( 2.0 );
		assertThat( rectangle.getWidth() ).isEqualTo( 3.0 );
		assertThat( rectangle.getHeight() ).isEqualTo( 4.0 );
	}

	@Test
	void state_attributes_should_consist_of_non_null_and_non_default_textual_and_numerical_attributes()
			throws Exception {
		// textual (in)
		final String name0 = "animation";
		final String criterion0 = "textual value";
		webData.put( name0, criterion0 );
		// numerical (in)
		final String name1 = "border-radius";
		final String criterion1 = "numerical value";
		webData.put( name1, criterion1 );
		// textual but null (out)
		final String name2 = "background";
		final String criterion2 = null;
		webData.put( name2, criterion2 );
		// numerical but default (out)
		final String name3 = "border-width";
		final String criterion3 = "0px";
		webData.put( name3, criterion3 );
		// identifying (out)
		final String name4 = "class";
		final String criterion4 = "identifying attribute";
		webData.put( name4, criterion4 );

		final MutableAttributes stateAttributes = cut.retrieveStateAttributes();
		final Attributes immutableStateAttributes = stateAttributes.immutable();
		assertThat( immutableStateAttributes.size() ).isEqualTo( 2 );
		assertThat( immutableStateAttributes.get( name0 ) ).isEqualTo( criterion0 );
		assertThat( immutableStateAttributes.get( name1 ) ).isEqualTo( criterion1 );
	}

}

package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.retest.ui.descriptors.OutlineAttribute;

class WebElementPeerTest {

	Map<String, Object> webData;
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
	void normalize_should_remove_apostrophe() {
		assertThat( WebElementPeer.normalize( null ) ).isEqualTo( null );
		assertThat( WebElementPeer.normalize( "\"Times New Roman\"" ) ).isEqualTo( "Times New Roman" );
		assertThat( WebElementPeer.normalize( "\" Times New Roman \"" ) ).isEqualTo( "Times New Roman" );
	}
}

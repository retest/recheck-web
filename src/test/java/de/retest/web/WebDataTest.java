package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class WebDataTest {

	@Test
	void null_web_data_should_result_in_null_outline() throws Exception {
		final WebData webData = new WebData( new HashMap<>() );
		final Rectangle outline = webData.getOutline();
		assertThat( outline ).isNull();
	}

	@Test
	void filled_web_data_should_result_in_outline_with_corresponding_values() throws Exception {
		final Map<String, Object> input = new HashMap<>();
		input.put( AttributesConfig.X, "1" );
		input.put( AttributesConfig.Y, "2" );
		input.put( AttributesConfig.WIDTH, "3" );
		input.put( AttributesConfig.HEIGHT, "4" );

		final Rectangle rectangle = new WebData( input ).getOutline();
		assertThat( rectangle.getX() ).isEqualTo( 1.0 );
		assertThat( rectangle.getY() ).isEqualTo( 2.0 );
		assertThat( rectangle.getWidth() ).isEqualTo( 3.0 );
		assertThat( rectangle.getHeight() ).isEqualTo( 4.0 );
	}

	@Test
	void normalize_should_trim() {
		assertThat( WebData.normalize( "\" Times New Roman \"" ) ).isEqualTo( "Times New Roman" );
	}

	@Test
	void normalize_should_be_null_safe() {
		assertThat( WebData.normalize( null ) ).isEqualTo( null );
	}

	@Test
	void normalize_should_remove_apostrophe() {
		assertThat( WebData.normalize( "\"Times New Roman\"" ) ).isEqualTo( "Times New Roman" );
	}

	@Test
	void normalize_should_remove_comma() {
		// e.g. the clip attribute comes in flavors
		assertThat( WebData.normalize( "rect(0px, 0px, 0px, 0px)" ) ).isEqualTo( "rect(0px 0px 0px 0px)" );
		assertThat( WebData.normalize( "rect(0px,0px,0px,0px)" ) ).isEqualTo( "rect(0px 0px 0px 0px)" );
	}
}

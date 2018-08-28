package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
	void normalize_should_remove_quotes_and_trim() {
		assertThat( WebData.normalize( null ) ).isEqualTo( null );
		assertThat( WebData.normalize( "\"Times New Roman\"" ) ).isEqualTo( "Times New Roman" );
		assertThat( WebData.normalize( "\" Times New Roman \"" ) ).isEqualTo( "Times New Roman" );
	}

	@Test
	void int_conversion_should_throw_an_exception_for_unknown_types() throws Exception {
		final WebData cut = new WebData( new HashMap<>() );
		assertThatThrownBy( () -> cut.getAsInt( "foo" ) ) //
				.isInstanceOf( ConversionException.class ) //
				.hasMessage( "Don't know how to convert null of null to int!" );
	}

	@ParameterizedTest
	@MethodSource( "data" )
	void int_conversion_should_handle_various_types( final WebData cut, final String key, final int expected )
			throws Exception {
		assertThat( cut.getAsInt( key ) ).isEqualTo( expected );
	}

	static Stream<Arguments> data() {
		final String key = "foo";

		final Integer value0 = 0;
		final Map<String, Object> wrappedData0 = new HashMap<>();
		wrappedData0.put( key, value0 );

		final String value1 = "1";
		final Map<String, Object> wrappedData1 = new HashMap<>();
		wrappedData1.put( key, value1 );

		final Double value2 = 2.3;
		final Map<String, Object> wrappedData2 = new HashMap<>();
		wrappedData2.put( key, value2 );

		final Long value3 = 3L;
		final Map<String, Object> wrappedData3 = new HashMap<>();
		wrappedData3.put( key, value3 );

		return Stream.of( //
				Arguments.of( new WebData( wrappedData0 ), key, 0 ), //
				Arguments.of( new WebData( wrappedData1 ), key, 1 ), //
				Arguments.of( new WebData( wrappedData2 ), key, 2 ), //
				Arguments.of( new WebData( wrappedData3 ), key, 3 ) );
	}

}

package de.retest.web.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.retest.web.AttributesUtil;
import de.retest.web.ConversionException;
import de.retest.web.mapping.WebData;

class WebDataTest {

	WebData cut;

	@BeforeEach
	void setUp() {
		cut = new WebData( new HashMap<>() );
	}

	@Test
	void null_web_data_should_result_in_null_outline() throws Exception {
		assertThat( cut.getOutline() ).isNull();
	}

	@Test
	void filled_web_data_should_result_in_outline_with_corresponding_values() throws Exception {
		final Map<String, Object> input = new HashMap<>();
		input.put( AttributesUtil.X, "1" );
		input.put( AttributesUtil.Y, "2" );
		input.put( AttributesUtil.WIDTH, "3" );
		input.put( AttributesUtil.HEIGHT, "4" );

		final Rectangle outline = new WebData( input ).getOutline();
		assertThat( outline.getX() ).isEqualTo( 1.0 );
		assertThat( outline.getY() ).isEqualTo( 2.0 );
		assertThat( outline.getWidth() ).isEqualTo( 3.0 );
		assertThat( outline.getHeight() ).isEqualTo( 4.0 );
	}

	@Test
	void normalize_should_remove_quotes_and_trim() {
		assertThat( WebData.normalize( null ) ).isEqualTo( null );
		assertThat( WebData.normalize( "\"Times New Roman\"" ) ).isEqualTo( "Times New Roman" );
		assertThat( WebData.normalize( "\" Times New Roman \"" ) ).isEqualTo( "Times New Roman" );
	}

	@Test
	void int_conversion_should_throw_an_exception_for_unknown_types() throws Exception {
		assertThatThrownBy( () -> cut.getAsInt( "foo" ) ) //
				.isInstanceOf( ConversionException.class ) //
				.hasMessage( "Don't know how to convert null of null to int!" );
	}

	@Test
	void isShown_should_be_true_when_rectangle_is_visible() {
		final Map<String, Object> wrappedData = getWrappedDataWithRectangle( true );
		assertThat( new WebData( wrappedData ).isShown() ).isTrue();
	}

	@Test
	void isShown_should_be_true_when_shown_and_rectangle_is_visible() throws Exception {
		final Map<String, Object> wrappedData = getWrappedDataWithRectangle( true );
		wrappedData.put( "shown", true );
		assertThat( new WebData( wrappedData ).isShown() ).isTrue();
	}

	@Test
	void isShown_should_be_true_if_shown_and_the_outline_is_not_visible() {
		final Map<String, Object> wrappedData = getWrappedDataWithRectangle( false );
		wrappedData.put( "shown", true );

		assertThat( new WebData( wrappedData ).isShown() ).isTrue();
	}

	@Test
	void isShown_should_be_false_if_not_shown_and_the_outline_is_not_visible() {
		final Map<String, Object> wrappedData = getWrappedDataWithRectangle( false );
		wrappedData.put( "shown", false );

		assertThat( new WebData( wrappedData ).isShown() ).isFalse();
	}

	@Test
	void getAsString_should_return_normalized_string() {
		final Map<String, Object> wrappedData = getWrappedDataWithRectangle( true );
		final String key = "key";
		final String text = "some text";
		wrappedData.put( key, "\"  " + text + "  \"" );
		final String result = new WebData( wrappedData ).getAsString( key );
		assertThat( result ).isEqualTo( text );
	}

	@Test
	void getAsString_should_return_not_normalized_string() {
		final Map<String, Object> wrappedData = getWrappedDataWithRectangle( true );
		final String key = "text";
		final String text = "\"  some text  \"";
		wrappedData.put( key, text );
		final String result = new WebData( wrappedData ).getAsString( key );
		assertThat( result ).isEqualTo( text );
	}

	private Map<String, Object> getWrappedDataWithRectangle( final boolean isVisible ) {
		final int size = isVisible ? 1 : 0;
		final Map<String, Object> wrappedData = new HashMap<>();
		wrappedData.put( AttributesUtil.X, size );
		wrappedData.put( AttributesUtil.Y, size );
		wrappedData.put( AttributesUtil.WIDTH, size );
		wrappedData.put( AttributesUtil.HEIGHT, size );
		return wrappedData;
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

		final Double value2 = 2.4;
		final Map<String, Object> wrappedData2 = new HashMap<>();
		wrappedData2.put( key, value2 );

		final Double value3 = 2.5;
		final Map<String, Object> wrappedData3 = new HashMap<>();
		wrappedData3.put( key, value3 );

		final Long value4 = 4L;
		final Map<String, Object> wrappedData4 = new HashMap<>();
		wrappedData4.put( key, value4 );

		return Stream.of( //
				Arguments.of( new WebData( wrappedData0 ), key, 0 ), //
				Arguments.of( new WebData( wrappedData1 ), key, 1 ), //
				Arguments.of( new WebData( wrappedData2 ), key, 2 ), //
				Arguments.of( new WebData( wrappedData3 ), key, 3 ), //
				Arguments.of( new WebData( wrappedData4 ), key, 4 ) );
	}

}

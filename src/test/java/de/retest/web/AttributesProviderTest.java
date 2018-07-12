package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttributesProviderTest {

	@BeforeEach
	void setUp() throws Exception {
		final Field instance = AttributesProvider.class.getDeclaredField( "instance" );
		instance.setAccessible( true );
		instance.set( null, null );
	}

	@Test
	void joined_attributes_should_equal_concated_attributes() throws Exception {
		final AttributesProvider cut = AttributesProvider.getInstance();
		final List<String> concatedAttributes =
				Stream.of( cut.getTextualAttributes(), cut.getNumericalAttributes(), cut.getIdentifyingAttributes() ) //
						.flatMap( Collection::stream ) //
						.collect( Collectors.toList() );
		assertThat( cut.getJoinedAttributes() ).containsExactlyElementsOf( concatedAttributes );
	}

	@Test
	void invalid_attributes_file_should_yield_RuntimeException() throws Exception {
		final String attributesFile = "foo";
		System.setProperty( AttributesProvider.ATTRIBUTES_FILE_PROPERTY, attributesFile );
		assertThatThrownBy( AttributesProvider::getInstance ) //
				.isInstanceOf( RuntimeException.class ) //
				.hasMessage( "Cannot read attributes file '" + attributesFile + "'." );
	}

	@AfterEach
	void tearDown() {
		System.clearProperty( AttributesProvider.ATTRIBUTES_FILE_PROPERTY );
	}

}

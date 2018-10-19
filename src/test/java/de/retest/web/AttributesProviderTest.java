package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ_WRITE;
import static org.junit.jupiter.api.parallel.Resources.SYSTEM_PROPERTIES;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

class AttributesProviderTest {

	@Test
	void joined_attributes_should_equal_concated_attributes() throws Exception {
		final AttributesProvider cut = AttributesProvider.getTestInstance();
		final List<String> concatedAttributes =
				Stream.concat( cut.getIdentifyingAttributes().stream(), cut.getAttributes().stream() )
						.collect( Collectors.toList() );
		assertThat( cut.getJoinedAttributes() ).containsExactlyElementsOf( concatedAttributes );
	}

	@Test
	@ResourceLock( value = SYSTEM_PROPERTIES, mode = READ_WRITE )
	void invalid_attributes_file_should_yield_UncheckedIOException() throws Exception {
		final String attributesFile = "foo";
		System.setProperty( AttributesProvider.ATTRIBUTES_FILE_PROPERTY, attributesFile );
		assertThatThrownBy( AttributesProvider::getTestInstance ) //
				.isExactlyInstanceOf( UncheckedIOException.class ) //
				.hasMessage( "Cannot read attributes file '" + attributesFile + "'." );
		System.clearProperty( AttributesProvider.ATTRIBUTES_FILE_PROPERTY );
	}

}

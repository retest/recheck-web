package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ_WRITE;
import static org.junit.jupiter.api.parallel.Resources.SYSTEM_PROPERTIES;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import de.retest.web.testutils.SystemProperty;

class AttributesProviderTest {

	@Test
	@ResourceLock( value = SYSTEM_PROPERTIES, mode = READ )
	void joined_attributes_should_equal_concated_attributes() throws Exception {
		final AttributesProvider cut = AttributesProvider.getTestInstance();
		final List<String> concatedAttributes =
				Stream.concat( cut.getIdentifyingAttributes().stream(), cut.getAttributes().stream() )
						.collect( Collectors.toList() );
		assertThat( cut.getJoinedAttributes() ).containsExactlyElementsOf( concatedAttributes );
	}

	@Test
	@ResourceLock( value = SYSTEM_PROPERTIES, mode = READ_WRITE )
	@SystemProperty( key = AttributesProvider.ATTRIBUTES_FILE_PROPERTY, value = "foo" )
	void invalid_attributes_file_should_yield_UncheckedIOException() throws Exception {
		assertThatThrownBy( AttributesProvider::getTestInstance ) //
				.isExactlyInstanceOf( UncheckedIOException.class ) //
				.hasMessage( "Cannot read attributes file 'foo'." );
	}

}

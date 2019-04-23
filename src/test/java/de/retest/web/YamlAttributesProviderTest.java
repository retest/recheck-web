package de.retest.web;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ_WRITE;
import static org.junit.jupiter.api.parallel.Resources.SYSTEM_PROPERTIES;

import java.io.UncheckedIOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import de.retest.web.testutils.SystemProperty;

class YamlAttributesProviderTest {

	@Test
	@ResourceLock( value = SYSTEM_PROPERTIES, mode = READ_WRITE )
	@SystemProperty( key = YamlAttributesProvider.ATTRIBUTES_FILE_PROPERTY, value = "foo" )
	void invalid_attributes_file_should_yield_UncheckedIOException() throws Exception {
		assertThatThrownBy( YamlAttributesProvider::getTestInstance ) //
				.isExactlyInstanceOf( UncheckedIOException.class ) //
				.hasMessage( "Cannot read attributes file 'foo'." );
	}

}

package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class AttributesProviderTest {

	@Test
	void joined_attributes_should_equal_concated_attributes() throws Exception {
		final AttributesProvider cut = AttributesProvider.getInstance();
		final List<String> concatedAttributes =
				Stream.concat( cut.getIdentifyingAttributes().stream(), cut.getAttributes().stream() )
						.collect( Collectors.toList() );
		assertThat( cut.getJoinedAttributes() ).containsExactlyElementsOf( concatedAttributes );
	}

}

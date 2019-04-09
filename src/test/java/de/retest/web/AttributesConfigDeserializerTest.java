package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

class AttributesConfigDeserializerTest {

	File baseDir = new File( "src/test/resources/de/retest/web/AttributesConfigDeserializerTest/" );
	ObjectMapper mapper = new ObjectMapper( new YAMLFactory() );
	AttributesConfigDeserializer cut = new AttributesConfigDeserializer();

	@Test
	void should_handle_attributes_selected() throws Exception {
		final AttributesConfig attributesConfig = deserialize( new File( baseDir, "selected.yaml" ) );
		assertThat( attributesConfig.getCssAttributes() ).contains( "foo", "bar", "baz" );
		assertThat( attributesConfig.getHtmlAttributes() ).contains( "tic", "tac", "toe" );
	}

	@Test
	void should_handle_attributes_empty() throws Exception {
		final AttributesConfig attributesConfig = deserialize( new File( baseDir, "empty.yaml" ) );
		assertThat( attributesConfig.getCssAttributes() ).isEmpty();
		assertThat( attributesConfig.getHtmlAttributes() ).isEmpty();
	}

	@Test
	void should_handle_html_attributes_all() throws Exception {
		final AttributesConfig attributesConfig = deserialize( new File( baseDir, "all-html.yaml" ) );
		assertThat( attributesConfig.getHtmlAttributes() ).isNull();
	}

	@Test
	void should_handle_css_attributes_all() throws Exception {
		assertThatThrownBy( () -> deserialize( new File( baseDir, "all-css.yaml" ) ) ) //
				.isExactlyInstanceOf( IllegalArgumentException.class ) //
				.hasMessage(
						"CSS attributes can only be a set of selected attributes or empty ('all' not supported)." );
	}

	private AttributesConfig deserialize( final File yaml ) throws Exception {
		final JsonParser parser = mapper.getFactory().createParser( yaml );
		final DeserializationContext context = mapper.getDeserializationContext();
		return cut.deserialize( parser, context );
	}

}

package de.retest.web.mapping.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class XPathGeneratorTest {

	@Test
	void next_should_produce_proper_path() throws Exception {
		final XPathGenerator cut = new XPathGenerator();

		assertThat( cut.next( "foo" ).getPath() ).isEqualTo( "/foo[1]" );
		assertThat( cut.next( "bar" ).getPath() ).isEqualTo( "/bar[1]" );
		assertThat( cut.next( "foo" ).getPath() ).isEqualTo( "/foo[2]" );
		assertThat( cut.next( "foo" ).getPath() ).isEqualTo( "/foo[3]" );
		assertThat( cut.next( "bar" ).getPath() ).isEqualTo( "/bar[2]" );
	}

	@Test
	void next_should_respect_root_path() throws Exception {
		final XPathGenerator cut = new XPathGenerator( "." );

		assertThat( cut.next( "foo" ).getPath() ).isEqualTo( "./foo[1]" );
		assertThat( cut.next( "bar" ).getPath() ).isEqualTo( "./bar[1]" );
		assertThat( cut.next( "foo" ).getPath() ).isEqualTo( "./foo[2]" );
		assertThat( cut.next( "foo" ).getPath() ).isEqualTo( "./foo[3]" );
		assertThat( cut.next( "bar" ).getPath() ).isEqualTo( "./bar[2]" );
	}

	@Test
	void chaining_should_save_proper_path() throws Exception {
		final XPathGenerator html = new XPathGenerator().next( "html" );
		final XPathGenerator header = html.next( "header" );
		final XPathGenerator body = html.next( "body" );

		assertThat( header.next( "meta" ).getPath() ).isEqualTo( "/html[1]/header[1]/meta[1]" );
		assertThat( header.next( "meta" ).getPath() ).isEqualTo( "/html[1]/header[1]/meta[2]" );
		assertThat( body.next( "div" ).getPath() ).isEqualTo( "/html[1]/body[1]/div[1]" );
		assertThat( body.next( "button" ).getPath() ).isEqualTo( "/html[1]/body[1]/button[1]" );
		assertThat( body.next( "span" ).getPath() ).isEqualTo( "/html[1]/body[1]/span[1]" );
		assertThat( body.next( "div" ).getPath() ).isEqualTo( "/html[1]/body[1]/div[2]" );
		assertThat( body.next( "span" ).getPath() ).isEqualTo( "/html[1]/body[1]/span[2]" );
	}
}
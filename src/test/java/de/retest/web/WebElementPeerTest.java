package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.retest.ui.descriptors.Element;

class WebElementPeerTest {

	WebElementPeer cut;

	@BeforeEach
	void setUp() {
		cut = new WebElementPeer( null, null );
	}

	@Test
	void converted_children_should_only_contain_non_null_elements() throws Exception {
		final Element el0 = mock( Element.class );
		final WebElementPeer child0 = mock( WebElementPeer.class );
		when( child0.toElement() ).thenReturn( el0 );

		final WebElementPeer child1 = mock( WebElementPeer.class );
		when( child1.toElement() ).thenReturn( null );

		cut.addChild( child0 );
		cut.addChild( child1 );

		assertThat( cut.convertChildren() ).containsExactly( el0 );
	}

	@Test
	void converted_children_should_never_be_null() throws Exception {
		assertThat( cut.convertChildren() ).isEmpty();
	}

}

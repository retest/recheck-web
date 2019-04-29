package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.retest.recheck.ui.descriptors.Element;

class ByTest {

	@Test
	void findElements_should_return_matching_children() {
		final Element matching = mock( Element.class );
		when( matching.toString() ).thenReturn( "matching" );
		final Element nonMatching = mock( Element.class );
		final List<Element> children = new ArrayList<>();
		children.add( matching );
		children.add( nonMatching );

		assertThat( By.findElements( children, child -> child.toString().equals( "matching" ) ) )
				.containsExactly( matching );
	}

}

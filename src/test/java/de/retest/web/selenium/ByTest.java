package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.RootElement;

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

	@Test
	void too_low_similarity_should_throw_exc() {
		final Element oldElement = mock( Element.class );
		when( oldElement.getIdentifyingAttributes() ).thenReturn( mock( IdentifyingAttributes.class ) );
		when( oldElement.getRetestId() ).thenReturn( "retestId" );
		final RootElement lastExpected = mock( RootElement.class );
		when( lastExpected.getContainedElements() ).thenReturn( Collections.singletonList( oldElement ) );
		when( lastExpected.getIdentifyingAttributes() ).thenReturn( mock( IdentifyingAttributes.class ) );

		final Element newElement = mock( Element.class );
		when( newElement.getIdentifyingAttributes() ).thenReturn( mock( IdentifyingAttributes.class ) );
		final RootElement lastActual = mock( RootElement.class );
		when( lastActual.getContainedElements() ).thenReturn( Collections.singletonList( newElement ) );
		when( lastActual.getIdentifyingAttributes() ).thenReturn( mock( IdentifyingAttributes.class ) );

		assertThatThrownBy(
				() -> By.findElement( lastExpected, lastActual, child -> child.getRetestId().equals( "retestId" ) ) ) //
						.isInstanceOf( NoElementWithHighEnoughMatchFoundException.class );
	}

}

package de.retest.web.selenium;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

import de.retest.recheck.ui.descriptors.Attributes;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.RootElement;

class ByBestMatchToRetestIdTest {

	@Test
	void ctor_should_throw_illegal_argument_exception_when_retest_id_is_empty() {
		assertThatThrownBy( () -> new ByBestMatchToRetestId( "" ) ) //
				.isInstanceOf( IllegalArgumentException.class ) //
				.hasMessage( "retestId must not be empty." );
	}

	@Test
	void find_elements_should_throw_no_element_with_retest_id_found_exception_when_result_is_null() {
		final RootElement rootElement = mock( RootElement.class );
		assertThatThrownBy( () -> new ByBestMatchToRetestId( "foo" ).findElement( rootElement, rootElement ) )
				.isInstanceOf( NoElementWithReTestIdFoundException.class ) //
				.hasMessage( "No element with retest ID 'foo' found!" );
	}

	@Test
	void find_elements_should_return_an_equal_element_when_found() {
		final IdentifyingAttributes identifyingAtt = mock( IdentifyingAttributes.class );
		final Attributes attributes = mock( Attributes.class );

		final RootElement lastExpected = new RootElement( "bar", identifyingAtt, attributes, null, null, 0, null );
		final RootElement lastActual = new RootElement( "baz", identifyingAtt, attributes, null, null, 0, null );
		final Element element = Element.create( "foo", mock( Element.class ), identifyingAtt, attributes );
		lastExpected.addChildren( element );
		lastActual.addChildren( element );
		final ByBestMatchToRetestId cut = new ByBestMatchToRetestId( "foo" );
		assertThat( cut.findElement( lastExpected, lastActual ) ).isEqualTo( element );
	}

	@Test
	void hash_code_should_return_same_hash_codes_for_equal_objects() {
		final ByBestMatchToRetestId cut = new ByBestMatchToRetestId( "foo" );
		final ByBestMatchToRetestId other = new ByBestMatchToRetestId( "foo" );
		assertThat( cut ).hasSameHashCodeAs( other );
	}

	@Test
	void hash_code_should_return_different_hash_codes_for_different_objects() {
		final ByBestMatchToRetestId cut = new ByBestMatchToRetestId( "foo" );
		final ByBestMatchToRetestId other = new ByBestMatchToRetestId( "bar" );
		assertThat( cut.hashCode() ).isNotEqualTo( other.hashCode() );
	}

	@Test
	void equals_should_return_false_when_the_object_is_null() {
		final ByBestMatchToRetestId cut = new ByBestMatchToRetestId( "foo" );
		assertThat( cut ).isNotEqualTo( null );
	}

	@Test
	void equals_should_return_true_when_the_objects_are_same() {
		final ByBestMatchToRetestId cut = new ByBestMatchToRetestId( "foo" );
		assertThat( cut ).isEqualTo( cut );
	}

	@Test
	void equals_should_return_true_if_the_retest_ids_are_equal() {
		final ByBestMatchToRetestId cut = new ByBestMatchToRetestId( "foo" );
		final ByBestMatchToRetestId other = new ByBestMatchToRetestId( "foo" );
		assertThat( cut ).isEqualTo( other );
	}

	@Test
	void equals_should_return_false_if_the_retest_ids_are_different() {
		final ByBestMatchToRetestId cut = new ByBestMatchToRetestId( "foo" );
		final ByBestMatchToRetestId other = new ByBestMatchToRetestId( "bar" );
		assertThat( cut ).isNotEqualTo( other );
	}

	@Test
	void equals_should_return_false_if_the_classes_are_different() {
		final ByBestMatchToRetestId cut = new ByBestMatchToRetestId( "foo" );
		assertThat( cut ).isNotEqualTo( new Object() );
	}

}

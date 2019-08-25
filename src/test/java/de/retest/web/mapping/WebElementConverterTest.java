package de.retest.web.mapping;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.Rectangle;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.Element;
import de.retest.recheck.ui.descriptors.IdentifyingAttributes;
import de.retest.recheck.ui.descriptors.idproviders.RetestIdProvider;
import de.retest.web.AttributesProvider;
import de.retest.web.YamlAttributesProvider;

class WebElementConverterTest {

	@Test
	void convert_should_extract_all_attributes() throws Exception {
		final RetestIdProvider retestIdProvider = mock( RetestIdProvider.class );
		when( retestIdProvider.getRetestId( any() ) ).thenReturn( "42" );
		final AttributesProvider attributesProvider = YamlAttributesProvider.getInstance();
		final DefaultValueFinder defaults = mock( DefaultValueFinder.class );

		final WebDataProvider provider = mock( WebDataProvider.class );
		when( provider.getPath() ).thenReturn( "/html[1]" );
		when( provider.getAbsoluteOutline() ).thenReturn( mock( Rectangle.class ) );
		when( provider.getOutline() ).thenReturn( mock( Rectangle.class ) );
		when( provider.getText() ).thenReturn( "text" );
		when( provider.getTag() ).thenReturn( "html" );
		when( provider.getHTMLAttributes() ).thenAnswer( __ -> Stream.of( //
				Pair.of( "id", "page" ), //
				Pair.of( "class", "foo" ), //
				Pair.of( "name", "bar" ) //
		) );
		when( provider.getCSSAttributes() ).thenReturn( Stream.empty() );

		final WebElementConverter cut = new WebElementConverter( retestIdProvider, attributesProvider, defaults );

		final Element element = cut.convert( mock( Element.class ), provider );
		final IdentifyingAttributes id = element.getIdentifyingAttributes();
		assertThat( element.getRetestId() ).isEqualTo( "42" );
		assertThat( id.getPath() ).isEqualTo( "html[1]" );
		assertThat( id.getOutlineRectangle() ).isEqualTo( new Rectangle( 0, 0, 0, 0 ) );
		assertThat( id.getType() ).isEqualTo( "html" );
		assertThat( id.getAttribute( "id" ).getValue() ).isEqualTo( "page" );
		assertThat( id.getAttribute( "class" ).getValue() ).isEqualTo( "foo" );
		assertThat( id.getAttribute( "name" ).getValue() ).isEqualTo( "bar" );
	}
}
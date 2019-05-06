package de.retest.web;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import de.retest.recheck.ui.DefaultValueFinder;
import de.retest.recheck.ui.descriptors.RetestIdProviderUtil;
import de.retest.recheck.ui.descriptors.RootElement;
import de.retest.recheck.ui.diff.RootElementDifference;
import de.retest.recheck.ui.diff.RootElementDifferenceFinder;
import de.retest.web.mapping.PathsToWebDataMapping;

class PeerConverterTest {

	@Test
	void getParentPath_should_return_null_for_toplevel() {
		assertThat( PeerConverter.getParentPath( "//HTML[1]" ) ).isNull();
	}

	@Test
	void getParentPath_should_return_parent_path() {
		assertThat( PeerConverter.getParentPath( "//HTML[1]/BODY[1]" ) ).isEqualTo( "//HTML[1]" );
	}

	@Test
	void convertToPeers_should_result_in_valid_tree() throws Exception {
		final Map<String, Map<String, Object>> data = new HashMap<>();
		data.put( "//HTML[1]/BODY[1]", toHashMap( "BODY" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]", toHashMap( "DIV" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]", toHashMap( "DIV" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[1]", toHashMap( "LI" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[1]/A[1]", toHashMap( "A" ) );
		data.put( "//HTML[1]", toHashMap( "HTML" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]", toHashMap( "FOOTER" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[2]", toHashMap( "LI" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[2]/A[1]", toHashMap( "A" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[3]", toHashMap( "LI" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]", toHashMap( "UL" ) );
		data.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[3]/A[1]", toHashMap( "A" ) );
		final PathsToWebDataMapping mapping = new PathsToWebDataMapping( data );

		final DefaultValueFinder defaultValueFinder = ( identifyingAttributes, attributeKey, attributeValue ) -> false;
		final PeerConverter cut = new PeerConverter( RetestIdProviderUtil.getConfiguredRetestIdProvider(),
				YamlAttributesProvider.getInstance(), mapping, "title", null, defaultValueFinder );
		final RootElement root = cut.convertToPeers();

		final RootElementDifferenceFinder diffFinder = new RootElementDifferenceFinder( defaultValueFinder );
		final List<RootElementDifference> diffs =
				diffFinder.findDifferences( singletonList( root ), singletonList( root ) );
		assertThat( diffs ).isEmpty();
	}

	private Map<String, Object> toHashMap( final String tagName ) {
		final Map<String, Object> result = new HashMap<>();
		result.put( "tagName", tagName );
		result.put( "x", "10" );
		result.put( "y", "10" );
		result.put( "height", "100" );
		result.put( "width", "100" );
		return result;
	}
}

package de.retest.web;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.retest.elementcollection.ElementCollection;
import de.retest.elementcollection.IgnoredComponents;
import de.retest.ui.descriptors.RootElement;
import de.retest.ui.diff.RootElementDifference;
import de.retest.ui.diff.RootElementDifferenceFinder;

class RecheckSeleniumAdapterTest {

	@BeforeEach
	void setUp() {
		IgnoredComponents.getTestInstance( new ElementCollection() );
	}

	@Test
	void getParentPath_should_return_null_for_toplevel() {
		assertThat( RecheckSeleniumAdapter.getParentPath( "//HTML[1]" ) ).isNull();
	}

	@Test
	void getParentPath_should_return_parent_path() {
		assertThat( RecheckSeleniumAdapter.getParentPath( "//HTML[1]/BODY[1]" ) ).isEqualTo( "//HTML[1]" );
	}

	@Test
	void convertToPeers_should_result_in_valid_tree() throws Exception {
		final Map<String, Map<String, String>> input = new HashMap<>();
		input.put( "//HTML[1]/BODY[1]", toHashMap( "BODY" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]", toHashMap( "DIV" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]", toHashMap( "DIV" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[1]", toHashMap( "LI" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[1]/A[1]", toHashMap( "A" ) );
		input.put( "//HTML[1]", toHashMap( "HTML" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]", toHashMap( "FOOTER" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[2]", toHashMap( "LI" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[2]/A[1]", toHashMap( "A" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[3]", toHashMap( "LI" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]", toHashMap( "UL" ) );
		input.put( "//HTML[1]/BODY[1]/FOOTER[1]/DIV[1]/DIV[1]/UL[1]/LI[3]/A[1]", toHashMap( "A" ) );
		final RecheckSeleniumAdapter adapter = new RecheckSeleniumAdapter();
		final RootElement root = adapter.convertToPeers( input, "title", null );

		final RootElementDifferenceFinder diffFinder =
				new RootElementDifferenceFinder( adapter.getDefaultValueFinder() );
		final List<RootElementDifference> diffs =
				diffFinder.findDifferences( singletonList( root ), singletonList( root ) );
		assertThat( diffs ).isEmpty();
	}

	private Map<String, String> toHashMap( final String tagName ) {
		final Map<String, String> result = new HashMap<>();
		result.put( "tagName", tagName );
		return result;
	}

}

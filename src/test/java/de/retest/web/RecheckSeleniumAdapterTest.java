package de.retest.web;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ;
import static org.junit.jupiter.api.parallel.ResourceAccessMode.READ_WRITE;
import static org.junit.jupiter.api.parallel.Resources.SYSTEM_PROPERTIES;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import de.retest.elementcollection.ElementCollection;
import de.retest.elementcollection.RecheckIgnore;
import de.retest.ui.descriptors.RootElement;
import de.retest.ui.diff.RootElementDifference;
import de.retest.ui.diff.RootElementDifferenceFinder;

class RecheckSeleniumAdapterTest {

	@BeforeEach
	void setUp() {
		RecheckIgnore.getTestInstance( new ElementCollection() );
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
	@ResourceLock( value = SYSTEM_PROPERTIES, mode = READ )
	void convertToPeers_should_result_in_valid_tree() throws Exception {
		final Map<String, Map<String, Object>> input = new HashMap<>();
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
		final RecheckSeleniumAdapter cut = new RecheckSeleniumAdapter();
		final RootElement root = cut.convertToPeers( input, "title", null );

		final RootElementDifferenceFinder diffFinder = new RootElementDifferenceFinder( cut.getDefaultValueFinder() );
		final List<RootElementDifference> diffs =
				diffFinder.findDifferences( singletonList( root ), singletonList( root ) );
		assertThat( diffs ).isEmpty();
	}

	@Test
	@ResourceLock( value = SYSTEM_PROPERTIES, mode = READ_WRITE )
	void outline_should_be_added_to_ignored_attributes_if_property_is_not_null() {
		final Properties backup = new Properties();
		backup.putAll( System.getProperties() );

		System.setProperty( RecheckIgnore.IGNORED_ATTRIBUTES_PROPERTY, "foo" );
		new RecheckSeleniumAdapter();
		assertThat( System.getProperty( RecheckIgnore.IGNORED_ATTRIBUTES_PROPERTY ) ).isEqualTo( "foo;outline" );

		System.setProperties( backup );
	}

	@Test
	@ResourceLock( value = SYSTEM_PROPERTIES, mode = READ )
	void outline_should_be_the_only_ignored_attribute_if_property_is_null() {
		assertThat( System.getProperty( RecheckIgnore.IGNORED_ATTRIBUTES_PROPERTY ) ).isNull();
		new RecheckSeleniumAdapter();
		assertThat( System.getProperty( RecheckIgnore.IGNORED_ATTRIBUTES_PROPERTY ) ).isEqualTo( "outline" );
	}

	private Map<String, Object> toHashMap( final String tagName ) {
		final Map<String, Object> result = new HashMap<>();
		result.put( "tagName", tagName );
		return result;
	}

}

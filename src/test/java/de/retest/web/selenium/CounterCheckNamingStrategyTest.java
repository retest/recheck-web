package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CounterCheckNamingStrategyTest {

	private final CounterCheckNamingStrategy cut = new CounterCheckNamingStrategy();

	@Test
	void makeUnique_should_append_unique_prefix() {
		assertThat( cut.getUniqueCheckName( "someString", null ) ).isEqualTo( "00" );
		assertThat( cut.getUniqueCheckName( "someOtherString", null ) ).isEqualTo( "01" );
		assertThat( cut.getUniqueCheckName( "someString", null ) ).isEqualTo( "02" );
	}

}

package de.retest.web.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;

class WebElementListRecheckAdapterTest {

	WebElementListRecheckAdapter cut;

	@BeforeEach
	void setUp() {
		cut = new WebElementListRecheckAdapter();
	}

	@Test
	void canCheck_with_empty_list_cannot_be_checked() throws Exception {
		assertThat( cut.canCheck( new ArrayList<WebElement>() ) ).isFalse();
		assertThat( cut.canCheck( Collections.<WebElement>emptyList() ) ).isFalse();
	}

	@Test
	void canCheck_with_single_argument_can_be_checked() throws Exception {
		final List<WebElement> elements = Collections.singletonList( mock( WebElement.class ) );
		final List<WrapsElement> wrapsElements = Collections.singletonList( mock( WrapsElement.class ) );

		assertThat( cut.canCheck( elements ) ).isTrue();
		assertThat( cut.canCheck( wrapsElements ) ).isTrue();
	}

	@Test
	void convert_should_still_convert_empty_lsits() throws Exception {
		assertThat( cut.convert( Collections.emptyList() ) ).isEmpty();
	}
}
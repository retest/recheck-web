package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

class ActionbasedCheckNamingStrategyTest {

	private final ActionbasedCheckNamingStrategy cut = new ActionbasedCheckNamingStrategy();

	@Test
	void getUniqueCheckName_should_return_simple_description_for_id() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() )
				.thenReturn( "[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> id: signupEmail]" );
		assertThat( cut.getUniqueCheckName( "click", mock ) ).isEqualTo( "click_signupEmail" );
	}

	@Test
	void same_action_should_still_be_unique() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() )
				.thenReturn( "[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> id: signupEmail]" );
		assertThat( cut.getUniqueCheckName( "click", mock ) ).isEqualTo( "click_signupEmail" );
		assertThat( cut.getUniqueCheckName( "click", mock ) ).isEqualTo( "click_signupEmail_2" );
		assertThat( cut.getUniqueCheckName( "click", mock ) ).isEqualTo( "click_signupEmail_3" );
	}

	@Test
	void getUniqueCheckName_should_return_simple_description_for_name() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() )
				.thenReturn( "[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> name: logo]" );
		assertThat( cut.getUniqueCheckName( "click", mock ) ).isEqualTo( "click_logo" );
	}

	@Test
	void getUniqueCheckName_should_return_simple_description_for_class() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() ).thenReturn(
				"[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> class name: sign-up-submit]" );
		assertThat( cut.getUniqueCheckName( "click", mock ) ).isEqualTo( "click_sign-up-submit" );
	}

	@Test
	void getUniqueCheckName_should_return_simple_description_for_link() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() ).thenReturn(
				"[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> link text: Contact]" );
		assertThat( cut.getUniqueCheckName( "click", mock ) ).isEqualTo( "click_Contact" );
	}

	@Test
	void getUniqueCheckName_should_contain_text_for_sendKeys() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() ).thenReturn(
				"[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> link text: Contact]" );

		assertThat( cut.getUniqueCheckName( "enter", mock,
				(Object[]) new CharSequence[] { "this very long text was sent" } ) )
						.isEqualTo( "enter_[this ...]_into_Contact" );
	}

	@Test
	void getUniqueCheckName_with_empty_parameters_on_get_should_return_simple_description() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() )
				.thenReturn( "[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> id: signupEmail]" );
		assertThat( cut.getUniqueCheckName( "get", mock ) ).isEqualTo( "get_signupEmail" );
	}

	@Test
	void shortenUrl_should_shorten_different_URLs() {
		assertThat( cut.shortenUrl( "http://retest.de" ) ).isEqualTo( "retest.de" );
		assertThat( cut.shortenUrl( "https://retest.de" ) ).isEqualTo( "retest.de" );
	}
}

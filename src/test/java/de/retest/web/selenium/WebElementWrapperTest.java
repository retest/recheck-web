package de.retest.web.selenium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;

class WebElementWrapperTest {

	@Test
	void toStep_should_return_simple_description_for_id() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() )
				.thenReturn( "[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> id: signupEmail]" );
		assertThat( WebElementWrapper.toStep( "click", mock ) ).isEqualTo( "click_signupEmail" );
	}

	@Test
	void toStep_should_return_simple_description_for_name() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() )
				.thenReturn( "[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> name: logo]" );
		assertThat( WebElementWrapper.toStep( "click", mock ) ).isEqualTo( "click_logo" );
	}

	@Test
	void toStep_should_return_simple_description_for_class() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() ).thenReturn(
				"[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> class name: sign-up-submit]" );
		assertThat( WebElementWrapper.toStep( "click", mock ) ).isEqualTo( "click_sign-up-submit" );
	}

	@Test
	void toStep_should_return_simple_description_for_link() {
		final WebElement mock = mock( WebElement.class );
		when( mock.toString() ).thenReturn(
				"[[ChromeDriver: chrome on MAC (5822a9b14739d081f70f6b6f42e789cc)] -> link text: Contact]" );
		assertThat( WebElementWrapper.toStep( "click", mock ) ).isEqualTo( "click_Contact" );
	}
}

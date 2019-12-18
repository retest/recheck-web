package de.retest.web.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;

import de.retest.web.util.SeleniumWrapperUtil.WrapperOf;
import lombok.Value;

class SeleniumWrapperUtilTest {

	@Test
	void should_detect_if_object_is_instance_of_wraps_element() {
		final Object someObject = new Object();
		assertThat( SeleniumWrapperUtil.isWrapper( WrapperOf.ELEMENT, someObject ) ).isFalse();

		final org.openqa.selenium.WrapsElement newWrapper = mock( org.openqa.selenium.WrapsElement.class );
		assertThat( SeleniumWrapperUtil.isWrapper( WrapperOf.ELEMENT, newWrapper ) ).isTrue();

		final WrapsElement oldWrapper = mock( WrapsElement.class );
		assertThat( SeleniumWrapperUtil.isWrapper( WrapperOf.ELEMENT, oldWrapper ) ).isTrue();
	}

	@Test
	void should_get_wrapped_element() throws Exception {
		final WebElement wrapped = mock( WebElement.class );

		final org.openqa.selenium.WrapsElement newWrapper = mock( org.openqa.selenium.WrapsElement.class );
		when( newWrapper.getWrappedElement() ).thenReturn( wrapped );
		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, newWrapper ) ).isSameAs( wrapped );

		final WrapsElement oldWrapper = mock( WrapsElement.class );
		when( oldWrapper.getWrappedElement() ).thenReturn( wrapped );
		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, oldWrapper ) ).isSameAs( wrapped );
	}

	@Test
	void should_detect_if_object_is_instance_of_wraps_driver() {
		final Object someObject = new Object();
		assertThat( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, someObject ) ).isFalse();

		final org.openqa.selenium.WrapsDriver newWrapper = mock( org.openqa.selenium.WrapsDriver.class );
		assertThat( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, newWrapper ) ).isTrue();

		final WrapsDriver oldWrapper = mock( WrapsDriver.class );
		assertThat( SeleniumWrapperUtil.isWrapper( WrapperOf.DRIVER, oldWrapper ) ).isTrue();
	}

	@Test
	void should_get_wrapped_driver() throws Exception {
		final WebDriver wrapped = mock( WebDriver.class );

		final org.openqa.selenium.WrapsDriver newWrapper = mock( org.openqa.selenium.WrapsDriver.class );
		when( newWrapper.getWrappedDriver() ).thenReturn( wrapped );
		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, newWrapper ) ).isSameAs( wrapped );

		final WrapsDriver oldWrapper = mock( WrapsDriver.class );
		when( oldWrapper.getWrappedDriver() ).thenReturn( wrapped );
		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, oldWrapper ) ).isSameAs( wrapped );
	}

	@Test
	void getWrapped_should_not_silently_ignore_exceptions_thrown_by_method() {
		final WrapsElement elementThrows = mock( WrapsElement.class );
		when( elementThrows.getWrappedElement() ).thenThrow( new NoSuchElementException( "No element found" ) );

		assertThatThrownBy( () -> SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, elementThrows ) )
				.hasCauseInstanceOf( NoSuchElementException.class );
	}

	@Test
	void getWrapped_should_not_loop_element_with_same_object() {
		final Object notElement = mock( Object.class );

		assertThatThrownBy( () -> SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, notElement ) )
				.isInstanceOf( IllegalArgumentException.class );
	}

	@Test
	void getWrapped_should_not_loop_driver_with_same_object() {
		final Object notElement = mock( Object.class );

		assertThatThrownBy( () -> SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, notElement ) )
				.isInstanceOf( IllegalArgumentException.class );
	}

	@Test
	void getWrapped_should_properly_handle_if_driver_returns_null() throws Exception {
		final org.openqa.selenium.WrapsDriver nullDriver = mock( org.openqa.selenium.WrapsDriver.class );

		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, nullDriver ) ).isEqualTo( null );
	}

	@Test
	void getWrapped_should_properly_handle_if_element_returns_null() throws Exception {
		final WrapsElement nullElement = mock( WrapsElement.class );

		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, nullElement ) ).isEqualTo( null );
	}

	@Test
	void getWrapped_should_throw_when_method_does_not_return_expected_type() throws Exception {
		final Wrapper<?> fooled = new Wrapper<>( new Object() );
		final org.openqa.selenium.WrapsDriver fooledDriver = () -> ((Wrapper<WebDriver>) fooled).getWrapped();

		assertThatThrownBy( () -> SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, fooledDriver ) ) //
				.isInstanceOf( RuntimeException.class ) //
				.hasMessageStartingWith( "Failed to invoke SeleniumWrapperUtilTest" ) //
				.hasCauseInstanceOf( ClassCastException.class );
	}

	@Value
	private static class Wrapper<T> {

		T wrapped;
	}
}

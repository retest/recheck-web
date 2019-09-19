package de.retest.web.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.internal.WrapsElement;

import de.retest.web.util.SeleniumWrapperUtil.WrapperOf;

class SeleniumWrapperUtilTest {

	@Test
	void should_detect_if_object_is_instance_of_wraps_element() {
		final Object someObject = new Object();
		assertThat( SeleniumWrapperUtil.instanceOf( WrapperOf.ELEMENT, someObject ) ).isFalse();

		final org.openqa.selenium.WrapsElement newWrapper = mock( org.openqa.selenium.WrapsElement.class );
		assertThat( SeleniumWrapperUtil.instanceOf( WrapperOf.ELEMENT, newWrapper ) ).isTrue();

		final WrapsElement oldWrapper = mock( WrapsElement.class );
		assertThat( SeleniumWrapperUtil.instanceOf( WrapperOf.ELEMENT, oldWrapper ) ).isTrue();
	}

	@Test
	void should_get_wrapped_element() throws Exception {
		final WebElement wrapped = mock( WebElement.class );
		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.ELEMENT, wrapped ) ).isSameAs( wrapped );

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
		assertThat( SeleniumWrapperUtil.instanceOf( WrapperOf.DRIVER, someObject ) ).isFalse();

		final org.openqa.selenium.WrapsDriver newWrapper = mock( org.openqa.selenium.WrapsDriver.class );
		assertThat( SeleniumWrapperUtil.instanceOf( WrapperOf.DRIVER, newWrapper ) ).isTrue();

		final WrapsDriver oldWrapper = mock( WrapsDriver.class );
		assertThat( SeleniumWrapperUtil.instanceOf( WrapperOf.DRIVER, oldWrapper ) ).isTrue();
	}

	@Test
	void should_get_wrapped_driver() throws Exception {
		final WebDriver wrapped = mock( WebDriver.class );
		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, wrapped ) ).isSameAs( wrapped );

		final org.openqa.selenium.WrapsDriver newWrapper = mock( org.openqa.selenium.WrapsDriver.class );
		when( newWrapper.getWrappedDriver() ).thenReturn( wrapped );
		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, newWrapper ) ).isSameAs( wrapped );

		final WrapsDriver oldWrapper = mock( WrapsDriver.class );
		when( oldWrapper.getWrappedDriver() ).thenReturn( wrapped );
		assertThat( SeleniumWrapperUtil.getWrapped( WrapperOf.DRIVER, oldWrapper ) ).isSameAs( wrapped );
	}

}

package de.retest.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.data.Index;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.By;

import de.retest.recheck.RecheckAdapter;
import de.retest.recheck.report.ActionReplayResult;
import de.retest.recheck.ui.diff.AttributeDifference;
import de.retest.recheck.ui.diff.ElementDifference;
import de.retest.web.selenium.UnbreakableDriver;
import de.retest.web.testutils.WebDriverFactory;
import de.retest.web.testutils.WebDriverFactory.Driver;

class RecheckSeleniumAdapterIT {

	UnbreakableDriver driver;
	RecheckWebImpl re;

	@BeforeEach
	void setUp() {
		driver = new UnbreakableDriver( WebDriverFactory.driver( Driver.CHROME ) );
		re = new RecheckWebImpl();
	}

	@AfterEach
	void tearDown() {
		driver.quit();
		re.cap();
	}

	@Test
	void warnings_should_be_notified_properly_on_breaking_change() {
		re.startTest();

		driver.get( getClass().getResource( "AutoHealingTest.html" ).toExternalForm() );

		final RecheckAdapter open = spyAdapter();
		re.check( driver, open, "open" );

		driver.findElement( By.id( "a" ) ).click();
		verifyWarning( open, "a", "b", 53 );

		final RecheckAdapter a = spyAdapter();
		re.check( driver, a, "a" );

		driver.findElement( By.id( "b" ) ).click();
		verifyWarning( a, "b", "a", 59 );

		final RecheckAdapter b = spyAdapter();
		re.check( driver, b, "b" );
		verifyNoWarning( b );

		assertThatThrownBy( () -> re.capTest() ) //
				.hasMessageContainingAll( "id: expected=\"a\", actual=\"b\"", "id: expected=\"b\", actual=\"a\"" );
	}

	public RecheckAdapter spyAdapter() {
		return spy( new RecheckSeleniumAdapter() );
	}

	private void verifyNoWarning( final RecheckAdapter adapter ) {
		final ArgumentCaptor<ActionReplayResult> captor = ArgumentCaptor.forClass( ActionReplayResult.class );
		verify( adapter ).notifyAboutDifferences( captor.capture() );
		final ActionReplayResult result = captor.getValue();

		assertThat( getAllAttributeDifferences( result ) ).allSatisfy( difference -> { // There are still differences
			assertThat( difference.getElementIdentificationWarnings() ).isEmpty();
		} );
	}

	private void verifyWarning( final RecheckAdapter adapter, final String expected, final String actual,
			final int line ) {
		final ArgumentCaptor<ActionReplayResult> captor = ArgumentCaptor.forClass( ActionReplayResult.class );
		verify( adapter ).notifyAboutDifferences( captor.capture() );
		final ActionReplayResult result = captor.getValue();

		assertThat( findAttributeDifferenceForId( result ) ).hasValueSatisfying( difference -> {
			assertThat( difference.getElementIdentificationWarnings() ) //
					.hasSize( 1 ) //
					.satisfies( warning -> {
						assertThat( warning.getTestFileName() ).isEqualTo( getClass().getSimpleName() + ".java" );
						assertThat( warning.getQualifiedTestName() ).isEqualTo( getClass().getName() );
						assertThat( warning.getTestLineNumber() ).isEqualTo( line );
						assertThat( warning.getFindByMethodName() ).isEqualTo( "id" );
					}, Index.atIndex( 0 ) );
			assertThat( difference.getExpected() ).isEqualTo( expected );
			assertThat( difference.getActual() ).isEqualTo( actual );
		} );
	}

	private Optional<AttributeDifference> findAttributeDifferenceForId( final ActionReplayResult result ) {
		return getAllAttributeDifferences( result ) //
				.filter( difference -> difference.getKey().equals( "id" ) ) // Only look for the id, but not the value
				.findFirst();
	}

	private Stream<AttributeDifference> getAllAttributeDifferences( final ActionReplayResult result ) {
		return result.getAllElementDifferences().stream() //
				.map( ElementDifference::getAttributeDifferences ) //
				.flatMap( Collection::stream );
	}
}

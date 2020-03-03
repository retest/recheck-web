package de.retest.web;

import de.retest.recheck.RecheckOptions;
import de.retest.web.screenshot.ScreenshotProvider;
import de.retest.web.screenshot.ScreenshotProviders;
import de.retest.web.selenium.AutocheckingCheckNamingStrategy;
import de.retest.web.selenium.CounterCheckNamingStrategy;
import lombok.Getter;

/**
 * This class extends {@link RecheckOptions} for some specific web options. Note that these options overwrite the
 * configuration as specified in {@link RecheckWebProperties}.
 */
@Getter
public class RecheckWebOptions extends RecheckOptions {

	private final AutocheckingCheckNamingStrategy checkNamingStrategy;
	private final ScreenshotProvider screenshotProvider;

	public RecheckWebOptions( final RecheckOptions superOptions,
			final AutocheckingCheckNamingStrategy checkNamingStrategy, final ScreenshotProvider screenshotProvider ) {
		super( superOptions );
		this.checkNamingStrategy = checkNamingStrategy;
		this.screenshotProvider = screenshotProvider;
	}

	public static RecheckWebOptionsBuilder builder() {
		return new RecheckWebOptionsBuilder();
	}

	public static class RecheckWebOptionsBuilder extends RecheckOptionsBuilder {

		private AutocheckingCheckNamingStrategy checkNamingStrategy = new CounterCheckNamingStrategy();
		private ScreenshotProvider screenshotProvider = null;

		/**
		 * @param checkNamingStrategy
		 *            The {@link AutocheckingCheckNamingStrategy} to be used. Default is
		 *            {@link CounterCheckNamingStrategy}.
		 * @return self
		 */
		public RecheckWebOptionsBuilder
				checkNamingStrategy( final AutocheckingCheckNamingStrategy checkNamingStrategy ) {
			this.checkNamingStrategy = checkNamingStrategy;
			return this;
		}

		/**
		 * @param screenshotProvider
		 *            The {@link ScreenshotProvider} to be used. Default is determined via
		 *            {@link RecheckWebProperties#screenshotProvider()}.
		 * @return self
		 */
		public RecheckWebOptionsBuilder screenshotProvider( final ScreenshotProvider screenshotProvider ) {
			this.screenshotProvider = screenshotProvider;
			return this;
		}

		/**
		 * Shortcut for <code>screenshotProvider( ScreenshotProviders.NONE )</code>.
		 *
		 * @return self
		 */
		public RecheckWebOptionsBuilder disableScreenshots() {
			return screenshotProvider( ScreenshotProviders.NONE );
		}

		/**
		 * Shortcut for <code>screenshotProvider( ScreenshotProviders.DEFAULT )</code>.
		 *
		 * @return self
		 */
		public RecheckWebOptionsBuilder enableScreenshots() {
			return screenshotProvider( ScreenshotProviders.DEFAULT );
		}

		@Override
		public RecheckWebOptions build() {
			return new RecheckWebOptions( super.build(), checkNamingStrategy, screenshotProvider );
		}
	}
}

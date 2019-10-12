package de.retest.web;

import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.retest.recheck.RecheckOptions;
import de.retest.web.selenium.AutocheckingCheckNamingStrategy;
import de.retest.web.selenium.CounterCheckNamingStrategy;
import lombok.Getter;

/**
 * This class extends RecheckOptions for some specific web options.
 */
@Getter
public class RecheckWebOptions extends RecheckOptions {

	/**
	 * The auto-checking driver needs more info than the standard naming strategy can provide. Therefore we need a
	 * special {@link AutocheckingCheckNamingStrategy}.
	 */
	private final AutocheckingCheckNamingStrategy checkNamingStrategy;

	/**
	 * The {@link AttributesProvider} that defines the CSS attributes to check.
	 */
	private final AttributesProvider attributesProvider;

	/**
	 * The {@link ScreenshotProvider} to use.
	 */
	private final ScreenshotProvider screenshotProvider;

	public RecheckWebOptions( final RecheckOptions superOptions,
			final AutocheckingCheckNamingStrategy checkNamingStrategy, final AttributesProvider attributesProvider,
			final ScreenshotProvider screenshotProvider ) {
		super( superOptions );
		this.checkNamingStrategy = checkNamingStrategy;
		this.attributesProvider = attributesProvider;
		this.screenshotProvider = screenshotProvider;
	}

	public static RecheckWebOptionsBuilder builder() {
		return new RecheckWebOptionsBuilder();
	}

	public static class RecheckWebOptionsBuilder extends RecheckOptionsBuilder {

		private AutocheckingCheckNamingStrategy checkNamingStrategy = new CounterCheckNamingStrategy();
		private AttributesProvider attributesProvider = YamlAttributesProvider.getInstance();
		private ScreenshotProvider screenshotProvider = new ScreenshotProvider();

		public RecheckWebOptionsBuilder
				checkNamingStrategy( final AutocheckingCheckNamingStrategy checkNamingStrategy ) {
			this.checkNamingStrategy = checkNamingStrategy;
			return this;
		}

		public RecheckWebOptionsBuilder attributesProvider( final AttributesProvider attributesProvider ) {
			this.attributesProvider = attributesProvider;
			return this;
		}

		public RecheckWebOptionsBuilder omitScreenshots() {
			screenshotProvider = new ScreenshotProvider() {
				@Override
				public BufferedImage shoot( final WebDriver driver, final WebElement element ) {
					return null;
				}
			};
			return this;
		}

		@Override
		public RecheckWebOptions build() {
			return new RecheckWebOptions( super.build(), checkNamingStrategy, attributesProvider, screenshotProvider );
		}
	}
}

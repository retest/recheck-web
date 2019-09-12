package de.retest.web;

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

	public RecheckWebOptions( final RecheckOptions superOptions,
			final AutocheckingCheckNamingStrategy checkNamingStrategy ) {
		super( superOptions );
		this.checkNamingStrategy = checkNamingStrategy;
	}

	public static RecheckWebOptionsBuilder builder() {
		return new RecheckWebOptionsBuilder();
	}

	public static class RecheckWebOptionsBuilder extends RecheckOptionsBuilder {

		private AutocheckingCheckNamingStrategy checkNamingStrategy = new CounterCheckNamingStrategy();

		public RecheckWebOptionsBuilder
				checkNamingStrategy( final AutocheckingCheckNamingStrategy checkNamingStrategy ) {
			this.checkNamingStrategy = checkNamingStrategy;
			return this;
		}

		@Override
		public RecheckWebOptions build() {
			return new RecheckWebOptions( super.build(), checkNamingStrategy );
		}
	}
}

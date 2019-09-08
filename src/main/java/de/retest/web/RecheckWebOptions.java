package de.retest.web;

import de.retest.recheck.RecheckOptions;
import de.retest.web.selenium.AutocheckingCheckNamingStrategy;
import de.retest.web.selenium.CounterCheckNamingStrategy;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

/**
 * This class extends RecheckOptions for some specific web options.
 */
@SuperBuilder
@Getter
public class RecheckWebOptions extends RecheckOptions {

	/**
	 * The auto-checking driver needs more info than the standard naming strategy can provide. Therefore we need a
	 * special {@link AutocheckingCheckNamingStrategy}.
	 */
	@Builder.Default
	private final AutocheckingCheckNamingStrategy namingStrategy = new CounterCheckNamingStrategy();

}

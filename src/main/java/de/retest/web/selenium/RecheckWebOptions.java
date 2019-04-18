package de.retest.web.selenium;

import de.retest.recheck.RecheckOptions;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class RecheckWebOptions extends RecheckOptions {

	@Builder.Default
	private final AutocheckingCheckNamingStrategy namingStrategy = new CounterCheckNamingStrategy();

}

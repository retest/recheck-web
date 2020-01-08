package de.retest.web.selenium;

import de.retest.recheck.ui.diff.ElementIdentificationWarning;
import lombok.Value;

@Value
public class QualifiedElementWarning {

	private String retestId;
	private String attributeKey;
	private ElementIdentificationWarning warning;

}

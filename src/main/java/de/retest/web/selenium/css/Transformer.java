package de.retest.web.selenium.css;

@FunctionalInterface
interface Transformer {

	Selector transform( String selector );

}

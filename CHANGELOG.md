Changelog
=========

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/) and this project adheres to
[Semantic Versioning](https://semver.org/).

But we group the changes as follows:

* *Breaking Changes* when a change requires manual activity at update.
* *Bug Fixes* when we fix broken functionality.
* *New Features* for added functionality.
* *Improvements* for other changed parts.


Table of Contents
-----------------

[TOC]


[Unreleased]
------------

### Breaking Changes

* The default of how screenshots are created has changed from full page to viewport only. These two options are now available to set globally via properties and per test individually via `RecheckWebOptions`.

### Bug Fixes

### New Features

* Screenshot creation can now be configured via the `RecheckOptions` or globally via `de.retest.recheck.web.screenshot.provider`. 

### Improvements

* `AutocheckingRecheckDriver` and thus the `RecheckDriver` now implement the `RecheckLifecycle`, meaning that they can be used with the JUnit extensions.


--------------------------------------------------------------------------------


[1.6.0] (2019-11-06)
--------------------

### Bug Fixes

* Fix `StackOverflowError` if a `WrapsElement` (e.g. `@FindBy`) is checked that throws an exception.
* Fix `NullPointerException` if the checked `WebElement` is not visible.

### New Features

* Option to skip checks with `AutocheckingRecheckDriver`.

```java
private AutocheckingRecheckDriver driver;
@Test
public void loginTest() throws Exception {
	driver.findElement( By.id( "username" ) ).skipCheck().sendKeys( "myuser" );
	driver.findElement( By.id( "password" ) ).skipCheck().sendKeys( "1234" );
	driver.findElement( By.id( "login-from" ) ).submit();
}
```


[1.5.1] (2019-09-23)
--------------------

### Bug Fixes

* Fix [#355](https://github.com/retest/recheck-web/issues/355) when an exception is thrown in JavaScript if the element has no parent. 


[1.5.0] (2019-09-12)
--------------------

### Improvements

* Check now also individual `WebElement`s, making recheck a "Jest for Java".
* Handle iFrames without id correctly.
* Autocheck also on actions on `Navigate`.
* Raise no exception of screenshot creation fails.
* Extend the `RecheckOptions` correctly with `RecheckWebOptions`.


[1.4.0] (2019-08-21)
--------------------

Changes not tracked before...

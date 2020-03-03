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

* Add `shoot(WebDriver, WebElement)` in interface `ScreenshotProvider` to create screenshots for `WebElement`.
* Change constructor visibility of `RecheckWebOptions` and `RecheckWebOptions$Builder` from `public` to `protected`. Please use the corresponding builder accordingly.

### Bug Fixes

* Fix that cascading frames inside of frames are not analyzed. Now depth doesn't matter...
* Fix that `NoScreenshot` or `RecheckWebOptions#disableScreenshots()` is not applied to web elements.
* Fix that `de.retest.recheck.web.screenshot.provider` from `recheck.ignore` is ignored when `RecheckWebOptions` are used.
* Fix that passing an element into `RecheckWebImpl` from an `UnbreakableDriver` throws an error.
* Fix that `viewportOnly` points to `ViewportOnlyMinimalScreenshot` and adapt the default accordingly (`viewportOnlyMinimal`).

### New Features

* Provide additional metadata: current URL
* Save the warnings identified by the `UnbreakableDriver` to the report to allow for healing of the test code.
* Implement [#389](https://github.com/retest/recheck-web/issues/389)
    * All CSS selectors using attribute syntax are supported.
    * The pseudo class selectors: checked, disabled, and read-only are supported.

### Improvements

* An exception is now thrown if explicit `Recheck#check` is called with an implicit `RecheckDriver`. This kind of mixing is not expected and therefore now prevented as it produced unexpected behavior due to both checks trying to find and create a Golden Master.
* Improve the breaking messages from a `UnbreakableDriver` to include more information like the line number.
* Add short cuts for screenshot enabling/disabling via the `RecheckWebOptions`.
* Replace the internal dependency to [Jackson](https://github.com/FasterXML/jackson) (which often caused trouble) with [SnakeYAML](https://bitbucket.org/asomov/snakeyaml).


--------------------------------------------------------------------------------


[1.9.0] (2020-01-29)
--------------------

### Bug Fixes

* Fix that cascading frames inside of frames are not analyzed. Now depth doesn't matter...

### New Features

* Provide additional metadata: current URL
* Save the warnings identified by the `UnbreakableDriver` to the report to allow for healing of the test code.

### Improvements

* An exception is now thrown if explicit `Recheck#check` is called with an implicit `RecheckDriver`. This kind of mixing is not expected and therefore now prevented as it produced unexpected behavior due to both checks trying to find and create a Golden Master.
* Improve the breaking messages from a `UnbreakableDriver` to include more information like the line number.
* Add short cuts for screenshot enabling/disabling via the `RecheckWebOptions`.


[1.8.1] (2020-01-23)
--------------------

### Bug Fixes

* (Re)Established IE11 compatibility.


[1.8.0] (2019-12-13)
--------------------

### New Features

* As recheck gathers metadata, recheck-web will provide additional metadata:
    * Browser name and version.
    * Driver width and height.
    * Operating system and version if specified in the driver.

### Improvements

* `AutocheckingRecheckDriver` and thus the `RecheckDriver` now implement the `RecheckLifecycle`, meaning that they can be used with the JUnit extensions.
* Big screenshots create a lot of overhead in storing, transferring and processing. Since we need screenshots only for the user, we reduce that overhead by specifying a max width in pixel, to which the screenshot is resized. 


[1.7.0] (2019-11-21)
--------------------

### Breaking Changes

* The default of how screenshots are created has changed from full page to viewport only. These two options are now available to set globally via properties and per test individually via `RecheckWebOptions`.

### New Features

* Screenshot creation can now be configured via the `RecheckOptions` or globally via `de.retest.recheck.web.screenshot.provider`. 


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

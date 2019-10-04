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

### Bug Fixes

* Fix `StackOverflowError` if a `WrapsElement` (e.g. `@FindBy`) is checked that throws an exception.
* Fix `NullPointerException` if the checked `WebElement` is not visible.

### New Features

### Improvements


--------------------------------------------------------------------------------


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

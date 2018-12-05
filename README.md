# ![recheck logo](https://user-images.githubusercontent.com/1871610/41766965-b69d46a2-7608-11e8-97b4-c6b0f047d455.png) for web apps

[![Build Status](https://travis-ci.com/retest/recheck-web.svg?branch=master)](https://travis-ci.com/retest/recheck-web)
[![Latest recheck-web on Maven Central](https://maven-badges.herokuapp.com/maven-central/de.retest/recheck-web/badge.svg?style=flat)](https://mvnrepository.com/artifact/de.retest/recheck-web)
[![license](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/retest/recheck-web/blob/master/LICENSE)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-ff69b4.svg)](https://github.com/retest/recheck-web/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)
[![code with hearth by retest](https://img.shields.io/badge/%3C%2F%3E%20with%20%E2%99%A5%20by-retest-C1D82F.svg)](https://retest.de/en/)

[recheck](https://github.com/retest/recheck) is a Golden Master-based test framework on top of Selenium that comes with powerful features. Check everything at once and create unbreakable tests.

<p align="center"><a href="https://youtu.be/ZK-r3GaM2Dw"><img src="https://user-images.githubusercontent.com/1871610/44585296-23b3ce80-a7ac-11e8-9687-d8939209b05a.gif" /></a></p>


## Features

* Easy creation and maintenance of checks for web.
* Semantic comparison of contents.
* Easily ignore volatile elements, attributes or sections.
* One-click maintenance to update Golden Masters with intended changes.
* No unexpected changes go unnoticed.
* Operates on top of Selenium.
* Makes your tests unbreakable.
* The Git for your GUI.


## Advantages

Instead of manually defining individual aspects that you want to check, check everything at once. So instead of writing many `assert` statements—and still not have complete checks—write a single `re.check()`. This saves a lot of effort when creating tests. And it makes sure to not [miss unexpected changes](https://hackernoon.com/assertions-considered-harmful-d3770d818054).

Even better: Using the [retest GUI](https://retest.de/en/) (or the soon to come open-source CLI), you can easily accept those changes with a single click (patent pending). This also saves a lot of time during maintenance. Moreover, any regular changing aspects or elements (e.g. date fields) can easily be ignored.

And, using the Golden Master, recheck can identify elements even after the identifying attribute was changed. So assume you are using, e.g. an HTML `id` property to identify an element within your Selenium test. Now, assume that this `id` property changes within the HTML. Then, your test would break, resulting in an `NoSuchElementException`. But using `RecheckDriver` as a drop-in replacement/wrapper of your normal driver magically finds the element and logs a warning such as

```
*************** recheck warning ***************
The HTML id attribute used for element identification changed from 'intro-slider' to 'introSlider'.
retest identified the element based on the persisted old state.
If you apply these changes to the state , your test  will break.
Use `By.id("introSlider")` or `By.retestId("9c40281d-5655-4ffa-9c6d-d079e01bb5a3")` to update your test.
```


## Prerequisites

Operates on top of [Selenium](https://www.seleniumhq.org/projects/webdriver/). Selenium has become an official [W3C standard](https://www.w3.org/TR/webdriver1/), supported by all major browsers. Learn more about Selenium and [how to install it](https://www.seleniumhq.org/download/).

recheck is available as a Java API with support for [JUnit 4](https://junit.org/junit4/) and [JUnit 5](https://junit.org/junit5/) as well as [TestNG](https://testng.org/). 


## Setup

Download recheck-web here on [GitHub](https://https://github.com/retest/recheck-web/releases) or add it as dependency through [Maven Central](https://search.maven.org/search?q=g:de.retest%20a:recheck-web): [![Latest recheck-web on Maven Central](https://maven-badges.herokuapp.com/maven-central/de.retest/recheck-web/badge.svg?style=flat)](https://mvnrepository.com/artifact/de.retest/recheck-web)

```xml
<dependency>
  <groupId>de.retest</groupId>
  <artifactId>recheck-web</artifactId>
  <version><!-- latest version, see above link --></version>
</dependency>
```


## Usage of plain recheck

Then replace the assertions in your Selenium test. An example test could look like so ([simplified](https://github.com/retest/recheck-web/blob/master/src/test/java/de/retest/web/SimpleShowcaseIT.java)):

```java
public class MyRecheckWebTest {

  private WebDriver driver;
  private Recheck re;

  @Before
  public void setUp() {
    driver = new ChromeDriver();
    
    // Use the default implementation.
    re = new RecheckImpl();
  }

  @Test
  public void index() throws Exception {
    // Set the file name of the Golden Master.
    re.startTest( "my-file-name" );

    // Do your Selenium stuff.
    driver.get( "https://my-url.org/" );

    // Single call instead of multiple assertions (doesn't fail on differences).
    re.check( driver, "my-step-name" );

    // Conclude the test case (fails on differences).
    re.capTest();
  }

  @After
  public void tearDown() {
    driver.quit();
    
    // Produce the result file.
    re.cap();
  }
}
```

Running such a test for the first time will result in a failure with an output like so:

```
java.lang.AssertionError: Found 1 differences in 1 checks of which 1 are unique: [No recheck file found.]

Details: 
test simple-showcase has 1 differences (1 unique): 
No recheck file found. First time test was run? Created recheck file now, don't forget to commit...

	at de.retest.recheck.RecheckImpl.capTest(SourceFile:135)
	at de.retest.web.SimpleRecheckShowcaseIT.index(SimpleRecheckShowcaseIT.java:61)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
...

```

Running such a test will also create a folder structure containing a `retest.xml` file and a screenshot per check (depending on your chosen names and configuration). These are now the Golden Master, the baseline which future executions of this test are compared against. If you use version control, you should commit those files. Note that the `retest.xml` contains a full description of the _rendered_ website, including all relevant information such as text, source, etc. and _all_ non-default CSS attributes such as font and margin. Although these files may become large, they are smaller than the original and by ignoring specific (or all) attributes, you can configure how large they are. Anyways, storing a few kilobyte extra is much cheaper than the manpower needed to manually specify checks.

Executing the same test again should not result in any differences. But after changing the website and executing the test, you should see the test reporting your changes.

E.g. if you change a single CSS rule, you will find all elements that are affected by [this change](https://github.com/retest/recheck-web/commit/a3e9edcbac5ef92152a2dc94e06a9f4918e0906f). Like so:

```
java.lang.AssertionError: Found 8 differences in 1 checks of which 2 are unique: [color: expected="rgb(65, 65, 65)", actual="rgb(0, 0, 0)", border-color: expected="rgb(65, 65, 65)", actual="rgb(0, 0, 0)"]

Details: 
test showcase has 8 differences (2 unique): 
index resulted in: 
	EM [Have you had enough]:
		border-color, color: expected="rgb(65, 65, 65)", actual="default"
	EM [Use an artificially intelligent]:
		border-color, color: expected="rgb(65, 65, 65)", actual="default"
	EM [to fully automatically test your application.]:
		border-color, color: expected="rgb(65, 65, 65)", actual="default"
	EM [No need to create assertions]:
		border-color, color: expected="rgb(65, 65, 65)", actual="default"
	EM [retest compares the whole picture instead of a single piece of the puzzle.]:
		border-color, color: expected="rgb(65, 65, 65)", actual="default"
	EM [The future of testing is in AI.]:
		border-color, color: expected="rgb(65, 65, 65)", actual="default"
	EM [The future of testing is now]:
		border-color, color: expected="rgb(65, 65, 65)", actual="default"
```

Additionally, a file named `replay.result` will be created upon test failure, typically located in your `target/test-classes` folder. This file can now be used to apply those changes to your baseline, using either the [recheck-cli](https://github.com/retest/recheck-cli) or the [retest-GUI](http://retest.org).


## Usage of RecheckDriver / "Unbreakable Selenium"

In order to use "Unbreakable Selenium", you just need to wrap your usual driver within a `RecheckDriver` (drop-in replacement) and use `RecheckWebImpl` instead of `RecheckImpl`. The code would the look like [so](https://github.com/retest/recheck-web/blob/master/src/test/java/de/retest/web/SimpleUnbreakableSeleniumShowcaseIT.java)):

```java
 // Use the RecheckDriver as a wrapper for your usual driver.
 driver = new RecheckDriver( new ChromeDriver() );

 // Use the unbreakable recheck implementation.
 re = new RecheckWebImpl();
```


## License

This project is licensed under the [AGPL license](LICENSE).

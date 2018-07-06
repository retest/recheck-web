# ![recheck logo](https://user-images.githubusercontent.com/1871610/41766965-b69d46a2-7608-11e8-97b4-c6b0f047d455.png) for web apps

[![Build Status](https://travis-ci.com/retest/recheck-web.svg?branch=master)](https://travis-ci.com/retest/recheck-web)
[![license](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/retest/recheck-web/blob/master/LICENSE)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-ff69b4.svg)](https://github.com/retest/recheck-web/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)
[![code with hearth by retest](https://img.shields.io/badge/%3C%2F%3E%20with%20%E2%99%A5%20by-retest-C1D82F.svg)](https://retest.de/en/)

[recheck](https://github.com/retest/recheck) for web apps. Replace manual asserts and check everything at once.


## Features

* Easy creation and maintenance of checks for web.
* Semantic comparison of contents.
* Easily ignore volatile elements, attributes or sections.
* One-click maintenance to update tests with intended changes.
* No unexpected changes go unnoticed.
* Operates on top of Selenium.
* The Git for your GUI.


## Advantages

Instead of manually defining individual aspects that you want to check, check everything at once. So instead of writing many `assert` statements—and still not have complete checks—write a single `re.check()`. This saves a lot of effort when creating tests. And it makes sure to not [miss unexpected changes](https://hackernoon.com/assertions-considered-harmful-d3770d818054).

Even better: Using the [retest GUI](https://retest.de/en/) (or the soon to come open-source CLI), you can easily accept those changes with a single click (patent pending). This also saves a lot of time during maintenance. Moreover, any regular changing aspects or elements (e.g. date fields) can easily be ignored.


## Prerequisites

Currently available as a Java API with support for JUnit 4.


## Usage

Download recheck-web [here on GitHub](https://github.com/retest/recheck-web/releases/) or add it as a Maven dependency in your POM:

```xml
<dependency>
  <groupId>de.retest</groupId>
  <artifactId>recheck-web</artifactId>
  <version>0.1.0</version>
</dependency>
```

Then replace the assertions in your Selenium test. An example test could look like so ([simplified](https://github.com/retest/recheck-web/blob/master/src/test/java/de/retest/web/IntegrationTest.java)):

```java
public class MyWebTest {

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
    re.startTest( "index" );

    // Do your Selenium stuff.
    driver.get( "your url" );

    // Single call instead of multiple assertions (doesn't fail on differences).
    re.check( driver, "index" );

    // Conclude the test case (fails on differences).
    re.capTest();
  }

  @After
  public void tearDown() {
    driver.quit();
    // Produce the test result.
    re.cap();
  }
}
```


## Building

To build this project locally, you have to skip JAR signing.

For normal builds use:

```
mvn deploy -Dgpg.skip=true
```

For making releases use:

```
mvn release:prepare -Darguments="-Dgpg.skip=true"
```


## License

This project is licensed under the [AGPL license](LICENSE).

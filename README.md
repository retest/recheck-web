# <a href="https://retest.dev"><img src="https://assets.retest.org/retest/ci/logos/recheck-screen.svg" width="300"/></a>-web [![Tweet](https://img.shields.io/twitter/url/http/shields.io.svg?style=social)](https://twitter.com/intent/tweet?text=Have%20a%20look%20at%20the%20test%20automation%20API%20recheck-web&url=http://retest.dev&via=retest_en&hashtags=opensource,software,testing,testautomation,developers)

[![Price](https://img.shields.io/badge/price-FREE-0098f7.svg)](https://github.com/retest/recheck-web/blob/master/LICENSE)
[![Build Status](https://travis-ci.com/retest/recheck-web.svg?branch=master)](https://travis-ci.com/retest/recheck-web)
[![Latest recheck-web on Maven Central](https://maven-badges.herokuapp.com/maven-central/de.retest/recheck-web/badge.svg?style=flat)](https://mvnrepository.com/artifact/de.retest/recheck-web)
[![license](https://img.shields.io/badge/license-AGPL-brightgreen.svg)](https://github.com/retest/recheck-web/blob/master/LICENSE)
[![PRs welcome](https://img.shields.io/badge/PRs-welcome-ff69b4.svg)](https://github.com/retest/recheck-web/issues?q=is%3Aissue+is%3Aopen+label%3A%22help+wanted%22)
[![code with hearth by retest](https://img.shields.io/badge/%3C%2F%3E%20with%20%E2%99%A5%20by-retest-C1D82F.svg)](https://retest.de/en/)

***recheck-web*** is a Golden Master-based test framework on top of Selenium that allows for easy creation and maintenance of more complete and nearly unbreakable tests. If you want to give it a quick try, checkout the [Chrome extension](https://chrome.google.com/webstore/detail/recheck-web-demo/ifbcdobnjihilgldbjeomakdaejhplii) that is based on recheck-web.

<p align="center"><a href="https://www.youtube.com/watch?v=7cl8flD--i0"><img src="https://user-images.githubusercontent.com/1871610/66708890-27930f00-ed26-11e9-878e-be9d616cec34.gif" /></a></p>


## TL;DR

Imagine you could:

1. Have your end-to-end tests actually fail when the website is broken or unusable (including visual regressions)
2. Have your tests _NOT_ fail when something as insignificant as an element ID changes
3. Maintain your tests with minimal effort

***recheck-web*** gives you these superpowers, and more.


## Table of Contents

- [Example](#example)
- [Benefits](#benefits)
- [How it works](#how-it-works)
- [Quickstart](#quickstart)
- [FAQs](#faqs)
- [License](#license)


## Example

A typical Selenium test could look like the following:

```java
driver.findElement( By.id( "username" ) ).sendKeys( "Simon" );
driver.findElement( By.id( "password" ) ).sendKeys( "secret" );
driver.findElement( By.id( "sign-in" ) ).click();

assertEquals( driver.findElement( By.tagName( "h4" ) ).getText(), "Success!" );
```

If the website e.g. looses _all_ of its CSS, thus rendering it essentially broken for a user, this test will still pass. However, if you change an invisible attribute that is irrelevant for a user, e.g. the element ID `username`, this test will break.

By simply wrapping the driver in a `RecheckDriver`, all of this changes. When the looks of the website changes, the test will fail. And if the element ID changes, the test will still execute, albeit report the change—unless you chose to ignore it e.g. by specifying `attribute=id` in your `recheck.ignore` file (a file similar to Git's `.gitignore`). 


## Benefits

With ***recheck-web*** you can:
* Easily create and maintain checks in your tests
* Perform _deep_ visual testing that goes beyond the UI and doesn't fail with every shifted pixel, i.e. _semantically_ compare all of the contents, tags and CSS attributes of websites or `WebElement`s *after rendering*
* Easily ignore volatile elements, attributes or sections, using a [Git-like syntax](https://docs.retest.de/recheck/usage/filter/)
* Automatically maintain Golden Masters (and soon [auto-heal your tests](https://github.com/retest/recheck.cli/issues/141)) with intended changes, using our [GUI](https://retest.de/review/) or [CLI](https://github.com/retest/recheck.cli)
* Be sure no unexpected change goes unnoticed
* Reuse your existing tests and infrastructure, as ***recheck-web*** operates on top of Selenium and is compatible with many test frameworks
* Use a virtual constant `retestId` to reference elements, instead of element IDs, names, XPaths and other attributes that are prone to changes
* Have virtually [unbreakable Selenium tests](https://retest.de/feature-unbreakable-selenium/)


## How It Works

Automated regression tests are not tests, in the sense that they don't aim to find existing bugs. Instead, they guard against unintended changes. As such, they are an extension to version control—but today's test tools are [not optimized for this work flow](https://hackernoon.com/assertions-considered-harmful-d3770d818054).

recheck takes this to the next level. You can explicitly or implicitly create Golden Masters (essentially a copy of the rendered website) and semantically compare against these. Irrelevant changes are easy to ignore and the Golden Masters are effortless to update. In case of critical changes that would otherwise break your tests, ***recheck-web*** can now peek into the Golden Master, find the element there, and (based on additional attributes) still identify the changed element on the current website.

![best-match copy](https://user-images.githubusercontent.com/1871610/65941692-e9dfdd80-e42b-11e9-8d07-d3284ea57f12.png)


## Quickstart

If you just want to quickly try ***recheck-web*** without the hassle of a full-project setup, we recommend using the [Chrome extension](https://chrome.google.com/webstore/detail/recheck-web-demo/ifbcdobnjihilgldbjeomakdaejhplii) that is based on ***recheck-web***.

***recheck-web*** operates on top of [Selenium](https://www.seleniumhq.org/projects/webdriver/). Selenium has become an official [W3C standard](https://www.w3.org/TR/webdriver1/), supported by all major browsers. Learn more about Selenium and [how to install it](https://www.seleniumhq.org/download/).

Simply add `recheck-web` as dependency to your project, e.g. via [Maven Central](https://search.maven.org/search?q=g:de.retest%20a:recheck-web): [![Latest recheck-web on Maven Central](https://maven-badges.herokuapp.com/maven-central/de.retest/recheck-web/badge.svg?style=flat)](https://mvnrepository.com/artifact/de.retest/recheck-web)

```xml
<dependency>
    <groupId>de.retest</groupId>
    <artifactId>recheck-web</artifactId>
    <version><!-- latest version, see above link --></version>
</dependency>
```

***recheck-web*** leverages [***recheck***](https://github.com/retest/recheck/), which is available as a Java API with support for [JUnit 4](https://junit.org/junit4/) and [JUnit 5](https://junit.org/junit5/) as well as [TestNG](https://testng.org/).

Now either create explicit checks or implicit checks.


### Explicit Checks

For explicit checks in your tests:

- Create an instance of `Recheck`, e.g. via `Recheck re = new RecheckImpl()` (typically in your `@Before`)
- Start tests via `re.startTest("test-name")`
- Check the complete current website via `re.check(driver, "check-name")` or individual web elements via `re.check(webElement, "check-name")`
- Make the test fail on changes via `re.capTest()`
- Produce a `report` file via `re.cap()` (e.g. in your `@After`), so changes can easily be applied to the Golden Masters 

A simple example test with explicit checks can be found in the [SimpleRecheckShowcaseIT](https://github.com/retest/recheck-web/blob/master/src/test/java/de/retest/web/it/SimpleRecheckShowcaseIT.java).

A complete tutorial about how to setup and use ***recheck-web*** like this can e.g. be found in [our documentation](https://docs.retest.de/recheck-web/tutorial/explicit-checks/).


### Implicit Checks

You can also use the recheck driver wrapper, which will automatically create a check after every action. To use this, simply wrap your driver like so:

```java
driver = new RecheckDriver( new ChromeDriver() );
```

Currently, you still need to call `driver.capTest()` after your test has finished, but we're [working on this](https://github.com/retest/recheck/issues/448), such that you can integrate recheck into your JUnit life-cycle (with [JUnit4](https://github.com/retest/recheck-junit-4-extension) and [JUnit 5](https://github.com/retest/recheck-junit-jupiter-extension))...


## FAQs

If there are any questions that are not answered here, please do not hesitate to [ask](https://github.com/retest/recheck-web/issues)!


### Why does my test fail the first time?

Running a ***recheck-web*** test for the first time will result in a failure with an output like so:

```
java.lang.AssertionError: No Golden Master found. First time test was run? Created new Golden Master, so don't forget to commit...
    at de.retest.recheck.RecheckImpl.capTest(RecheckImpl.java:191)
...
```

Running such a test will also create a folder structure containing a `retest.xml` file and a screenshot per check, the location of both depends on your configuration, see [below](#how-can-i-configure-the-location-of-the-golden-master-files). This contains the Golden Master against which future executions of this test are compared. If you use version control, you should commit those files. Note that the `retest.xml` contains a full description of the _rendered_ website, including all relevant information such as text, source, etc. and _all_ non-default CSS attributes such as font and margin. Although these files may become large, they are smaller than the original and by ignoring specific (or all) attributes, you can configure how large they are. Anyways, storing a few kilobyte extra is much cheaper than the manpower needed to manually specify such checks.

Executing the same test again should not result in any differences. But after changing the website and executing the test, you should see the test reporting your changes.


### Help, I have too many changes! What do I do?

You need to decide what the goal of your test setup is. With traditional assertions, you ignore more than 99% of the changes. Instead, much like Git without an ignore file, ***recheck-web*** will report _every_ change. So it is up to you to ignore changes that are not of interest for you. ***recheck-web*** can be used for cross-browser and cross-device testing, deep visual regression testing and functional regression testing. ***recheck-web*** is just a tool, it depends on you how you want to use it.

A good starting point are the [pre-defined filter files](https://github.com/retest/recheck/tree/master/src/main/resources/filter/web) in recheck. [Ignoring positioning](https://github.com/retest/recheck/blob/master/src/main/resources/filter/web/positioning.filter) is usually a good idea. If you want to know more about the ways to maintain your ignore file or create your own filters, refer to the [detailed documentation](https://docs.retest.de/recheck/usage/filter/).

If you didn't change anything between two executions, you can use our CLI and e.g. call `recheck ignore --all` to simply add all changes as ignore rules to your `recheck.ignore` file.


### I still have too many changes! What do I do?

You can use filters in conjunction with the [CLI](https://github.com/retest/recheck.cli/) or the [GUI](https://retest.de/review/) to quickly drill down on what changes are relevant to you. E.g. ignore changes to positioning, CSS and invisible attributes, and see only changes in content using:

```text
recheck diff --exclude positioning.filter \
        --exclude style-attributes.filter \
        --exclude invisible-attributes.filter \
        tests.report
```

If you have trouble with this, please [contact us](https://github.com/retest/recheck-web/issues), so we can learn on how to improve this for you.


### How can I configure the location of the Golden Master files?

You can provide a [project layout](https://github.com/retest/recheck/blob/master/src/main/java/de/retest/recheck/persistence/ProjectLayout.java) via the `RecheckOptions`:

```java
RecheckOptions opts = RecheckOptions.builder()
        .projectLayout( new CustomProjectLayout() )
        .build();
driver = new RecheckDriver( new ChromeDriver(), opts );
```

The default project layout is [Maven](https://github.com/retest/recheck/blob/master/src/main/java/de/retest/recheck/persistence/MavenProjectLayout.java) and will create Golden Master files under `src/test/resources/retest/recheck/` and reports as `${TEST_CLASS_NAME}.report` in your `target/test-classes/retest/recheck/` folder. A [Gradle layout](https://github.com/retest/recheck/blob/master/src/main/java/de/retest/recheck/persistence/GradleProjectLayout.java) is also available. If you want your Golden Master files and reports to be located somewhere different, implementing this interface is straightforward.

The [naming strategy](https://github.com/retest/recheck/blob/master/src/main/java/de/retest/recheck/persistence/NamingStrategy.java) is responsible for automagically retrieving names for the suite and test. The default implementation does so based on the [JUnit names](https://github.com/retest/recheck/blob/master/src/main/java/de/retest/recheck/persistence/ClassAndMethodBasedNamingStrategy.java). An [explicit naming strategy](https://github.com/retest/recheck/blob/master/src/main/java/de/retest/recheck/persistence/ExplicitMutableNamingStrategy.java) with setters and getters is also available. 


### How can I open the `report` file? 

You can open the `report` using [***review***](https://retest.de/review/) or the open-source [***recheck.cli***](https://github.com/retest/recheck.cli/). Both let you easily accept changes with a single click (patent pending) or a single command. This also saves a lot of time during maintenance. Moreover, any regularly changing aspect or element (e.g. date fields) can easily be ignored (see [above](#help-i-have-too-many-changes-what-do-i-do)).


### What does the recheck warning mean?

***recheck-web*** can identify elements even after the identifying attribute has changed. So assume you are using, e.g. an HTML `id` property to identify an element within your Selenium test. Now, assume that this `id` property changes within the HTML. With traditional Selenium, your test would break, resulting in a `NoSuchElementException`. But using e.g. the `RecheckDriver` as a drop-in replacement/wrapper of your normal driver, ***recheck-web*** magically finds the element and logs a warning such as:

```text
*************** recheck warning ***************
The HTML id attribute used for element identification changed from 'username' to 'user'.
retest identified the element based on the persisted Golden Master.
If you apply these changes to the Golden Master, your test de.retest.recheck.example.MyUnbreakableSeleniumTest will break.
Use `By.id("user")` or `By.retestId("username")` to update your test MyUnbreakableSeleniumTest.java:30.
```


### How can I use unbreakable Selenium with explicit calls to check?

In order to use "Unbreakable Selenium", you just need to wrap your usual driver in an `UnbreakableDriver` (drop-in replacement) and use `RecheckWebImpl` instead of `RecheckImpl`. The code would look like [so](https://github.com/retest/recheck-web/blob/master/src/test/java/de/retest/web/it/SimpleUnbreakableSeleniumShowcaseIT.java)):

```java
// Use the RecheckDriver as a wrapper for your usual driver.
driver = new UnbreakableDriver( new ChromeDriver() );
// Use the unbreakable recheck implementation.
re = new RecheckWebImpl();
```

Note that this works only in conjunction with at least one previous call to `Recheck#check(...)`, as behind the scenes, if the element cannot be found on the current page, then ***recheck-web*** searches for it in the _last_ Golden Master (where e.g. the ID still is), makes a 1-on-1 assignment to the current elements and returns the element with the highest match, if it's higher than a configurable confidence level. Also check out the other [ways of using ***recheck-web***](https://docs.retest.de/recheck-web/ways-of-using-recheck-web/).


### Can I use recheck-web on a CI/CD server?

Yes, ***recheck-web*** easily integrates into your CI/CD environment. I can even be used with headless Selenium. Have a look at [our tests](https://github.com/retest/recheck-web/tree/master/src/test/java/de/retest/web/it) for various examples.


### How can I access the report files on my CI/CD server?

If you cannot easily the reports on your CI/CD server, test reports can be easily uploaded to [***rehub***](https://retest.de/rehub/). To upload reports, you will need a [retest account](https://sso.prod.cloud.retest.org/auth/realms/customer/account).

The first step is to modify the `setUp()` method in our existing test case to enable the upload to ***rehub***. There are two ways to achieve this:

- Set the `REHUB_REPORT_UPLOAD_ENABLED` system property (you have to do this _before_ `RecheckImpl` is created)

```java
@Before
void setUp() {
    System.setProperty( Properties.REHUB_REPORT_UPLOAD_ENABLED, "true" );
    re = new RecheckImpl();
    // ...
}
```

- Set the rehub flag via `RecheckOptions`

```java
@Before
void setUp() {
    RecheckOptions options = RecheckOptions.builder()
            .enableReportUpload()
            .build();
    re = new RecheckImpl( options );
    // ...
}
```

If we execute the test locally and the configuration was successful, your browser will pop up and you will be prompted to login. Afterwards, you can find your test reports on [***rehub*** dashboard](https://garkbit.prod.cloud.retest.org/dashboard).

A detailed tutorial can be found in our [documentation](https://docs.retest.de/recheck-web/tutorial/upload-test-reports-to-rehub/).


## License

This project is licensed under the [AGPL license](LICENSE).

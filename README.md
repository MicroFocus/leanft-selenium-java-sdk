# leanft-selenium-java-sdk

LeanFT for Selenium Java SDK

LeanFT for Selenium Java SDK extends the Selenium WebDriver API with locators and utilities that enable creating tests that are more robust, and reduces Selenium test automation and maintenance efforts.

## Install

```xml
<dependency>
  <groupId>com.microfocus.adm.leanft</groupId>
  <artifactId>leanft-selenium-java-sdk</artifactId>
  <version>1.0.1</version>
</dependency>
```

## Usage Example
LeanFT's By class extends the original Selenium By class. To use it,  import the following:

```java
import com.hpe.leanft.selenium.By;
import com.hpe.leanft.selenium.ByEach;
import com.hpe.leanft.selenium.Utils;
```

The following example demonstrates locating elements by their visible text.
```java
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.google.com");

        WebElement element = driver.findElement(By.visibleText("Google Search"));
        element.click();

        driver.quit();
```

The following example demonstrates locating elements using a regular expression.
```java
        WebDriver driver = new ChromeDriver();
        driver.get("http://www.google.com");

        WebElement element = driver.findElement(By.name(Pattern.compile("^btn")));
        element.click();

        driver.quit(); 
```

Locate an element using its HTML attributes and highlight it.

```java
       WebDriver driver = new ChromeDriver();
        driver.get("http://www.google.com");

        Map<String, String> attributes = new HashMap<>();
        attributes.put("class", "gsfi lst-d-f");
        attributes.put("id", "lst-ib");

        WebElement element = driver.findElement(By.attributes(attributes));
        element.click();

        driver.quit();
 ```

## API

LeanFT for Selenium API reference documentation can be found [here
](https://admhelp.microfocus.com/leanft/en/latest/HelpCenter/Content/S4Java_SDK/top-Selenium-Java.htm)

### New Locators

#### By.visibleText

Finds elements based on their visible text.

#### By.visible

Finds elements based on their visibility.

#### By.role

Finds elements based on their role.

#### By.type

Finds elements based on their type.

#### By.attributes

Finds elements based on their attributes (one or more). Attribute values can be defined using regular expressions.

#### By.styles

Finds elements based on their computed style (one or more). Computed style values can be defined using regular expressions.

#### By.each

Finds elements based on the combination of locators (attributes, tags, styles etc.).

### Regular Expression Support

All the locators which accept a string as a value of the element's property were extended to support regular expressions, including the following Selenium native locators:

* By.id
* By.className
* By.linkText
* By.name
* By.tagName

## Utilities

### Utils.getSnapshot

Returns a snapshot (image) of the selenium element as a Base64 string.

### Utils.highlight

Highlights the selenium element in the browser.


## build

To build the project, clone it and run the following command:

```
mvn clean install
```

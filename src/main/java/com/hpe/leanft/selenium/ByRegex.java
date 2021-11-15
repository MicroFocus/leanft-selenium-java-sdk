/*! (c) Copyright 2015 – 2021 Micro Focus or one of its affiliates. */
//
// Licensed under the Apache License, Version 2.0 (the "License");
// You may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Apache License 2.0 - Apache Software Foundation
// www.apache.org
// Apache License Version 2.0, January 2004 http://www.apache.org/licenses/ TERMS AND CONDITIONS FOR USE, REPRODUCTION ...
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.hpe.leanft.selenium;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Provides a base class for all regular expression search requests.
 */
abstract class ByRegex extends By {
    protected final Pattern pattern;
    protected final String className;

    ByRegex(Pattern pattern, String className) {
        if (pattern == null || pattern.pattern() == null || pattern.pattern().length()==0){
            throw new IllegalArgumentException("Pattern cannot be null.");
        }
        this.pattern = pattern;
        this.className = className;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        List<WebElement> elementsByRegex;
        JavascriptExecutor executor = InternalUtils.getExecutor(context);

        // In case element is the context pass it to the method as the root
        // element to search into it
        if (context instanceof WebElement){
            elementsByRegex = findElementsByRegex(executor, (WebElement) context);
        } else {
            // In case the driver is the root element
            elementsByRegex = findElementsByRegex(executor, null);
        }

        // Set foundBy property of the WebElement.
        if ((elementsByRegex.size() > 0 ) && (elementsByRegex.get(0) instanceof RemoteWebElement)) {
            InternalUtils.setFoundBy(elementsByRegex, context, className, pattern.toString());
        }
        return elementsByRegex;
    }

    @Override
    public WebElement findElement(SearchContext context) {
        List<WebElement> elements = findElements(context);
        return elements.get(0);
    }

    /**
     * Finds all the elements that match pattern.pattern() global field.
     * @param executor The executor that runs runs the script in the browser.
     * @param element The Web element used as the root element from which to search.
     * @return A list of found web elements.
     */
    protected abstract List<WebElement> findElementsByRegex(JavascriptExecutor executor, WebElement element);

    @Override
    public String toString() {
        return className + ": \"" + pattern.pattern() + "\"" ;
    }

}
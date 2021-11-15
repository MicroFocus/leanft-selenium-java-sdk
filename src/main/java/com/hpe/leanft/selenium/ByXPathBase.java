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

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;
/**
 * Provides an internal base class for all xpath search requests.
 */
class ByXPathBase extends By {
    /**
     * This class is a base for all find by XPath search context.
     */
    private final String attributeName; // Can be type or role.
    private final String xPathExp;      // The expression to look for.

	/**
     * A constructor for the ByXPathBase locator.
     * @param attributeName The name of the attribute.
     * @param xPathExp The XPath expression.
     */
    ByXPathBase(String attributeName, String xPathExp) {
        this.attributeName = attributeName;
        this.xPathExp = xPathExp;
        if ((InternalUtils.isNullOrEmpty(xPathExp)) || (InternalUtils.isNullOrEmpty(attributeName))){
            throw new IllegalArgumentException("xPath expression cannot be null.");
        }
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return context.findElements(By.xpath(".//*[@" + attributeName + " = '" + xPathExp
                + "']"));
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return context.findElement(By.xpath(".//*[@" + attributeName + " = '" + xPathExp
                + "']"));
    }

    @Override
    public String toString() {
        return new StringBuilder().append("By.").append(attributeName).append(": ").append(xPathExp).toString();
    }
}
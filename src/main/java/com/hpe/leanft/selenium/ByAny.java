/*! (c) Copyright 2015 - 2018 Micro Focus or one of its affiliates. */
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

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A locator that locates elements according to any of the given locators (attributes, tags, styles etc.).
 */
public class ByAny extends By {
    private org.openqa.selenium.By[] bys;

    /**
     * A constructor for the ByAny locator.
     * @param bys The locators (Bys) by which the elements should be identified.
     */
    public ByAny(org.openqa.selenium.By... bys) {
        if (bys == null){
            throw new IllegalArgumentException("ByAny: The arguments list cannot be null.");
        }
        if (bys.length == 0) {
            throw new IllegalArgumentException("ByAny: The arguments list cannot be empty.");
        }

        this.bys = bys;
    }

    @Override
    public WebElement findElement(SearchContext context) {
        List<WebElement> elements = this.findElements(context);
        if(elements.isEmpty()) {
            throw new NoSuchElementException("ByAny: Cannot locate an element using " + this.toString());
        }

        return elements.get(0);
    }

    @Override
    public List<WebElement> findElements(SearchContext searchContext) {

        HashSet<WebElement> elems = new HashSet<>();

        for (org.openqa.selenium.By by : this.bys) {
            elems.addAll(by.findElements(searchContext));
        }

        return new ArrayList<>(elems);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("By.any(");
        stringBuilder.append("{");
        boolean first = true;
        org.openqa.selenium.By[] bys = this.bys;
        int bysLen = this.bys.length;

        for(int i = 0; i < bysLen; ++i) {
            org.openqa.selenium.By by = bys[i];
            stringBuilder.append(first?"":",").append(by);
            first = false;
        }

        stringBuilder.append("})");
        return stringBuilder.toString();
    }
}

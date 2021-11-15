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

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.Serializable;
import java.util.*;

/**
 * A locator that locates elements according to one or more locators (attributes, tags, styles etc.).
 */
public class ByEach extends org.openqa.selenium.By implements Serializable {
    private static final long serialVersionUID = 4573668832699497306L;
    private static String EXIST_IN_ALL_JS_FUNC = InternalUtils.getResourceFile("existInAll.js");
    private org.openqa.selenium.By[] bys;

    /**
     * A constructor for the ByEach locator.
     * @param bys The locators (Bys) by which the elements should be identified.
     */
    public ByEach(org.openqa.selenium.By... bys) {
        if (bys == null) {
            throw new IllegalArgumentException("ByEach: The arguments list cannot be null.");
        }
        if (bys.length == 0) {
            throw new IllegalArgumentException("ByEach: The arguments list cannot be empty.");
        }
        this.bys = bys;
    }

    @Override
    public WebElement findElement(SearchContext context) {
        List<WebElement> elements = findElements(context);
        if (elements.isEmpty()) {
            throw new NoSuchElementException("ByEach: Cannot locate an element using " + toString());
        }

        return elements.get(0);
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        if(bys.length == 1){
            return bys[0].findElements(context);
        }

        List<List<WebElement>> listOfByLists = getElementsOfMultipleBys(context, bys);

        sortAscending(listOfByLists);

        RemoteWebDriver executor = InternalUtils.getDriver(context);

        if (executor!= null && executor.getCapabilities().getBrowserName().equals("firefox")) {
            List<List<WebElement>> elements = listOfByLists.subList(1, listOfByLists.size());
            Object result = executor.executeScript(EXIST_IN_ALL_JS_FUNC , listOfByLists.get(0), elements);
            try {

                List<WebElement> resultList = (List<WebElement>) result;
                if (resultList.size() == 0) {
                    throw new java.util.NoSuchElementException("No element match all this Bys found.");
                }

                return resultList;
            } catch (Exception e) {
                throw new java.util.NoSuchElementException("No element match all this Bys found.");
            }

        }

        Map<String, WebElement> matchedMap = insertIntoMap(listOfByLists.get(0));

        listOfByLists.set(0, null);

        for (int i = 1; i < listOfByLists.size(); i++) {
            matchedMap = existInBoth(listOfByLists.get(i), matchedMap, context);
            listOfByLists.set(i, null);
            if (matchedMap.size() == 0) {
                throw new java.util.NoSuchElementException("No element match all this Bys found.");
            }
        }

        // Set foundBy property of the WebElement.
        // Take from this.toString the part needed for foundBy.
        String using = this.toString().substring(this.toString().indexOf("(")+1, this.toString().length()-1);

        Map.Entry<String, WebElement> entry = matchedMap.entrySet().iterator().next();
        if ((matchedMap.size() > 0 ) && (entry.getValue() instanceof RemoteWebElement)) {
            List<WebElement> lwe = new ArrayList<>(matchedMap.size());
            lwe.addAll(matchedMap.values());

            InternalUtils.setFoundBy(lwe, context, "By.each", using);
        }

        return new ArrayList<>(matchedMap.values());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("By.each(");
        stringBuilder.append("{");

        boolean first = true;
        for (org.openqa.selenium.By by : bys) {
            stringBuilder.append((first ? "" : ",")).append(by);
            first = false;
        }
        stringBuilder.append("})");
        return stringBuilder.toString();
    }

    /**
     * Inserts a list of elements into the HashMap.
     *
     * @param list
     * @return the HashMap for which the key is the ID of the WebElement, and for which the value is the WebElement itself.
     */
    private static Map<String, WebElement> insertIntoMap(List<WebElement> list) {

        Map<String, WebElement> map = new HashMap<String, WebElement>();
        for (WebElement element : list) {
            map.put(((RemoteWebElement) element).getId(), element);
        }
        return map;
    }

    /**
     * Checks if any element of the 'list' exists in 'matchedMap'.
     *
     * @return the HashMap that contains only the values that exist in both 'list' and 'matchedMap'.
     */
    private static Map<String, WebElement> existInBoth(List<WebElement> list, Map<String, WebElement> matchedMap, SearchContext context) {

        Map<String, WebElement> matchedWithCurrentAlsoMap = new HashMap<String, WebElement>();
        for (WebElement element : list) {
            if (matchedMap.containsKey(((RemoteWebElement) element).getId())) {
                matchedWithCurrentAlsoMap.put(((RemoteWebElement) element).getId(), element);
            }
        }
        return matchedWithCurrentAlsoMap;
    }

    private static List<List<WebElement>> sortAscending(List<List<WebElement>> lists) {

        /**
         * Compares the two arguments for order.  Returns a negative integer,
         * zero, or a positive integer when the first argument is less than, equal
         * to, or greater than the second, respectively.<p>
         */
        Collections.sort(lists, new Comparator<List<WebElement>>() {
            @Override
            public int compare(List<WebElement> o1, List<WebElement> o2) {
                if (o1.size() > o2.size()) {
                    return -1;
                }
                if (o1.size() < o2.size()) {
                    return 1;
                }

                return 0;
            }
        });
        return lists;
    }

    /**
     *  Finds the elements of all the 'By's.
     *
     *  @return an array of arrays that contain the results of all the findElements of all the 'By's
     */
    private static List<List<WebElement>> getElementsOfMultipleBys(SearchContext context, org.openqa.selenium.By[] bys) {
        List<List<WebElement>> lists = new ArrayList<List<WebElement>>();

        for (org.openqa.selenium.By by : bys) {
            lists.add(by.findElements(context));
        }

        return lists;
    }
}
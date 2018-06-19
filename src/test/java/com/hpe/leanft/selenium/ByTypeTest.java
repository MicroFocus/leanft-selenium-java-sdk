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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.internal.FindsByXPath;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ByTypeTest {
    private ByXPathBase _byStrategyUnderTest;

    @Before
    public void setUp() {
        _byStrategyUnderTest = new ByXPathBase("type", "some string");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_ShouldThrowExceptionInCaseConstructedWithNullType() {
        _byStrategyUnderTest = new ByXPathBase(null, "some string");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_ShouldThrowExceptionInCaseConstructedWithNullExp() {
        _byStrategyUnderTest = new ByXPathBase("some string", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_ShouldThrowExceptionInCaseConstructedWithEmpty() {
        _byStrategyUnderTest = new ByXPathBase("", "some string");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_ShouldThrowExceptionInCaseConstructedWithWhiteSpace() {
        _byStrategyUnderTest = new ByXPathBase("  ", "some string");
    }

    @Test
    public void findElement_ShouldConstructXpathUsingType() {
        FindsByXPath contextMock = Mockito.mock(FindsByXPath.class,
                Mockito.withSettings().extraInterfaces(SearchContext.class));

        _byStrategyUnderTest.findElement((SearchContext) contextMock);

        verify(contextMock, times(1)).findElementByXPath(".//*[@type = 'some string']");
    }

    @Test
    public void findElements_ShouldConstrucXpathUsingText() {
        FindsByXPath contextMoxk = Mockito.mock(FindsByXPath.class,
                Mockito.withSettings().extraInterfaces(SearchContext.class));

        _byStrategyUnderTest.findElements((SearchContext) contextMoxk);

        verify(contextMoxk, times(1)).findElementsByXPath(".//*[@type = 'some string']");
    }

    @Test
    public void toString_ShouldReturnText() {
        _byStrategyUnderTest.toString();

        assertEquals("By.type: some string", _byStrategyUnderTest.toString());
    }
}
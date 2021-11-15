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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.regex.Pattern;

import static org.mockito.Mockito.*;

public class ByRegTextTest {
    private By.ByRegText _byLocatorUnderTest;

    @Before
    public void setUp() throws Exception {
        _byLocatorUnderTest = new By.ByRegText(Pattern.compile("ab.*"), "ByRegText");
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_ShouldThrowExceptionInCaseOfNull() {
        _byLocatorUnderTest = new By.ByRegText(null, "");
    }

    @Test(expected = NullPointerException.class)
    public void constructor_ShouldThrowExceptionInCaseOfNullPattern() {
        _byLocatorUnderTest = new By.ByRegText(Pattern.compile(null), "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructor_ShouldThrowExceptionInCaseOfEmptyPattern() {
        _byLocatorUnderTest = new By.ByRegText(Pattern.compile(""), "");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findElements_ShouldThrowExceptionInCaseOfNullSearchContextArgument() {
        _byLocatorUnderTest.findElements(null);
    }


    @Test(expected = NoSuchElementException.class)
    public void findElements_ShouldThrowExceptionInCaseOfNoMatchedRegexElementFound() {
        RemoteWebDriver remoteWebDriverMock = Mockito.mock(RemoteWebDriver.class,
                Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
        when(remoteWebDriverMock.executeScript(anyString(), any())).thenReturn(null);
        _byLocatorUnderTest.findElements(remoteWebDriverMock);
    }

    @Test(expected = NoSuchElementException.class)
    public void findElement_ShouldThrowExceptionInCaseOfNoMatchedRegexElementFound() {
        RemoteWebDriver remoteWebDriverMock = Mockito.mock(RemoteWebDriver.class,
                Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
        when(remoteWebDriverMock.executeScript(anyString(), any())).thenReturn(null);
        _byLocatorUnderTest.findElement(remoteWebDriverMock);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findElement_ShouldThrowExceptionInCaseOfNullSearchContextArgument() {
        _byLocatorUnderTest.findElement(null);
    }
}
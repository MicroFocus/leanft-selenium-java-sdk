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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class ByAnyTest {
	private ByAny _byStrategyUnderTest;

	@Before
	public void setUp() {
	}

	@Test(expected = IllegalArgumentException.class)
	public void byAny_ShouldThrowExceptionInCaseCallWithNullParameter() {
		new ByAny(null);
	}

	@Test
	public void findElements_ShouldCallAllBys() {
		SearchContext contextMock = Mockito.mock(SearchContext.class);

		RemoteWebElement webElement = Mockito.mock(RemoteWebElement.class);
		By by1 = Mockito.mock(By.class);
		By by2 = Mockito.mock(By.class);
		By by3 = Mockito.mock(By.class);

		_byStrategyUnderTest = new ByAny(by1, by2, by3);
		List<WebElement> byMockEle = new ArrayList<>();
		byMockEle.add(webElement);
		byMockEle.add(webElement);

		when(by1.findElements(contextMock)).thenReturn(byMockEle);
		when(by2.findElements(contextMock)).thenReturn(byMockEle);
		when(by3.findElements(contextMock)).thenReturn(byMockEle);

		_byStrategyUnderTest.findElements(contextMock);
		verify(by1, times(1)).findElements(contextMock);
		verify(by2, times(1)).findElements(contextMock);
		verify(by3, times(1)).findElements(contextMock);
	}

	@Test
	public void findElements_ShouldNotReturnDuplication() {
		SearchContext contextMock = Mockito.mock(SearchContext.class);

		By by1 = Mockito.mock(By.class);
		RemoteWebElement webElement = Mockito.mock(RemoteWebElement.class);

		_byStrategyUnderTest = new ByAny(by1);

		List<WebElement> byMockEle = new ArrayList<>();
		byMockEle.add(webElement);
		byMockEle.add(webElement);

		when(webElement.getId()).thenReturn(anyString());
		when(by1.findElements(contextMock)).thenReturn(byMockEle);
		_byStrategyUnderTest.findElements(contextMock);

		// Check there is no duplication in the return list.
		Assert.assertTrue((_byStrategyUnderTest.findElements(contextMock).size() == 1));
	}

	@Test
	public void toString_ShouldReturnToStringOfPassedBys() {
		By by1 = Mockito.mock(By.class);
		when(by1.toString()).thenReturn("some string");
		_byStrategyUnderTest = new ByAny(by1);
		_byStrategyUnderTest.toString();

		assertEquals("By.any({some string})", _byStrategyUnderTest.toString());
	}
}
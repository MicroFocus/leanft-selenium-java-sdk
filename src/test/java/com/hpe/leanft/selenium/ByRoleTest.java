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

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.SearchContext;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ByRoleTest {
	private ByXPathBase _byStrategyUnderTest;

	@Before
	public void setUp() {
		_byStrategyUnderTest = new ByXPathBase("role", "some string");
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
		_byStrategyUnderTest = new ByXPathBase("", "");
	}

	@Test
	public void findElement_ShouldConstructXpathUsingType() {
		SearchContext contextMoxk = Mockito.mock(SearchContext.class);

		_byStrategyUnderTest.findElement((SearchContext) contextMoxk);

		verify(contextMoxk, times(1)).findElement(By.xpath(".//*[@role = 'some string']"));
	}

	@Test
	public void findElements_ShouldConstructXpathUsingText() {
		SearchContext contextMoxk = Mockito.mock(SearchContext.class);

		_byStrategyUnderTest.findElements((SearchContext) contextMoxk);

		verify(contextMoxk, times(1)).findElements(By.xpath(".//*[@role = 'some string']"));
	}

	@Test
	public void toString_ShouldReturnText() {
		_byStrategyUnderTest.toString();

		assertEquals("By.role: some string", _byStrategyUnderTest.toString());
	}
}
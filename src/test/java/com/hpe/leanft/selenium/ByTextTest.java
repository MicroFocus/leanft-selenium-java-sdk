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

import static org.junit.Assert.assertEquals;

public class ByTextTest {
	private By.ByText _byStrategyUnderTest;

	@Before
	public void setUp() {
		_byStrategyUnderTest = (By.ByText) By.visibleText("some string");
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor_ShouldThrowExceptionInCaseConstructedWithNull() {
		_byStrategyUnderTest = (By.ByText) By.visibleText(null, By.FLAGS.CASE_INSENSITIVE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructor_ShouldThrowExceptionInCaseConstructedWithEmpty() {
		_byStrategyUnderTest = (By.ByText) By.visibleText("", By.FLAGS.CASE_INSENSITIVE);
	}

	@Test
	public void toString_ShouldReturnText() {
		_byStrategyUnderTest.toString();

		assertEquals("By.visibleText: \"some string\"", _byStrategyUnderTest.toString());
	}
}
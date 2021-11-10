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

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Pattern;

public class ByTest {
    @Test
    public void attribute_RegExp_ShouldReturnByRegExpAttributesObject() {
        By.ByAttributes byAttributes = (By.ByAttributes) By.attribute("a", Pattern.compile("^b"));
        Assert.assertEquals(1, byAttributes.attributes.size());
        Assert.assertEquals("^b", byAttributes.attributes.get("a").get("value"));
    }

    @Test
    public void style_StringValue_shouldReturnByStylesObjectWithString() {
        By.ByStyles byStyles = (By.ByStyles) By.style("a", "b");

        Assert.assertEquals(1, byStyles.styles.size());
        Assert.assertEquals("String", byStyles.styles.get("a").get("type"));
        Assert.assertEquals("b", byStyles.styles.get("a").get("value"));
    }

    @Test
    public void style_PatternValue_ShouldReturnByStylesObjectWithRegExp() {
        By.ByStyles byStyles = (By.ByStyles) By.style("a", Pattern.compile("^b"));

        Assert.assertEquals(1, byStyles.styles.size());
        Assert.assertEquals("RegExp", byStyles.styles.get("a").get("type"));
        Assert.assertEquals("^b", byStyles.styles.get("a").get("value"));
    }
}
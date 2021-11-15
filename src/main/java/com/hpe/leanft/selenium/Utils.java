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

import org.openqa.selenium.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

import static com.hpe.leanft.selenium.InternalUtils.isVisible;

/**
 * LeanFT for Selenium utilities.
 */
public class Utils {

    private static final String SNAPSHOT_JS_FUNC = InternalUtils.getResourceFile("snapshot.js");
    private static final String SCROLL_INTO_VIEW_FUNC = InternalUtils.getResourceFile("scrollIntoView.js");
    private static final String HIGHLIGHT_JS_FUNC = InternalUtils.getResourceFile("highlight.js");
    private static final long DEFAULT_HIGHLIHT_TIME_IN_MILISEC = 6000;
    private static final long EXTRA_WAIT_TIME_IN_MILISEC = 500;
    private final static String SCREENSHOT_PREPARE = "var rect = arguments[0].getBoundingClientRect();" +
            "return {left: rect.left,top:rect.top,width:rect.width,height:rect.height};";

    /**
     * Returns a snapshot (image) of the selenium element as a Base64 string.
     *
     * @param element The element to retrieve a snapshot for.
     * @return A snapshot of the element as a Base64 image.
     * @throws IOException Thrown in case the image failed to be read.
     */
    public static RenderedImage getSnapshot(WebElement element) throws IOException {
        if (element == null) {
            throw new IllegalArgumentException("Element can't be null.");
        }

        Map<String, Object> obj;
        WebDriver wrapsDriver = InternalUtils.getDriver(element);

        if (isVisible(element)) {
            obj = (Map<String, Object>) ((JavascriptExecutor) wrapsDriver).executeScript(SCREENSHOT_PREPARE, element);
        } else {
            obj = (Map<String, Object>) ((JavascriptExecutor) wrapsDriver).executeScript(SNAPSHOT_JS_FUNC, element);
        }

        byte[] screenshotAs = ((TakesScreenshot) wrapsDriver).getScreenshotAs(OutputType.BYTES);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(screenshotAs));

        Rectangle rect = new Rectangle(((Number) (obj.get("left"))).intValue(),
                ((Number) (obj.get("top"))).intValue(),
                ((Number) (obj.get("width"))).intValue(),
                ((Number) (obj.get("height"))).intValue());

        return bufferedImage.getSubimage(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Highlights the selenium element in the browser.
     *
     * @param element The web element to highlight.
     * @throws InterruptedException Thrown in case the thread is interrupted while waiting for the highlight to finish.
     */
    public static void highlight(WebElement element) throws InterruptedException {
        highlight(element, DEFAULT_HIGHLIHT_TIME_IN_MILISEC);
    }

    /**
     * Highlights the selenium elements in the browser for t milliseconds.
     *
     * @param element The web element to highlight.
     * @param t       The time (in milliseconds) that the element is highlighted.
     *                In case of a negative number, the method throws an IllegalArgumentException.
                      If time is less than 150, the element is not highlighted.
     * @throws InterruptedException Thrown in case the thread is interrupted while waiting for the highlight to finish.
     */
    public static void highlight(WebElement element, long t) throws InterruptedException {
        if (element == null) {
            throw new IllegalArgumentException("cannot highlight null object. ");
        }
        if (t < 0) {
            throw new IllegalArgumentException("The time parameter can't be negative. ");
        }
        if (t == 0)
            return;

        WebDriver wrapsDriver = InternalUtils.getDriver(element);

        JavascriptExecutor executor = (JavascriptExecutor) wrapsDriver;

        // Scroll into the view.
        if (!isVisible(element)) {
            executor.executeScript(SCROLL_INTO_VIEW_FUNC, element);
        }

        // Highlight the element.
        executor.executeScript(HIGHLIGHT_JS_FUNC, element, t);

        // Wait for the highlight to finish.
        Thread.sleep(t + EXTRA_WAIT_TIME_IN_MILISEC);
    }

    /**
     * Scrolls the page to make the web element visible.
     *
     * @param element The web element to scroll to.
     */
    public static void scrollIntoView(WebElement element) {
        if (element == null) {
            throw new IllegalArgumentException("Cannot scrollIntoView null object.");
        }

        WebDriver wrapsDriver = InternalUtils.getDriver(element);
        JavascriptExecutor executor = (JavascriptExecutor) wrapsDriver;

        // Scroll into the view.
        executor.executeScript(SCROLL_INTO_VIEW_FUNC, element);
    }
}

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
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.*;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Window;
import org.openqa.selenium.remote.RemoteWebDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class UtilsTest {
	private static final String BYTES_FILE_NAME = "/bytes";

	@Test(expected = IllegalArgumentException.class)
	public void getSnapshot_ShouldThrowExceptionInCaseCallWithNull() throws Exception {
		Utils.getSnapshot(null);
	}

	@Test
	public void getSnapshot_ShouldReturnElementImage() throws Exception {
		WebElement webElement = mock(WebElement.class, withSettings().extraInterfaces(WrapsDriver.class));
		RemoteWebDriver wrapsDriverMock = mock(RemoteWebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class, TakesScreenshot.class));
		when(((WrapsDriver) webElement).getWrappedDriver()).thenReturn(wrapsDriverMock);

		final Map<String, Object> rectMap = new HashMap<>();
		rectMap.put("left", 538);
		rectMap.put("top", 1031);
		rectMap.put("width", 165);
		rectMap.put("height", 18);

		when(((JavascriptExecutor) wrapsDriverMock).executeScript(anyString(), anyObject())).thenReturn(rectMap);

		InputStream bytesInputStream = UtilsTest.class.getResourceAsStream(BYTES_FILE_NAME);

		byte[] screenshotAs = readBytes(bytesInputStream);

		when(((TakesScreenshot) wrapsDriverMock).getScreenshotAs(OutputType.BYTES)).thenReturn(screenshotAs);

		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(screenshotAs));

		Rectangle rect = new Rectangle(538, 1031, 165, 18);
		BufferedImage expectedImage = bufferedImage.getSubimage(rect.x, rect.y, rect.width, rect.height);

		Dimension d = new Dimension(10, 10);
		Point eleL = new Point(5, 5);

		Options manage = mock(Options.class);

		when(wrapsDriverMock.manage()).thenReturn(manage);
		Window window = mock(Window.class);

		when(manage.window()).thenReturn(window);
		when(window.getSize()).thenReturn(d);

		when(webElement.getSize()).thenReturn(d);
		when(webElement.getLocation()).thenReturn(eleL);

		RenderedImage destImage = Utils.getSnapshot(webElement);

		verify(((JavascriptExecutor) wrapsDriverMock), times(1)).executeScript(anyString(), anyObject());

		Assert.assertTrue(equals(expectedImage, destImage));

	}

	private byte[] readBytes(InputStream stream) throws IOException {
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			int numRead;
			while ((numRead = stream.read(buffer)) > -1) {
				output.write(buffer, 0, numRead);
			}
		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
		} finally {
			stream.close();
		}

		output.flush();
		return output.toByteArray();
	}


	@Test(expected = IllegalArgumentException.class)
	public void highlight_ShouldThrowExceptionInCaseCallWithNull() throws InterruptedException {
		Utils.highlight(null, 1000);
	}

	@Test(expected = IllegalArgumentException.class)
	public void highlight_ShouldThrowExceptionInCaseCallWithNegativeTimeParameter() throws InterruptedException {
		Utils.highlight(null, -1000);
	}

	@Test
	public void highlight_WithOneParamShouldRunScript() throws InterruptedException {
		WebElement webElement = mock(WebElement.class, withSettings().extraInterfaces(WrapsDriver.class));
		RemoteWebDriver wrapsDriverMock = mock(RemoteWebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class, TakesScreenshot.class));
		when(((WrapsDriver) webElement).getWrappedDriver()).thenReturn(wrapsDriverMock);

		Options manage = mock(Options.class);

		when(wrapsDriverMock.manage()).thenReturn(manage);
		Window window = mock(Window.class);

		when(manage.window()).thenReturn(window);
		Dimension d = new Dimension(10, 10);
		when(window.getSize()).thenReturn(d);
		Point eleL = new Point(5, 5);
		when(webElement.getSize()).thenReturn(d);
		when(webElement.getLocation()).thenReturn(eleL);

		Utils.highlight(webElement);

		// Call js once.
		verify(((JavascriptExecutor) wrapsDriverMock), times(1)).executeScript(anyString(), anyLong());
	}

	@Test
	public void highlight_DefaultTimeoutForHighlightShouldBe6Seconds() throws InterruptedException {
		WebElement webElement = mock(WebElement.class, withSettings().extraInterfaces(WrapsDriver.class));
		RemoteWebDriver wrapsDriverMock = mock(RemoteWebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class, TakesScreenshot.class));
		when(((WrapsDriver) webElement).getWrappedDriver()).thenReturn(wrapsDriverMock);

		Options manage = mock(Options.class);

		when(wrapsDriverMock.manage()).thenReturn(manage);

		Window window = mock(Window.class);

		when(manage.window()).thenReturn(window);
		Dimension wind = new Dimension(20, 20);
		Dimension d = new Dimension(10, 10);
		when(window.getSize()).thenReturn(wind);
		Point eleL = new Point(5, 5);
		when(webElement.getSize()).thenReturn(d);
		when(webElement.getLocation()).thenReturn(eleL);

		Utils.highlight(webElement);

		// Call js once.
		verify(((JavascriptExecutor) wrapsDriverMock), times(1)).executeScript(anyString(), anyString(), eq(new Long(6000)));
	}

	@Test
	public void highlight_HighlightWithTwoParamShouldRunScript() throws InterruptedException {
		WebElement webElement = mock(WebElement.class, withSettings().extraInterfaces(WrapsDriver.class));
		RemoteWebDriver wrapsDriverMock = mock(RemoteWebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class, TakesScreenshot.class));
		when(((WrapsDriver) webElement).getWrappedDriver()).thenReturn(wrapsDriverMock);

		Options manage = mock(Options.class);

		when(wrapsDriverMock.manage()).thenReturn(manage);
		Window window = mock(Window.class);

		when(manage.window()).thenReturn(window);
		Dimension d = new Dimension(10, 10);
		when(window.getSize()).thenReturn(d);
		Point eleL = new Point(5, 5);
		when(webElement.getSize()).thenReturn(d);
		when(webElement.getLocation()).thenReturn(eleL);

		Utils.highlight(webElement, 6);

		// Call js once.
		verify(((JavascriptExecutor) wrapsDriverMock), times(1)).executeScript(anyString(), anyLong());
	}

	@Test
	public void highlight_HighlightWithNonVisibleElementShouldScrollIntoView() throws InterruptedException {
		WebElement webElement = mock(WebElement.class, withSettings().extraInterfaces(WrapsDriver.class));
		RemoteWebDriver wrapsDriverMock = mock(RemoteWebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class, TakesScreenshot.class));
		when(((WrapsDriver) webElement).getWrappedDriver()).thenReturn(wrapsDriverMock);

		Options manage = mock(Options.class);

		when(wrapsDriverMock.manage()).thenReturn(manage);

		Window window = mock(Window.class);

		when(manage.window()).thenReturn(window);

		Dimension d = new Dimension(10, 10);
		when(window.getSize()).thenReturn(d);
		Point eleL = new Point(5, 5);
		when(webElement.getSize()).thenReturn(d);
		when(webElement.getLocation()).thenReturn(eleL);

		Utils.highlight(webElement, 6);

		// Call js for scroll into view.
		verify(((JavascriptExecutor) wrapsDriverMock), times(1)).executeScript(anyString(), anyString());

	}

	@Test(expected = IllegalArgumentException.class)
	public void scrollIntoView_ShouldThrowExceptionInCaseCallWithNull() {
		Utils.scrollIntoView(null);
	}

	@Test
	public void scrollIntoView_ShouldNotScrollIfVisible() {
		WebElement webElement = mock(WebElement.class, withSettings().extraInterfaces(WrapsDriver.class));
		RemoteWebDriver wrapsDriverMock = mock(RemoteWebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class, TakesScreenshot.class));
		when(((WrapsDriver) webElement).getWrappedDriver()).thenReturn(wrapsDriverMock);

		// Send element visible - true.
		Dimension d = new Dimension(10, 10);
		Point eleL = new Point(5, 5);
		Options manage = mock(Options.class);

		when(wrapsDriverMock.manage()).thenReturn(manage);
		Window window = mock(Window.class);

		when(manage.window()).thenReturn(window);
		when(window.getSize()).thenReturn(d);

		when(webElement.getSize()).thenReturn(d);
		when(webElement.getLocation()).thenReturn(eleL);

		Utils.scrollIntoView(webElement);

		// Call js zero times.
		verify(((JavascriptExecutor) wrapsDriverMock), times(0)).executeScript(anyString());
	}

	@Test
	public void scrollIntoView_ShouldScrollIfNotVisible() {
		WebElement webElement = mock(WebElement.class, withSettings().extraInterfaces(WrapsDriver.class));
		RemoteWebDriver wrapsDriverMock = mock(RemoteWebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class, TakesScreenshot.class));
		when(((WrapsDriver) webElement).getWrappedDriver()).thenReturn(wrapsDriverMock);

		// Send element not visible (false).
		Dimension d = new Dimension(10, 10);
		Point eleL = new Point(105, 105);
		Options manage = mock(Options.class);

		when(wrapsDriverMock.manage()).thenReturn(manage);
		Window window = mock(Window.class);

		when(manage.window()).thenReturn(window);
		when(window.getSize()).thenReturn(d);

		when(webElement.getSize()).thenReturn(d);
		when(webElement.getLocation()).thenReturn(eleL);

		Utils.scrollIntoView(webElement);

		// Call js once.
		verify(((JavascriptExecutor) wrapsDriverMock), times(1)).executeScript(anyString(), anyObject());
	}

	private static boolean sameRasterData(Raster r1, Raster r2) {
		for (int i = 0; i < r1.getNumBands(); i++) {
			if (!sameRasterBand(r1, r2, i)) return false;
		}
		return true;
	}

	private static boolean sameRasterBand(Raster r1, Raster r2, int band) {
		for (int xIdx = 0; xIdx < r1.getWidth(); xIdx++) {
			for (int yIdx = 0; yIdx < r1.getHeight(); yIdx++) {
				if (r1.getSampleDouble(xIdx + r1.getMinX(), yIdx + r1.getMinY(), band) !=
						r2.getSampleDouble(xIdx + r2.getMinX(), yIdx + r2.getMinY(), band)) {
					return false;
				}
			}
		}
		return true;
	}

	private static boolean equals(RenderedImage img1, RenderedImage img2) {
		if (img1 == img2) return true;
		if (img1 == null || img2 == null) return false;
		Raster r1 = img1.getData();
		Raster r2 = img2.getData();

		if (!r2.getBounds().equals(r1.getBounds())) return false;

		if (!(r1.getNumBands() == r2.getNumBands())) return false;
		if (!sameRasterData(r1, r2)) return false;

		return true;
	}
}
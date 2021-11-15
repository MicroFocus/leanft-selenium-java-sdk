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

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Contains all LeanFT for Selenium By methods for locating elements.
 */
public abstract class By extends org.openqa.selenium.By {
	/**
	 * Regular expression flags.
	 */
	public enum FLAGS {
		CASE_INSENSITIVE("2");
		String flag;

		FLAGS(String s) {
			flag = s;
		}
	}

	/**
	 * Returns a locator that locates elements by the provided regular expression name parameter.
	 *
	 * @param name The name of the elements in the form of a regular expression.
	 * @return A locator that locates elements with the given name.
	 */
	public static By name(final Pattern name) {
		if ((name == null || name.pattern() == null) || (name.pattern().length() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when pattern is null.");
		}

		return new ByRegName(name, "ByRegName");
	}

	/**
	 * Returns a locator that locates elements by the provided regular expression ID parameter.
	 *
	 * @param id The ID of the elements in the form of a regular expression.
	 * @return A locator that locates elements with the given ID.
	 */
	public static By id(final Pattern id) {
		if ((id == null || id.pattern() == null) || (id.pattern().length() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when pattern is null.");
		}

		return new ByRegId(id, "ByRegId");
	}

	/**
	 * Returns a locator that locates elements by the provided regular expression className parameter.
	 *
	 * @param className The className of the elements in the form of a regular expression.
	 * @return A locator that locates elements with the given className.
	 */
	public static By className(final Pattern className) {
		if ((className == null || className.pattern() == null) || (className.pattern().length() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when pattern is null.");
		}

		return new ByRegClassName(className, "ByRegClassName");
	}

	/**
	 * Returns a locator that locates elements by the provided regular expression linkText parameter.
	 *
	 * @param linkText The linkText of the elements in the form of a regular expression.
	 * @return A locator that locates elements with the given linkText.
	 */
	public static By linkText(final Pattern linkText) {
		if ((linkText == null || linkText.pattern() == null) || (linkText.pattern().length() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when pattern is null.");
		}

		return new ByRegLinkText(linkText, "ByRegLinkText");
	}

	/**
	 * Returns a locator that locates elements by the provided regular expression tagName parameter.
	 *
	 * @param tagName The tagName of the elements in the form of a regular expression.
	 * @return A locator that locates elements with the given tagName.
	 */
	public static By tagName(final Pattern tagName) {
		if ((tagName == null || tagName.pattern() == null) || (tagName.pattern().length() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when pattern is null.");
		}

		return new ByRegTagName(tagName, "ByRegTagName");
	}

	/**
	 * Returns a locator that locates elements by the provided role parameter.
	 *
	 * @param role The role of the elements.
	 * @return A locator that locates elements with the given role.
	 */
	public static By role(final String role) {
		if ((role == null) || (role.length() == 0)) {
			throw new IllegalArgumentException("By.role: Cannot find elements when role is empty or null.");
		}

		return new ByXPathBase("role", role);
	}

	/**
	 * Returns a locator that locates elements by the provided regular expression role parameter.
	 *
	 * @param role The role of the elements in the form of a regular expression.
	 * @return A locator that locates elements with the given role.
	 */
	public static By role(final Pattern role) {
		if ((role == null || role.pattern() == null) || (role.pattern().length() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when pattern is null.");
		}

		return new ByRegRole(role, "ByRegRole");
	}

	/**
	 * Returns a locator that locates elements by the provided type parameter.
	 *
	 * @param type The type of the elements.
	 * @return A locator that locates elements with the given type.
	 */
	public static By type(final String type) {
		if (InternalUtils.isNullOrEmpty(type)) {
			throw new IllegalArgumentException("By.type: Cannot find elements when type is empty or null.");
		}

		return new ByXPathBase("type", type);
	}

	/**
	 * Returns a locator that locates elements by the provided regular expression type parameter.
	 *
	 * @param type The role of the elements in the form of a regular expression.
	 * @return A locator that locates elements with the given type.
	 */
	public static By type(final Pattern type) {
		if ((type == null || type.pattern() == null) || (type.pattern().length() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when pattern is null.");
		}

		return new ByRegType(type, "ByRegType");
	}

	/**
	 * Returns a locator that locates elements by the provided visibleText parameter.
	 *
	 * @param visibleText The visible text of the elements.
	 * @return A locator that locates elements by the visible text.
	 */
	public static By visibleText(final String visibleText) {
		if ((visibleText == null) || (visibleText.length() == 0)) {
			throw new IllegalArgumentException("By.visibleText: Cannot find elements when input is empty or null.");
		}

		return new ByText(Pattern.compile(visibleText));
	}

	/**
	 * Returns a locator that locates element by the provided regular expression visibleText parameter.
	 *
	 * @param visibleText The visible text of the elements in the form of a regular expression.
	 * @return A locator that locates elements by the visible text.
	 */
	public static By visibleText(final Pattern visibleText) {
		if ((visibleText == null || visibleText.pattern() == null) || (visibleText.pattern().length() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when pattern is null.");
		}

		return new ByRegText(visibleText, "ByRegText");
	}

	/**
	 * Returns a locator that locates elements by the provided regular expression visibleText that is case insensitive.
	 *
	 * @param visibleText The visible text of the elements in the form of a regular expression.
	 * @param flags       The flag CASE_INSENSITIVE indicating that the visible text is case insensitive.
	 * @return A locator that locates elements by the visible text with the given flag.
	 */
	public static By visibleText(final String visibleText, FLAGS flags) {
		if ((visibleText == null) || (visibleText.length() == 0)) {
			throw new IllegalArgumentException("By.visibleText: Cannot find elements when input is empty or null.");
		}

		return new ByText(Pattern.compile(visibleText, Integer.parseInt(flags.flag)));
	}

	/**
	 * Returns a locator that locates elements that are either visible or not, depending on the parameter passed.
	 *
	 * @param visible Whether the elements are visible.
	 * @return A locator that locates elements that are either visible or not, depending on the parameter passed.
	 */
	public static By visible(final boolean visible) {
		return new ByVisible(visible);
	}

	/**
	 * Returns a locator that locates elements according to one or more styles. You can also use regular expressions.
	 *
	 * @param styles Dictionary of &lt;key, value&gt;.
	 * @return a locator that locates elements by their styles.
	 */
	public static By styles(Map<String, ?> styles) {
		if ((styles == null) || (styles.size() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when arguments dictionary is null.");
		}

		return new ByStyles(styles);
	}

	/**
	 * Returns a locator that locates elements according to a single style.
	 *
	 * @param name  The name of the style.
	 * @param value The value of the style.
	 * @return a locator that locates elements by a style.
	 */
	public static By style(String name, String value) {
		Map<String, String> map = new HashMap<>();
		map.put(name, value);

		return styles(map);
	}

	/**
	 * Returns a locator that locates elements according to a single style.
	 *
	 * @param name  The name of the style.
	 * @param value The Pattern of the style.
	 * @return a locator that locates elements by a style.
	 */
	public static By style(String name, Pattern value) {
		Map<String, Pattern> map = new HashMap<>();
		map.put(name, value);

		return styles(map);
	}

	/**
	 * Returns a locator that locates elements according to one or more attributes. You can also use regular expressions.
	 *
	 * @param attributes Dictionary of &lt;key, value&gt;.
	 * @return a locator that locates elements by their attributes.
	 */
	public static org.openqa.selenium.By attributes(Map<String, ?> attributes) {
		if ((attributes == null) || (attributes.size() == 0)) {
			throw new IllegalArgumentException("Cannot find elements when arguments dictionary is null.");
		}

		return new ByAttributes(attributes);
	}

	/**
	 * Returns a locator that locates elements according to a single attribute.
	 *
	 * @param name  The name of the attribute.
	 * @param value The value of the attribute.
	 * @return a locator that locates elements by an attribute.
	 */
	public static org.openqa.selenium.By attribute(String name, String value) {
		Map<String, String> map = new HashMap<>();
		map.put(name, value);

		return attributes(map);
	}

	/**
	 * Returns a locator that locates elements according to a single attribute.
	 *
	 * @param name  The name of the attribute.
	 * @param value The Pattern of the attribute.
	 * @return a locator that locates elements by an attribute.
	 */
	public static org.openqa.selenium.By attribute(String name, Pattern value) {
		Map<String, Pattern> map = new HashMap<>();
		map.put(name, value);

		return attributes(map);
	}

	/**
	 * A locator that locates elements by the provided visible text.
	 */
	public static class ByText extends By implements Serializable {
		private static final long serialVersionUID = -1517587391453818111L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByVisibleText.js");
		private Pattern visibleText;

		/**
		 * A constructor for the ByText locator.
		 *
		 * @param visibleText The visible text of the element in the form of a regular expression.
		 */
		public ByText(Pattern visibleText) {
			if ((visibleText == null) || visibleText.pattern() == null || visibleText.pattern().length() == 0) {
				throw new IllegalArgumentException("Cannot find elements when the visibleText is null.");
			}

			this.visibleText = visibleText;
		}

		@Override
		@SuppressWarnings("unchecked")
		public List<WebElement> findElements(SearchContext context) {
			WebElement rootElement = null;
			List<WebElement> matchedElements;

			JavascriptExecutor executor = InternalUtils.getExecutor(context);

			if (context instanceof WebElement) {
				rootElement = (WebElement) context;
			}
			matchedElements = (List<WebElement>) executor.executeScript(
					jsGetElementsFunc,
					visibleText.pattern(),
					InternalUtils.flagsToString(visibleText),
					rootElement,
					1
			);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements matched this expression");
			}

			// Set foundBy property of the WebElement.
			if ((matchedElements.size() > 0) && (matchedElements.get(0) instanceof RemoteWebElement)) {
				InternalUtils.setFoundBy(matchedElements, context, "ByText", visibleText.toString());
			}

			return matchedElements;
		}

		@Override
		public WebElement findElement(SearchContext context) {
			List<WebElement> elements = findElements(context);

			return elements.get(0);
		}

		@Override
		public String toString() {
			return "By.visibleText: \"" + visibleText.pattern() + "\"";
		}
	}

	/**
	 * A locator that locates elements that are either visible or not, depending on the parameter passed.
	 */
	public static class ByVisible extends By implements Serializable {
		private static final long serialVersionUID = -1517587391453818111L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByVisible.js");
		private boolean visible;

		/**
		 * A constructor for the ByVisible locator.
		 *
		 * @param visible Whether the elements are visible.
		 */
		public ByVisible(boolean visible) {
			this.visible = visible;
		}

		@Override
		public List<WebElement> findElements(SearchContext context) {
			WebElement rootElement = null;
			List<WebElement> matchedElements;

			JavascriptExecutor executor = InternalUtils.getExecutor(context);

			if (context instanceof WebElement) {
				rootElement = (WebElement) context;
			}

			matchedElements = (List<WebElement>) executor.executeScript(jsGetElementsFunc, rootElement, visible);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements found");
			}

			// Set foundBy property of the WebElement.
			if ((matchedElements.size() > 0) && (matchedElements.get(0) instanceof RemoteWebElement)) {
				InternalUtils.setFoundBy(matchedElements, context, "ByVisible", visible ? "true" : "false");
			}

			return matchedElements;
		}

		@Override
		public WebElement findElement(SearchContext context) {
			return findElements(context).get(0);
		}

		@Override
		public String toString() {
			return "By.visible: \"" + visible + "\"";
		}

	}

	/**
	 * A locator that locates elements by the provided regular expression name parameter.
	 */
	public static class ByRegName extends ByRegex implements Serializable {
		private static final long serialVersionUID = 1L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByRegExp.js");

		/**
		 * A constructor for the ByRegName locator.
		 *
		 * @param pattern   The name of the element in the form of a regular expression.
		 * @param className The name of the class of the locator.
		 */
		public ByRegName(Pattern pattern, String className) {
			super(pattern, className);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected List<WebElement> findElementsByRegex(
				JavascriptExecutor executor, WebElement element) {
			List<WebElement> matchedElements;

			matchedElements = (List<WebElement>) executor.executeScript(
					jsGetElementsFunc,
					element,
					null,
					"name",
					pattern.pattern(),
					InternalUtils.flagsToString(pattern)

			);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements matched this expression.");
			}

			return matchedElements;
		}
	}

	/**
	 * A locator that locates elements by the provided regular expression ID parameter.
	 */
	public static class ByRegId extends ByRegex implements Serializable {
		private static final long serialVersionUID = 1L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByRegExp.js");

		/**
		 * A constructor for the ByRegId locator.
		 *
		 * @param pattern   The ID of the element in the form of a regular expression.
		 * @param className The name of the class of the locator.
		 */
		public ByRegId(Pattern pattern, String className) {
			super(pattern, className);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected List<WebElement> findElementsByRegex(
				JavascriptExecutor executor, WebElement element) {
			List<WebElement> matchedElements;

			matchedElements = (List<WebElement>) executor.executeScript(
					jsGetElementsFunc,
					element,
					null,
					"id",
					pattern.pattern(),
					InternalUtils.flagsToString(pattern)
			);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements matched this expression.");
			}

			return matchedElements;
		}

	}

	/**
	 * A locator that locates elements by the provided regular expression class name parameter.
	 */
	public static class ByRegClassName extends ByRegex implements Serializable {
		private static final long serialVersionUID = 1L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByRegExp.js");

		/**
		 * A constructor for the ByRegClassName locator.
		 *
		 * @param pattern   The class name of the element in the form of a regular expression.
		 * @param className The name of the class of the locator.
		 */
		public ByRegClassName(Pattern pattern, String className) {
			super(pattern, className);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected List<WebElement> findElementsByRegex(
				JavascriptExecutor executor, WebElement element) {
			List<WebElement> matchedElements;

			matchedElements = (List<WebElement>) executor.executeScript(
					jsGetElementsFunc,
					element,
					null,
					"className",
					pattern.pattern(),
					InternalUtils.flagsToString(pattern)
			);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements matched this expression.");
			}

			return matchedElements;
		}
	}

	/**
	 * A locator that locates elements by the provided regular expression link text parameter.
	 */
	public static class ByRegLinkText extends ByRegex implements Serializable {
		private static final long serialVersionUID = 1L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByRegExp.js");

		/**
		 * A constructor for the ByRegLinkText locator.
		 *
		 * @param pattern   The link text of the element in the form of a regular expression.
		 * @param className The name of the class of the locator.
		 */
		public ByRegLinkText(Pattern pattern, String className) {
			super(pattern, className);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected List<WebElement> findElementsByRegex(
				JavascriptExecutor executor, WebElement element) {
			List<WebElement> matchedElements;

			matchedElements = (List<WebElement>) executor.executeScript(
					jsGetElementsFunc,
					element,
					"a",
					"textContent",
					pattern.pattern(),
					InternalUtils.flagsToString(pattern)
			);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements matched this expression.");
			}
			return matchedElements;
		}
	}

	/**
	 * A locator that locates elements by the provided regular expression visible text parameter.
	 */
	public static class ByRegText extends ByRegex implements Serializable {
		private static final long serialVersionUID = 1L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByVisibleText.js");

		/**
		 * A constructor for the ByRegText locator.
		 *
		 * @param pattern   The visible text of the element in the form of a regular expression.
		 * @param className The name of the class of the locator.
		 */
		public ByRegText(Pattern pattern, String className) {
			super(pattern, className);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected List<WebElement> findElementsByRegex(
				JavascriptExecutor executor, WebElement element) {

			List<WebElement> matchedElements;

			matchedElements = (List<WebElement>) executor.executeScript(
					jsGetElementsFunc,
					pattern.pattern(),
					InternalUtils.flagsToString(pattern),
					element
			);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements matched this expression.");
			}

			return matchedElements;
		}
	}

	/**
	 * A locator that locates elements by the provided regular expression tag name parameter.
	 */
	public static class ByRegTagName extends ByRegex implements Serializable {
		private static final long serialVersionUID = 1L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByRegExp.js");

		/**
		 * A constructor for the ByRegTagName locator.
		 *
		 * @param pattern   The tag name of the element in the form of a regular expression.
		 * @param className The name of the class of the locator.
		 */
		public ByRegTagName(Pattern pattern, String className) {
			super(pattern, className);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected List<WebElement> findElementsByRegex(
				JavascriptExecutor executor, WebElement element) {
			List<WebElement> matchedElements;

			matchedElements = (List<WebElement>) executor.executeScript(
					jsGetElementsFunc,
					element,
					null,
					"tagName",
					pattern.pattern(),
					"i"
			);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements matched this expression.");
			}

			return matchedElements;
		}
	}

	/**
	 * A locator that locates elements by the provided regular expression role parameter.
	 */
	public static class ByRegRole extends ByRegex implements Serializable {
		private static final long serialVersionUID = 1L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByRegExp.js");

		/**
		 * A constructor for the ByRegRole locator.
		 *
		 * @param pattern   The role of the element in the form of a regular expression.
		 * @param className The name of the class of the locator.
		 */
		public ByRegRole(Pattern pattern, String className) {
			super(pattern, className);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected List<WebElement> findElementsByRegex(
				JavascriptExecutor executor, WebElement element) {
			List<WebElement> matchedElements;

			matchedElements = (List<WebElement>) executor.executeScript(
					jsGetElementsFunc,
					element,
					null,
					"role",
					pattern.pattern(),
					InternalUtils.flagsToString(pattern)
			);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements matched this expression.");
			}

			return matchedElements;
		}
	}

	/**
	 * A locator that locates elements by the provided regular expression type parameter.
	 */
	public static class ByRegType extends ByRegex implements Serializable {
		private static final long serialVersionUID = 1L;
		private static final String jsGetElementsFunc = InternalUtils.getResourceFile("getElementsByRegExp.js");

		/**
		 * A constructor for the ByRegType locator.
		 *
		 * @param pattern   The type of the element in the form of a regular expression.
		 * @param className The name of the class of the locator.
		 */
		public ByRegType(Pattern pattern, String className) {
			super(pattern, className);
		}

		@Override
		@SuppressWarnings("unchecked")
		protected List<WebElement> findElementsByRegex(
				JavascriptExecutor executor, WebElement element) {
			List<WebElement> matchedElements;

			matchedElements = (List<WebElement>) executor.executeScript(
					jsGetElementsFunc,
					element,
					null,
					"type",
					pattern.pattern(),
					InternalUtils.flagsToString(pattern)
			);

			if (matchedElements == null || matchedElements.size() == 0) {
				throw new NoSuchElementException("No elements matched this expression.");
			}

			return matchedElements;
		}
	}

	/**
	 * A locator that locates elements by the provided styles (string or regular expressions).
	 */
	public static class ByStyles extends By {
		private static final String jsFindStyleFunc = InternalUtils.getResourceFile("getElementsByStyles.js");
		Map<String, Map<String, String>> styles = new HashMap<>();

		/**
		 * A constructor for the ByStyles locator.
		 *
		 * @param styles One or more styles (string or regular expressions).
		 */
		public ByStyles(Map<String, ?> styles) {
			if ((styles == null) || (styles.size() == 0)) {
				throw new IllegalArgumentException("Cannot find elements when arguments dictionary is null.");
			}

			// We pass both the type and the value to the script that is executed in the browser,
			// so the browser script will know how to work with the received string (string or regular expression).
			for (Map.Entry<String, ?> styleEntry : styles.entrySet()) {
				Map<String, String> styleValue = new HashMap<>();
				styleValue.put("type", styleEntry.getValue() instanceof Pattern ? "RegExp" : "String");
				styleValue.put("value", styleEntry.getValue().toString());
				this.styles.put(styleEntry.getKey(), styleValue);
			}
		}

		@Override
		public List<WebElement> findElements(SearchContext searchContext) {
			JavascriptExecutor executor = InternalUtils.getExecutor(searchContext);
			WebElement rootElement = null;
			if (searchContext instanceof WebElement) {
				rootElement = (WebElement) searchContext;
			}

			List<WebElement> webElements = (List<WebElement>) executor.executeScript(
					jsFindStyleFunc,
					rootElement,
					styles
			);

			// Set foundBy property of the WebElement.
			if ((webElements.size() > 0) && (webElements.get(0) instanceof RemoteWebElement)) {
				InternalUtils.setFoundBy(webElements, searchContext, "ByStyles", styles.toString());
			}

			return webElements;
		}

		@Override
		public String toString() {
			return "ByStyles";
		}
	}

	/**
	 * A locator that locates elements by the provided attributes (strings or regular expressions).
	 */
	public static class ByAttributes extends By {
		private static final String jsFindRegExpAttributesFunc = InternalUtils.getResourceFile("getElementsByAttributes.js");
		Map<String, Map<String, String>> attributes = new HashMap<>();

		/**
		 * A constructor for the ByAttributes locator.
		 *
		 * @param attributes One or more attributes (strings or regular expressions).
		 */
		public ByAttributes(Map<String, ?> attributes) {
			if ((attributes == null) || (attributes.size() == 0)) {
				throw new IllegalArgumentException(
						"Cannot find elements when arguments dictionary is null.");
			}

			// We pass both the type and the value to the script that is executed in the browser,
			// so the browser script will know how to work with the received string (string or regular expression).
			for (String key : attributes.keySet()) {
				Map<String, String> attributeValue = new HashMap<>();
				attributeValue.put("type", attributes.get(key) instanceof Pattern ? "RegExp" : "String");
				attributeValue.put("value", attributes.get(key).toString());
				this.attributes.put(key, attributeValue);
			}
		}

		@Override
		public List<WebElement> findElements(SearchContext searchContext) {
			JavascriptExecutor executor = InternalUtils.getExecutor(searchContext);
			WebElement rootElement = null;
			if (searchContext instanceof WebElement) {
				rootElement = (WebElement) searchContext;
			}

			List<WebElement> webElements = (List<WebElement>) executor.executeScript(
					jsFindRegExpAttributesFunc,
					rootElement,
					attributes
			);

			// Set foundBy property of the WebElement.
			if ((webElements.size() > 0) && (webElements.get(0) instanceof RemoteWebElement)) {
				InternalUtils.setFoundBy(webElements, searchContext, "ByAttributes", attributes.toString());
			}

			return webElements;
		}

		@Override
		public String toString() {
			return "ByAttributes";
		}
	}
}
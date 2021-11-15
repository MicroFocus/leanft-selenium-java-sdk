/*! (c) Copyright 2015 - 2021 Micro Focus or one of its affiliates. */
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

var context = arguments[0] || document;
var candidates = context.getElementsByTagName("*");

// Finds elements with the received visibility.
function findElements(candidates, visible) {
    return Array.prototype.filter.call(candidates, function(element) {
        if (element.nodeType !== element.ELEMENT_NODE) {
            return false;
        }

        return isVisible(element) === visible;
    });
}

function isOneOfAncestorsInvisible (element) {
    var parent = element.parentNode;
    while (parent && parent.nodeType === parent.ELEMENT_NODE) {
        var parentVisible = isVisibleInternal(parent);
        if (!parentVisible)
            return true;

        parent = parent.parentNode;
    }

    return false;
}

function isVisibleInternal(element) {
    var rect = element.getBoundingClientRect();
    if (rect.width === 0 && rect.height === 0)
        return false;

    var style = window.getComputedStyle(element);
    return style.width !== 0 &&
        style.height !== 0 &&
        style.opacity !== 0 &&
        style.display !== 'none' &&
        style.visibility !== 'hidden';
}

function isVisible (element) {
    if (!isVisibleInternal(element))
        return false;

    var isInternetExplorer = false || !!document.documentMode;

    if (isInternetExplorer && isOneOfAncestorsInvisible(element))
        return false;

    return true;
}

return findElements(candidates, arguments[1]);
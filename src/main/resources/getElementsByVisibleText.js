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

var matchArr = [];

function findElements(elem, regex) {
    var resMatches = [];

    if (['SELECT', 'DATALIST', 'SCRIPT'].indexOf(elem.tagName) >= 0 || !isVisible(elem)) {
        return;
    }

    var children = elem.children || [];

    Array.prototype.forEach.call(children, function (child) {
        var childMatch = findElements(child, regex) || [];
        resMatches = resMatches.concat(childMatch);
    });

    if (resMatches.length === 0 && isMatch(elem, regex)) {
        return [elem];
    }

    return resMatches;
}

function isMatch(elem,regex) {
    elem = elem || "";
    var textHandlers = {
        "INPUT": function (elem) {
            switch (elem.type.toLowerCase()) {
                case "button":
                case "image":
                case "reset":
                case "submit":
                case "number":
                    return elem.value || "";
                case "radio":
                case "checkbox":
                    return findAttachedText(elem);
            }
        },
        "__DEFAULT__": function (elem) {
            return elem.innerText || elem.textContent;
        }
    };

    var textHandler = textHandlers[elem.tagName] || textHandlers['__DEFAULT__'];
    var text = textHandler(elem) || "";
    text = removeQuotes(text);
    return text.match(regex);
}

function findAttachedText(elem) {
    if (!elem.nextSibling || !elem.nextSibling.nodeType) {
        return;
    }

    if (elem.nextSibling.nodeType !== Node.TEXT_NODE) {
        return;
    }

    return elem.nextSibling.data;
}

function removeQuotes(str) {
    if (!(typeof str === 'string' || str instanceof String)) {
        return str;
    }

    if (str.length < 4 || str.substring(0, 1) != '\"' || str.substring(str.length - 2, str.length - 1) != '\"') {
        return str;
    }

    return str.substring(1, str.length - 2);
}

function isVisible(elem) {
    // Element not supported.
    if (elem.nodeType !== elem.ELEMENT_NODE) {
        return 1;
    }
    var style = window.getComputedStyle(elem);
    return style.width !== 0 &&
           style.height !== 0 &&
           style.opacity !== 0 &&
           style.display !== 'none' &&
           style.visibility !== 'hidden';
}

function quote (str) {
    return (str + '').replace(/[.?*+^$[\]\\(){}|-]/g, "\\$&");
}

function main(text,flags,root,nonRegex) {
    if (nonRegex) {
        text = quote(text);
        text = "^" + text + "$";
    }

    var regex = new RegExp(text, flags);

    var rootElem = root || document.documentElement;
    return findElements(rootElem, regex, 1);
}

return main(arguments[0], arguments[1], arguments[2], arguments[3]);
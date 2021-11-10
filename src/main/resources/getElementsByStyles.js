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

// Finds elements that match the given styles (string or regular expression).
function findElements(candidates, styles) {
    if (!styles)
        return;

    return Array.prototype.filter.call(candidates, function(element) {
        var computedStyle = window.getComputedStyle(element, null);

        return Object.keys(styles).every(function(styleKey) {
            // The SDK sends type of String or RegExp.
            if (styles[styleKey].type === "RegExp")
                return new RegExp(styles[styleKey].value).test(computedStyle[styleKey]);        
            return styles[styleKey].value === computedStyle[styleKey];
        });
    });
}

return findElements(candidates, arguments[1]);
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

function findElements(candidates, propName, regex, flags) {
    regex = new RegExp(regex, flags);

    return Array.prototype.filter.call(candidates, function(e) {
        var valuesToMatch = [e.getAttribute(propName) || e[propName]];

        if(propName === "className") {
            valuesToMatch =  e.hasAttribute("class") ? e.getAttribute("class").split(" ") : [];
        }

        return (Array.prototype.filter.call(valuesToMatch, function(val) {
            // Representation of empty RegExp.
            if (regex.source === "(?:)")
                return val && val === "";
                
            return val && val.match(regex);
        })).length > 0;
    });
}

function findLinks(e){
  return (e && e.tagName && e.tagName.toLowerCase() === "a") || (e && (e.role || "").toLowerCase() === "link");
}

//Arguments : context
//            tagsFilter
//            propName
//            regex
//            flags

var context = arguments[0] || document;
var tagsFilter = arguments[1];
var candidateFilter = (tagsFilter && tagsFilter.toLowerCase() === "a") ? findLinks : function(elem) { return true; };

var candidates = context.getElementsByTagName("*");
candidates = Array.prototype.filter.call(candidates, candidateFilter);

return findElements(candidates, arguments[2], arguments[3], arguments[4]);
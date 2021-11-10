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
       
// Determines the direction to scroll to bring the row into view.
function determineRowScrollDirection(parentPane, row) {
    var parentTop = parentPane.scrollTop;
    var parentBottom = parentTop + parentPane.clientHeight;

    var childTop = row.offsetTop;
    var childBottom = childTop + row.clientHeight;

    var direction = 0; // Row is already in view, no scrolling required.

    if (parentTop > childTop) {
        direction = -1; // Require scrolling up.
    }
    else if (parentBottom < childBottom) {
        direction = 1; // Require scrolling down.
    }

    return direction;
}

// Scroll the pane in the relevant direction to bring the row into view.
function scrollRowIntoView(parentPane, row, direction) {
    var distance = 0;
    if (direction != 0) {
        // scrollIntoView() is broken under ie8 so we need to do it this way.
        distance = calculateScrollTopDistance(row, direction);
        parentPane.scrollTop = distance;
    }
    else {
        // If scroll in top direction.
        if (direction == -1) {
            row.scrollIntoView(true);
        }
        else if (direction == 1) { // Scroll in bottom direction.
            row.scrollIntoView(false);
        }
    }
}

// Calculates the distance required to bring a row into view.
function calculateScrollTopDistance(parentPane, row, direction) {
    var distance = 0;
    if (direction === -1) // upwards scroll
    {
        distance = row.offsetTop;
    }
    else if (direction === 1) // downwards scroll
    {
        var paddingLength = parentPane.clientHeight - row.clientHeight;
        distance = row.offsetTop - paddingLength;
    }

    return distance;
}

function getImageElementForMapElement (elem) {
    var parent = elem;
    while (parent) {
        if (parent.tagName.toLowerCase() === "map") {
            break;
        }
        parent = parent.parentNode;
    }

    if (parent) {
        var name = parent.name;
        for (var i = 0; i < document.images.length; i++) {
            if (document.images[i].useMap.substring(1) === name) {
                return document.images[i];
            }
        }
    }
}

function calcRectForElement (elem) {
    if (elem.tagName.toLowerCase() === "map") {
        var imgElem = getImageElementForMapElement(elem);
        var rect = {
            left: 0,
            top: 0,
            right: 0,
            bottom: 0
        };

        if (!imgElem) {
            return rect;
        }

        return imgElem.getBoundingClientRect();
    }
    else if (elem.tagName.toLowerCase() === "area") {
        var imgElem = getImageElementForMapElement(elem);
        var rect = {
            left: 0,
            top: 0,
            right: 0,
            bottom: 0
        };

        if (!imgElem) {
            return rect;
        }

        var img_rect = imgElem.getBoundingClientRect();
        var coords = elem.coords.split(/\s*,\s*/);
        coords = coords.map(function (e) {
            return parseInt(e, 10);
        });

        switch (elem.shape.toLowerCase()) {
            case "rect":
                if (coords.length === 4) {
                    rect.left = coords[0];
                    rect.top = coords[1];
                    rect.right = coords[2];
                    rect.bottom = coords[3];
                }
                break;
            case "circle":
                if (coords.length === 3) {
                    var x = coords[0];
                    var y = coords[1];
                    var radius = coords[2];
                    if (radius < 0) {
                        break;
                    }

                    rect.left = x - radius;
                    rect.top = y - radius;
                    rect.right = x + radius;
                    rect.bottom = y + radius;
                }
                break;
            case "poly":
            case "polygon":
                if (coords.length >= 2) {
                    var x1, x2;
                    var y1, y2;
                    x1 = x2 = coords[0];
                    y1 = y2 = coords[1];
                    for (var i = 2; i < coords.length; i += 2) {
                        x1 = x1 < coords[i] ? x1 : coords[i];
                        x2 = x2 > coords[i] ? x2 : coords[i];
                        y1 = y1 < coords[i + 1] ? y1 : coords[i + 1];
                        y2 = y2 > coords[i + 1] ? y2 : coords[i + 1];
                    }
                    rect.left = x1;
                    rect.top = y1;
                    rect.right = x2;
                    rect.bottom = y2;
                }
                break;
        }

        var calcRect = {
            left: Math.round(img_rect.left + rect.left),
            top: Math.round(img_rect.top + rect.top),
            right: Math.round(img_rect.left + rect.right),
            bottom: Math.round(img_rect.top + rect.bottom)
        };

        calcRect.width = calcRect.right - calcRect.left;
        calcRect.height = calcRect.bottom - calcRect.top;
        return calcRect;
    }
    else {
        return elem.getBoundingClientRect();
    }
}

return (function(obj) {
    var isIE = false || !!document.documentMode;
        if (isIE) {
            var direction = determineRowScrollDirection(obj.parentNode, obj);
            scrollRowIntoView(obj.parentNode, obj, direction);
        } else
            obj.scrollIntoView(true);
        var rect = calcRectForElement(obj);
        return {
            left: rect.left, top: rect.top, width: rect.right - rect.left, height: rect.bottom - rect.top
        };
    }
).apply(undefined, arguments);
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

var obj = arguments[0];
var isIE = false || !!document.documentMode;

if (isIE){
    var direction = determineRowScrollDirection(obj.parentNode, obj);
    scrollRowIntoView(obj.parentNode, obj, direction);
}
else {
    obj.scrollIntoView(false);
}

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
    if (direction === -1) { // Upwards scroll.
        distance = row.offsetTop;
    }
    else if (direction === 1) { // Downwards scroll.
        var paddingLength = parentPane.clientHeight - row.clientHeight;
        distance = row.offsetTop - paddingLength;
    }

    return distance;
}
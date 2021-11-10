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

var Util = {
    extend: function (obj, properties) {
        if (properties) {
            Object.keys(properties).forEach(function (key) {
                obj[key] = properties[key];
            });
        }
        return obj;
    }
}

var ContentUtils = {
    ElementBorder: function (elem) {
        this.overlay = document.createElement('div');
        Util.extend(this.overlay.style, {
            position: 'absolute',
            display: 'none',
            borderColor: '#444444',
            borderStyle: 'solid',
            zIndex: '9999',
            boxSizing: 'content-box'
        });

        document.body.appendChild(this.overlay);

        if (elem)
            this.wrap(elem);
    },

    highlightElement: function (elem, t) {
        elem.scrollIntoView(false);
        var border = new this.ElementBorder(elem);

        var count = 0;

        t /= 150;
        // Flash t times ((t/2)*on + (t/2)*off)
        function drawRect() {
            if (++count > t) {
                border.remove();
                return;
            }

            if (count % 2)
                border.show();
            else
                border.hide();

            window.setTimeout(drawRect, 150);
        }
        drawRect();
    }
};

ContentUtils.ElementBorder.prototype = {
    overlay: null,
    elem: null,
    show: function () {
        this.overlay.style.display = 'block';
    },
    hide: function () {
        this.overlay.style.display = 'none';
    },
    // Returns whether the wrapped element was changed.
    wrap: function (elem) {
        if (this.elem === elem)
            return false;

        this.elem = elem;
        var border = 4;
        function pixels(n) { return n + 'px'; }
        var rect = this._calcRectForElement(elem);


        // In Safari for <OPTION> & <AREA> elements return an empty rectangle.
        // In this case we draw the rectangle around their parents.
        // Note that height & width == 0 is also wrong.
        if (!(rect.width || rect.height))
            rect = elem.parentElement.getBoundingClientRect();

        Util.extend(this.overlay.style, {
            border: pixels(border),
            top: pixels(rect.top + window.pageYOffset),
            left: pixels(rect.left + window.pageXOffset),
            // Width and height should not make the object bigger (which would add scroll bars for page).
            width: pixels(rect.width - (border * 2) ),
            height: pixels(rect.height - (border * 2))
        });

        // Setting the border resets the borderStyle.
        this.overlay.style.borderStyle = 'solid';
        this.show();

        return true;
    },

    remove: function () {
        if (this.overlay) {
            document.body.removeChild(this.overlay);
            this.elem = null;
            this.overlay = null;
        }
    },
    _calcRectForElement: function(elem){
        if (elem.tagName.toLowerCase() === "map" ){
            var imgElem = this._getImageElementForMapElement(elem);
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
        else if (elem.tagName.toLowerCase() === "area"){
            var imgElem = this._getImageElementForMapElement(elem);
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
            coords = coords.map(function(e) {
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

            calcRect.width = calcRect.right- calcRect.left;
            calcRect.height = calcRect.bottom- calcRect.top;
            return calcRect;
        }
        else {
            return elem.getBoundingClientRect();
        }
    },

    // This method returns the relevant image for the Map or Area tags.
    _getImageElementForMapElement: function (elem) {
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
}; // Of ContentUtils.ElementBorder.prototype.

ContentUtils.highlightElement(arguments[0], arguments[1]);
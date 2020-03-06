if (typeof String.prototype.trim !== 'function') {
    String.prototype.trim = function() {
        return this.replace(/^\s+|\s+$/g, '');
    }
}

var cssAttributes = [
    "align-content",
    "align-items",
    "align-self",
    "all",
    "animation-name",
    "animation-duration",
    "animation-timing-function",
    "animation-delay",
    "animation-iteration-count",
    "animation-direction",
    "animation-fill-mode",
    "animation-play-state",
    "backface-visibility",
    "background-attachment",
    "background-blend-mode",
    "background-clip",
    "background-color",
    "background-image",
    "background-origin",
    "background-position",
    "background-repeat",
    "background-size",
    "border-bottom-style",
    "border-bottom-color",
    "border-bottom-left-radius",
    "border-bottom-right-radius",
    "border-bottom-width",
    "border-collapse",
    "border-image-outset",
    "border-image-repeat",
    "border-image-slice",
    "border-image-source",
    "border-image-width",
    "border-left-color",
    "border-left-style",
    "border-left-width",
    "border-radius",
    "border-right-color",
    "border-right-style",
    "border-right-width",
    "border-spacing",
    "border-top-color",
    "border-top-left-radius",
    "border-top-right-radius",
    "border-top-style",
    "border-top-width",
    "bottom",
    "box-decoration-break",
    "box-shadow",
    "box-sizing",
    "break-after",
    "break-before",
    "break-inside",
    "caption-side",
    "caret-color",
    "clear",
    "clip",
    "color",
    "column-count",
    "column-fill",
    "column-gap",
    "column-rule-color",
    "column-rule-style",
    "column-rule-width",
    "column-span",
    "column-width",
    "content",
    "counter-increment",
    "counter-reset",
    "direction",
    "display",
    "empty-cells",
    "filter",
    "flex-basis",
    "flex-direction",
    "flex-grow",
    "flex-shrink",
    "flex-wrap",
    "float",
    "font-family",
    "font-size",
    "line-height",
    "text-align",
    "text-indent",
    "float",
    "font-kerning",
    "font-size",
    "font-size-adjust",
    "font-stretch",
    "font-style",
    "font-variant",
    "font-weight",
    "grid-auto-columns",
    "grid-auto-flow",
    "grid-auto-rows",
    "grid-column-end",
    "grid-column-start",
    "grid-row-end",
    "grid-row-start",
    "grid-template-areas",
    "grid-template-columns",
    "grid-template-rows",
    "grid-row-gap",
    "grid-column-gap",
    "hanging-punctuation",
    "hyphens",
    "isolation",
    "justify-content",
    "left",
    "letter-spacing",
    "line-height",
    "list-style-image",
    "list-style-position",
    "list-style-type",
    "margin-top",
    "margin-right",
    "margin-bottom",
    "margin-left",
    "max-height",
    "max-width",
    "min-height",
    "min-width",
    "mix-blend-mode",
    "object-fit",
    "object-position",
    "opacity",
    "order",
    "outline-color",
    "outline-offset",
    "outline-style",
    "outline-width",
    "overflow-x",
    "overflow-y",
    "offsetHeight",
    "offsetWidth",
    "padding-top",
    "padding-right",
    "padding-bottom",
    "padding-left",
    "page-break-after",
    "page-break-before",
    "page-break-inside",
    "perspective",
// "perspective-origin",
    "pointer-events",
    "position",
    "quotes",
    "resize",
    "right",
    "scroll-behavior",
    "tab-size",
    "table-layout",
    "text-align",
    "text-align-last",
    "text-decoration-color",
    "text-decoration-line",
    "text-decoration-style",
    "text-indent",
    "text-justify",
    "text-overflow",
    "text-shadow",
    "text-transform",
    "top",
    "transform",
// "transform-origin",
    "transform-style",
    "transition-delay",
    "transition-duration",
    "transition-property",
    "transition-timing-function",
    "unicode-bidi",
    "user-select",
    "vertical-align",
    "visibility",
    "white-space",
    "word-break",
    "word-spacing",
    "word-wrap",
    "z-index"
];

var ELEMENT_NODE = 1;
var TEXT_NODE = 3;
var DOCUMENT_TYPE_NODE = 10;

var Counter = /** @class */ (function () {
    function Counter() {
        this.map = {};
    }
    Counter.prototype.increase = function (element) {
        if (element.tagName in this.map) {
            this.map[element.tagName] = this.map[element.tagName] + 1;
        }
        else {
            this.map[element.tagName] = 1;
        }
        return this.map[element.tagName];
    };
    ;
    return Counter;
}());

function getText(node) {
    var firstNode = node.childNodes[0];
    if (firstNode && firstNode.nodeType == TEXT_NODE) {
        return firstNode.nodeValue;
    }
    if (node.nodeType == TEXT_NODE) {
        return node.nodeValue;
    }
    return "";
}

function getX(node) {
    var rect = node.getBoundingClientRect();
    if (window.scrollX) {
        return rect.left + window.scrollX;
    }
    return rect.left;
}

function getY(node) {
    var rect = node.getBoundingClientRect();
    if (window.scrollY) {
        return rect.top + window.scrollY;
    }
    return rect.top;
}

function getWidth(node) {
    if (node.getBoundingClientRect().width) {
        return node.getBoundingClientRect().width;
    }
    return node.clientWidth;
}

function getHeight(node) {
    if (node.getBoundingClientRect().height) {
        return node.getBoundingClientRect().height;
    }
    return node.clientHeight;
}

function addCoordinates(extractedAttributes, node) {
    // these attributes need special treatment
    extractedAttributes["absolute-x"] = getX(node);
    extractedAttributes["absolute-y"] = getY(node);
    extractedAttributes["absolute-width"] = getWidth(node);
    extractedAttributes["absolute-height"] = getHeight(node);
    if (typeof node.parentNode.getBoundingClientRect === "function") {
        extractedAttributes["x"] = getX(node) - getX(node.parentNode);
        extractedAttributes["y"] = getY(node) - getY(node.parentNode);
        extractedAttributes["width"] = getWidth(node) - getWidth(node.parentNode);
        extractedAttributes["height"] = getHeight(node) - getHeight(node.parentNode);
    } else {
        extractedAttributes["x"] = getX(node);
        extractedAttributes["y"] = getY(node);
        extractedAttributes["width"] = getWidth(node);
        extractedAttributes["height"] = getHeight(node);
    }
}

// Disabled should only be added for matching nodes
function isDisabled(node) {
    if (!node.disabled) {
        return false;
    }
    if (node.disabled === "") {
        return false;
    }
    if (node.disabled === "disabled") {
        return true;
    }
    return node.disabled ? true : false;
}

function isClickable(node) {
	const slack = 2;
    // is element visible by styles
    var styles = getComputedStyleSafely(node);
    if (!(styles.visibility !== 'hidden' && styles.display !== 'none')) {
        return false;
    }

    if (typeof node.getBoundingClientRect === "function") {
	    // is the element behind another element
	    var boundingRect = node.getBoundingClientRect();
	
	    // adjust coordinates to get more accurate results
	    var left = boundingRect.left + slack;
	    var right = boundingRect.right - slack;
	    var top = boundingRect.top + slack;
	    var bottom = boundingRect.bottom - slack;
	
	    var topLeft = document.elementFromPoint(left, top);
	    var topRight = document.elementFromPoint(right, top);
	    var bottomLeft = document.elementFromPoint(left, bottom);
	    var bottomRight = document.elementFromPoint(right, bottom);
	    while (!node.contains(topLeft) || !node.contains(topRight) ||
	    	!node.contains(bottomLeft) || !node.contains(bottomRight)) {
	        if (!node.contains(topLeft)) {
	        	left = topLeft.getBoundingClientRect().left + slack;
	        	top = topLeft.getBoundingClientRect().top + slack;
	        	if (left > right && top < bottom) {
	        		return false;
	        	}
	        	topLeft = document.elementFromPoint(left, top);
	        	continue;
	        }
	        if (!node.contains(topRight)) {
	        	right = topRight.getBoundingClientRect().right - slack;
	        	top = topRight.getBoundingClientRect().top + slack;
	        	if (left > right && top < bottom) {
	        		return false;
	        	}
	        	topRight = document.elementFromPoint(right, top);
	        	continue;
	        }
	        if (!node.contains(bottomLeft)) {
	        	left = bottomLeft.getBoundingClientRect().left + slack;
	        	bottom = bottomLeft.getBoundingClientRect().bottom - slack;
	        	if (left > right && top < bottom) {
	        		return false;
	        	}
	        	bottomLeft = document.elementFromPoint(left, bottom);
	        	continue;
	        }
	        if (!node.contains(bottomRight)) {
	        	right = bottomRight.getBoundingClientRect().right - slack;
	        	bottom = bottomRight.getBoundingClientRect().bottom - slack;
	        	if (left > right && top < bottom) {
	        		return false;
	        	}
	        	bottomRight = document.elementFromPoint(right, bottom);
	        	continue;
	        }
	    }
    }

    return true;
}

//extract *given* CSS style attributes
function getComputedStyleSafely(node) {
    try {
        return window.getComputedStyle(node) || [];
    } catch (err) {}
    return [];
}

function transform(node) {
    var extractedAttributes = {
        "tagName": node.tagName.toLowerCase(),
        "text": getText(node),
        "value": node.value,
        "tabindex": node.tabIndex,
        "shown": isShown(node),
        "isClickable": isClickable(node)
    };
    
    if (node.nodeType == TEXT_NODE) {
        addCoordinates(extractedAttributes, node.parentNode);
        return extractedAttributes;
    }

    // extract *all* HTML element attributes
    var attrs = node.attributes;
    for (var i = 0; i < attrs.length; i++) {
        var attributeName = attrs[i].name;
        var attributeValue = attrs[i].value;
        if (attributeValue && attributeValue != "" && attributeValue != "null") { 
            extractedAttributes[attributeName] = attributeValue;
        }
    }
    
    // overwrite empty attributes (e.g. 'disabled')
    extractedAttributes["checked"] = node.checked;
    extractedAttributes["disabled"] = isDisabled(node);
    extractedAttributes["read-only"] = node.readOnly;

    var style = getComputedStyleSafely(node);
    var parentStyle = getComputedStyleSafely(node.parentNode);
    for (var i = 0; i < cssAttributes.length; i++) {
        var attrName = cssAttributes[i];
        if (!extractedAttributes[attrName]) {
            if (parentStyle[attrName] != style[attrName]) {
                extractedAttributes[attrName] = style[attrName];
            }
        }
    }

    addCoordinates(extractedAttributes, node);

    return extractedAttributes;
}

function isShown(e) {
    if (e.nodeType == TEXT_NODE) {
        return isShown(e.parentNode);
    }
    return !!(e.offsetWidth || e.offsetHeight || e.getClientRects().length);
}

function isNonEmptyTextNode(node) {
    var nodeValue = (node.nodeValue == null) ? "" : node.nodeValue;
    return node.nodeType == TEXT_NODE && nodeValue.trim().length > 0;
}

function containsOtherElements(element) {
    return element.children.length > 0;
}

function getElementXPath(node) {
    var paths = [];
    for ( ; node && node.nodeType == ELEMENT_NODE; node = node.parentNode)  {
        var index = 0;
        for (var sibling = node.previousSibling; sibling; sibling = sibling.previousSibling) {
            if (sibling.nodeType == DOCUMENT_TYPE_NODE) {
                continue;
            }

            if (sibling.nodeName == node.nodeName) {
                ++index;
            }
        }
        var tagName = node.nodeName.toLowerCase();
        var pathIndex = "[" + (index+1) + "]";
        paths.unshift(tagName + pathIndex);
    }

    return paths.length ? "/" + paths.join( "/") : null;
}

function mapElement(parent, parentPath, allElements) {
    if (!parent || !parent.children) {
        return allElements;
    }
    var counter = new Counter();
    for (var i = 0; i < parent.childNodes.length; i++) {
        var child = parent.childNodes[i];
        if (child.nodeType == ELEMENT_NODE ||
            (isNonEmptyTextNode(child) && containsOtherElements(parent))) {
            if (child.nodeType == TEXT_NODE) {
                child.tagName = "textnode";
            }
            var cnt = counter.increase(child);
            var path = parentPath + "/" + child.tagName.toLowerCase() + "[" + cnt + "]";
            allElements[path] = transform(child);
            mapElement(child, path, allElements);
        }
    }
    return allElements;
}

var rootNode = document.getElementsByTagName("html")[0];
var rootPath = "//html[1]";
if (arguments.length >= 1 && arguments[0]) {
    rootNode = arguments[0];
    rootPath = getElementXPath(rootNode);
}
var root = transform(rootNode);
var allElements = {};
allElements[rootPath] = root;
allElements = mapElement(rootNode, rootPath, allElements);
return allElements;

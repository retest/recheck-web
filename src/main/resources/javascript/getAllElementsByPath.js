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

var pseudoElements = [
	"::before",
	"::after",
	"::first-line",
	"::first-letter"
];

var defaultPseudoElementValues = [];
defaultPseudoElementValues['align-items'] = ['normal']; //?RecheckRemoteWebElementIT
defaultPseudoElementValues['animation-delay'] = ['0s'];
defaultPseudoElementValues['animation-direction'] = ['normal'];
defaultPseudoElementValues['animation-duration'] = ['0'];
defaultPseudoElementValues['animation-fill-mode'] = ['none'];
defaultPseudoElementValues['animation-iteration-count'] = ['1'];
defaultPseudoElementValues['animation-name'] = ['none'];
defaultPseudoElementValues['animation-play-state'] = ['running'];
defaultPseudoElementValues['animation-timing-function'] = ['ease'];
defaultPseudoElementValues['background-color'] = ['rgba(0, 0, 0, 0)'];
defaultPseudoElementValues['background-image'] = ['none'];
defaultPseudoElementValues['background-origin'] = ['padding-box'];
defaultPseudoElementValues['background-position'] = ['0% 0%'];
defaultPseudoElementValues['background-repeat'] = ['repeat'];
defaultPseudoElementValues['background-size'] = ['auto'];
defaultPseudoElementValues['bottom'] = ['auto'];
defaultPseudoElementValues['border-bottom-color'] = ['rgb(0, 0, 0)', 'rgba(0, 0, 0, 0)'];
defaultPseudoElementValues['border-bottom-left-radius'] = ['0px'];
defaultPseudoElementValues['border-bottom-right-radius'] = ['0px'];
defaultPseudoElementValues['border-bottom-style'] = ['none'];
defaultPseudoElementValues['border-bottom-width'] = ['0px'];
defaultPseudoElementValues['border-left-color'] = ['rgb(0, 0, 0)', 'rgba(0, 0, 0, 0)'];
defaultPseudoElementValues['border-left-style'] = ['none'];
defaultPseudoElementValues['border-left-width'] = ['0px'];
defaultPseudoElementValues['border-radius'] = ['0px'];
defaultPseudoElementValues['border-right-color'] = ['rgb(0, 0, 0)', 'rgba(0, 0, 0, 0)'];
defaultPseudoElementValues['border-right-style'] = ['none'];
defaultPseudoElementValues['border-right-width'] = ['0px'];
defaultPseudoElementValues['border-top-color'] = ['rgb(0, 0, 0)', 'rgba(0, 0, 0, 0)'];
defaultPseudoElementValues['border-top-left-radius'] = ['0px'];
defaultPseudoElementValues['border-top-right-radius'] = ['0px'];
defaultPseudoElementValues['border-top-style'] = ['none'];
defaultPseudoElementValues['border-top-width'] = ['0px'];
defaultPseudoElementValues['box-shadow'] = ['none'];
defaultPseudoElementValues['box-sizing'] = ['content-box'];
defaultPseudoElementValues['break-inside'] = ['auto'];
defaultPseudoElementValues['content'] = ['none', 'normal']; // For -moz-alt-content see https://bugzilla.mozilla.org/show_bug.cgi?id=10280
defaultPseudoElementValues['counter-reset'] = ['none'];
defaultPseudoElementValues['display'] = ['inline'];
defaultPseudoElementValues['flex-direction'] = ['row']; //?RecheckRemoteWebElementIT - html[1]/body[1]/form[4]/textarea[2]
defaultPseudoElementValues['flex-shrink'] = ['1'];
defaultPseudoElementValues['flex-wrap'] = ['nowrap'];
defaultPseudoElementValues['float'] = ['none'];
defaultPseudoElementValues['justify-content'] = ['normal']; // not allowed but appears in chrome
defaultPseudoElementValues['left'] = ['auto'];
defaultPseudoElementValues['margin-bottom'] = ['0px'];
defaultPseudoElementValues['margin-left'] = ['0px'];
defaultPseudoElementValues['margin-right'] = ['0px'];
defaultPseudoElementValues['margin-top'] = ['0px'];
defaultPseudoElementValues['max-height'] = ['none'];
defaultPseudoElementValues['max-width'] = ['none'];
defaultPseudoElementValues['min-height'] = ['0px'];
defaultPseudoElementValues['min-width'] = ['0px'];
defaultPseudoElementValues['opacity'] = ['1'];
defaultPseudoElementValues['outline-color'] = ['rgb(0, 0, 0)'];
defaultPseudoElementValues['outline-offset'] = ['0px'];
defaultPseudoElementValues['outline-style'] = ['none'];
defaultPseudoElementValues['outline-width'] = ['0px'];
defaultPseudoElementValues['overflow-x'] = ['visible'];
defaultPseudoElementValues['overflow-y'] = ['visible'];
defaultPseudoElementValues['padding-bottom'] = ['0px'];
defaultPseudoElementValues['padding-left'] = ['0px'];
defaultPseudoElementValues['padding-right'] = ['0px'];
defaultPseudoElementValues['padding-top'] = ['0px'];
defaultPseudoElementValues['page-break-inside'] = ['auto'];
defaultPseudoElementValues['position'] = ['static'];
defaultPseudoElementValues['pseudo'] = ['true'];
defaultPseudoElementValues['resize'] = ['none']; //?RecheckRemoteWebElementIT - html[1]/body[1]/form[4]/textarea[2]
defaultPseudoElementValues['right'] = ['auto'];
defaultPseudoElementValues['table-layout'] = ['auto'];
defaultPseudoElementValues['text-overflow'] = ['clip'];
defaultPseudoElementValues['text-decoration-line'] = ['none'];
defaultPseudoElementValues['top'] = ['auto'];
defaultPseudoElementValues['transform'] = ['none'];
defaultPseudoElementValues['transition-delay'] = ['0s'];
defaultPseudoElementValues['transition-duration'] = ['0s'];
defaultPseudoElementValues['transition-property'] = ['all'];
defaultPseudoElementValues['transition-timing'] = ['ease'];
defaultPseudoElementValues['transition-timing-function'] = ['ease'];
defaultPseudoElementValues['unicode-bidi'] = ['normal']; //invisible on 2 Elements at SimplePageIT - Firefox
defaultPseudoElementValues['user-select'] = ['auto'];
defaultPseudoElementValues['vertical-align'] = ['baseline'];
defaultPseudoElementValues['z-index'] = ['auto'];

// Firefox fixes
var imgDefaultPseudoElementValues = [];
imgDefaultPseudoElementValues['content'] = ['none', '-moz-alt-content']; // see https://bugzilla.mozilla.org/show_bug.cgi?id=10280
imgDefaultPseudoElementValues['unicode-bidi'] = ['isolate']; // https://developer.mozilla.org/en-US/docs/Web/CSS/unicode-bidi

// Default values for disabled pseudo elements at disabled nodes
var disabledPseudoElementValues = [];
disabledPseudoElementValues['border-bottom-color'] = ['rgb(109, 109, 109)'];
disabledPseudoElementValues['border-left-color'] = ['rgb(109, 109, 109)'];
disabledPseudoElementValues['border-right-color'] = ['rgb(109, 109, 109)'];
disabledPseudoElementValues['border-top-color'] = ['rgb(109, 109, 109)'];

var ELEMENT_NODE = 1;
var TEXT_NODE = 3;
var DOCUMENT_TYPE_NODE = 10;
const BOUNDING_PRECISION = 1;

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

function hasAutofocus(node) {
    if (!node.autofocus) {
        return false;
    }
    if (node.autofocus === "") {
        return false;
    }
    if (node.autofocus === "autofocus") {
        return true;
    }
    return node.autofocus ? true : false;
}

// check if element is behind another one
function isCovered(node) {
    // TODO Handle false negatives for elements outside of viewport
    if (typeof node.getBoundingClientRect === "function") {
        var boundingRect = node.getBoundingClientRect();

        var top = document.elementFromPoint(boundingRect.left + (boundingRect.width / 2), boundingRect.top + BOUNDING_PRECISION);
        var bottom = document.elementFromPoint(boundingRect.left + (boundingRect.width / 2), boundingRect.bottom - BOUNDING_PRECISION);
        var left = document.elementFromPoint(boundingRect.left + BOUNDING_PRECISION, boundingRect.top + (boundingRect.height / 2));
        var right = document.elementFromPoint(boundingRect.right - BOUNDING_PRECISION, boundingRect.top + (boundingRect.height / 2));

        if ((top != null && !node.contains(top))
            && (bottom != null && !node.contains(bottom))
            && (left != null && !node.contains(left))
            && (right != null && !node.contains(right))) {
            return true;
        }
    }

    return false;
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
        "covered": isCovered(node)
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
    extractedAttributes["autofocus"] = hasAutofocus(node);
    extractedAttributes["read-only"] = node.readOnly;

    var style = getComputedStyleSafely(node);
    var parentStyle = [];
    if (node !== rootNode) {
    	parentStyle = getComputedStyleSafely(node.parentNode);
    }
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

//extract *given* CSS style attributes for pseudo elements
function getComputedPseudoStyleSafely(node, pseudo) {
  try {
      return window.getComputedStyle(node, pseudo) || [];
  } catch (err) {}
  return [];
}

function addPseudoElements(node, nodePath, allElements) {
	for (pseudo of pseudoElements) {
		try {
			var parentStyle = getComputedStyleSafely(node);
			var style = getComputedPseudoStyleSafely(node, pseudo);
			if (!style || !parentStyle) {
				continue;
            }
            if (pseudo === "::before" || pseudo === "::after") {
                // TODO: This is w3c standard, but what do the different browsers implement (null, "")?
                if (style["content"] === "none") {
                    continue;
                }
            }
			var path = nodePath + "/#pseudo" + pseudo + "[1]";
			var extractedAttributes = {
					"pseudo": allElements.find(elem => elem[0] === nodePath)[1].shown,
					"tagName": pseudo,
				};
			for (attributeName of cssAttributes) {
				var defaultValues = selectDefaultValueSet(node, attributeName);
				if (!extractedAttributes[attributeName]) {
					if (style[attributeName]) {
						if (!Object.keys(defaultValues).includes(attributeName) ||
						Object.keys(defaultValues).includes(attributeName) && !defaultValues[attributeName].includes(style[attributeName])
						) {
							if (parentStyle[attributeName] != style[attributeName]) {
								extractedAttributes[attributeName] = style[attributeName];
							}
						}
					}
				}
			}
			if (Object.keys(extractedAttributes).length > 2) {
				allElements.push([path, extractedAttributes]);
			}
		} catch (err) {}
	}
}

function selectDefaultValueSet(node, attributeName) {
	if (isDisabled(node)) {
		if (Objects.keys(disabledPseudoElementValues).includes(attributeName)) {
			return disabledPseudoElementValues;
		}
	}
	if (('img' == node.tagName.toLowerCase() || 'input' == node.tagName.toLowerCase())
			&& Object.keys(imgDefaultPseudoElementValues).includes(attributeName)) {
		return imgDefaultPseudoElementValues;
	}
	return defaultPseudoElementValues;
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
            allElements.push([path, transform(child)]);
            addPseudoElements(child, path, allElements);
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
var allElements = [];
allElements.push([rootPath, root]);
allElements = mapElement(rootNode, rootPath, allElements);
return allElements;

cssAttributes = arguments[0];

function Counter() {
	this.map = {};
	this.increase = function (element) {
		if (element.tagName in this.map) {
			this.map[element.tagName] = this.map[element.tagName] + 1;
		} else {
			this.map[element.tagName] = 1;
		}
		return this.map[element.tagName];
	};
}

function getText(node) {
	var firstNode = node.childNodes[0];
	if (firstNode && firstNode.nodeType == node.TEXT_NODE) {
		return firstNode.nodeValue;
	}
	if (node.nodeType == node.TEXT_NODE) {
		return node.nodeValue;
	}
	return "";
}

function getX(node) {
	var rect = node.getBoundingClientRect();
	return rect.left + window.scrollX;
}

function getY(node) {
	var rect = node.getBoundingClientRect();
	return rect.top + window.scrollY;
}

function addCoordinates(extractedAttributes, node) {
	// these attributes need special treatment
	extractedAttributes["absolute-x"] = getX(node);
	extractedAttributes["absolute-y"] = getY(node);
	extractedAttributes["absolute-width"] = node.getBoundingClientRect().width;
	extractedAttributes["absolute-height"] = node.getBoundingClientRect().height;
	if (typeof node.parentNode.getBoundingClientRect === "function") {
		extractedAttributes["x"] = getX(node) - getX(node.parentNode);
		extractedAttributes["y"] = getY(node) - getY(node.parentNode);
		extractedAttributes["width"] = node.getBoundingClientRect().width - node.parentNode.getBoundingClientRect().width;
		extractedAttributes["height"] = node.getBoundingClientRect().height - node.parentNode.getBoundingClientRect().height;
	} else {
		extractedAttributes["x"] = getX(node);
		extractedAttributes["y"] = getY(node);
		extractedAttributes["width"] = node.getBoundingClientRect().width;
		extractedAttributes["height"] = node.getBoundingClientRect().height;
	}
}

function transform(node) {
	var extractedAttributes = {
		"tagName": node.tagName.toLowerCase(),
		"text": getText(node),
		"value": node.value,
		"tab-index": node.tabIndex,
		"disabled": node.disabled === "" ? "false" : node.disabled,
		"read-only": node.readOnly,
		"shown": isShown(node)
	};
	
	if (node.nodeType == node.TEXT_NODE) {
		addCoordinates(extractedAttributes, node.parentNode);
		return extractedAttributes;
	}

	// extract *all* HTML element attributes
	var attrs = node.attributes;
	for (var i = 0; i < attrs.length; i++) {
		var attributeName = attrs[i].name;
		var attributeValue = attrs[i].value;
		extractedAttributes[attributeName] = attributeValue;
	}

	// extract *given* CSS style attributes
	var style = window.getComputedStyle(node);
	var parentStyle = [];
	try {
		parentStyle = window.getComputedStyle(node.parentNode);
	} catch (err) {}
	for (var i = 0; i < cssAttributes.length; i++) {
		var attributeName = cssAttributes[i];
		if (!extractedAttributes[attributeName]) {
			if (parentStyle[attributeName] != style[attributeName]) {
				extractedAttributes[attributeName] = style[attributeName];
			}
		}
	}

	addCoordinates(extractedAttributes, node);

	return extractedAttributes;
}

function isShown(e) {
	if (e.nodeType == e.TEXT_NODE) {
		return isShown(e.parentNode);
	}
	return !!(e.offsetWidth || e.offsetHeight || e.getClientRects().length);
}

function isNonEmptyTextNode(node) {
	return node.nodeType == node.TEXT_NODE && node.nodeValue.trim().length > 0;
}

function containsOtherElements(element) {
	return element.children.length > 0;
}

function mapElement(element, parentPath, allElements) {
	if (!element || !element.children) {
		return allElements;
	}
	var counter = new Counter();
	for (var i = 0; i < element.childNodes.length; i++) {
		var child = element.childNodes[i];
		if (child.nodeType == child.ELEMENT_NODE || 
				(isNonEmptyTextNode(child) && containsOtherElements(element))) {
			if (child.nodeType == child.TEXT_NODE) {
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

var htmlNode = document.getElementsByTagName("html")[0];
var html = transform(htmlNode);
var allElements = mapElement(htmlNode, "//html[1]", {
	"//html[1]": html
});
return allElements;

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
	// 3 is text node
	if (node.childNodes[0] && node.childNodes[0].nodeType == 3) {
		return node.childNodes[0].nodeValue;
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

function transform(node) {
	var extractedAttributes = {
		"tagName": node.tagName,
		"text": getText(node),
		"shown": isShown(node)
	};

	// extract *all* HTML element attributes
	var attrs = node.attributes;
	for (var i = 0; i < attrs.length; i++) {
		var attributeName = attrs[i].name;
		var attributValue = attrs[i].value;
		extractedAttributes[attributeName] = attributValue;
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

	return extractedAttributes;
}

function isShown(e) {
	return !!(e.offsetWidth || e.offsetHeight || e.getClientRects().length);
}

function mapElement(element, parentPath, allElements) {
	if (!element || !element.children) {
		return allElements;
	}
	var counter = new Counter();
	for (var i = 0; i < element.children.length; i++) {
		var child = element.children[i];
		var cnt = counter.increase(child);
		var path = parentPath + "/" + child.tagName + "[" + cnt + "]";
		allElements[path] = transform(child);
		mapElement(child, path, allElements);
	}
	return allElements;
}

var htmlNode = document.getElementsByTagName("html")[0];
var html = transform(htmlNode);
var allElements = mapElement(htmlNode, "//HTML[1]", {
	"//HTML[1]": html
});
return allElements;

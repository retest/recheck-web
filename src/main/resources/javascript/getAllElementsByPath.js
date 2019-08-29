const transform = require("./transform")

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

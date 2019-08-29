
cssAttributes = arguments[0];

function transform(node) {
	var extractedAttributes = {
		"tagName": node.tagName.toLowerCase(),
		"text": getText(node),
		"value": node.value,
		"tab-index": node.tabIndex,
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

	// overwrite empty attributes (e.g. 'disabled')
	extractedAttributes["checked"] = node.checked;
	extractedAttributes["disabled"] = isDisabled(node);
	extractedAttributes["read-only"] = node.readOnly;

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

function isShown(e) {
	if (e.nodeType == e.TEXT_NODE) {
		return isShown(e.parentNode);
	}
	return !!(e.offsetWidth || e.offsetHeight || e.getClientRects().length);
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

function getX(node) {
	var rect = node.getBoundingClientRect();
	return rect.left + window.scrollX;
}

function getY(node) {
	var rect = node.getBoundingClientRect();
	return rect.top + window.scrollY;
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
	return node.disabled;
}

return transform( arguments[1] );
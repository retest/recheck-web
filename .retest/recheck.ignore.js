var allowedPxDiff = 5;
var baseUrl = /http[s]?:\/\/[\w.:\d\-]*/;
var fontFamilies = [["system-ui", "Arial"], ["-apple-system", "sans-serif"]];

function contains(array, key) {
    for (var i = 0; i < array.length; i++) {
        if (key == array[i]) {
        	return true;
        }
    }
    return false;
}

function shouldIgnoreAttributeDifference(element, diff) {
	if (diff.key == 'outline') {
		return (Math.abs(diff.expected.x - diff.actual.x) <= allowedPxDiff) && 
			   (Math.abs(diff.expected.y - diff.actual.y) <= allowedPxDiff) && 
			   (Math.abs(diff.expected.width - diff.actual.width) <= allowedPxDiff) && 
			   (Math.abs(diff.expected.height - diff.actual.height) <= allowedPxDiff);
	}
	if (diff.expected === null) {
		diff.expected = "";
	}
	if (diff.actual === null) {
		diff.actual = "";
	}
	if (contains(['absolute-x', 'absolute-y', 'absolute-width', 'absolute-height', 'x', 'y', 'width', 'height'], diff.key)) {
		return (Math.abs(diff.expected.replace("px", "") - diff.actual.replace("px", "")) <= allowedPxDiff);
	}
	if (diff.expected.indexOf("px") !== -1) {
		return (Math.abs(diff.expected.replace("px", "") - diff.actual.replace("px", "")) <= allowedPxDiff);
	}
	if (diff.key == "font-family") {
	    for (var i = 0; i < fontFamilies.length; i++) {
	        if (contains(fontFamilies[i], diff.expected)) {
	        	return contains(fontFamilies[i], diff.actual);
	        }
	    }
	}
	if (diff.expected != null && diff.actual != null) {
		cleanExpected = diff.expected.replace(baseUrl, '');
		cleanActual = diff.actual.replace(baseUrl, '');
		return cleanExpected === cleanActual;
	}
	return false;
}

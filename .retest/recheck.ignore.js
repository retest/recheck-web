var baseUrl = /http[s]?:\/\/[\w.:\d\-]*/;
var fontFamilies = [ [ "system-ui", "Arial" ], [ "-apple-system", "sans-serif" ] ];

function contains(array, key) {
	for (var i = 0; i < array.length; i++) {
		if (key == array[i]) {
			return true;
		}
	}
	return false;
}

function matches(element, diff) {
	if (diff == null) {
		return true;
	}
	if (diff.expected == null) {
		diff.expected = "";
	}
	if (diff.actual == null) {
		diff.actual = "";
	}
	if (diff.key == "opacity") {
		return (Math.abs(diff.expected - diff.actual) <= 10);
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

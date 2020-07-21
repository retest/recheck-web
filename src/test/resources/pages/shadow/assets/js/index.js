class LayoutHTMLElement extends HTMLElement {

	constructor() {
		super();
	}

	static get observedAttributes() {
		return ['title'];
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this._update();
	}

	connectedCallback() {
		const shadow = this.attachShadow({ mode: 'open' });
		shadow.innerHTML = `
			<header>
				<link rel="stylesheet" href="assets/css/layout.css">
				<slot name="icon">
					<svg id="logo" width="1em" height="1em" fill="currentColor">
						<use href="assets/images/retest.svg#logo"/>
					</svg>
				</slot>
				<h1 id="title"></h1>
				<slot name="context"></slot>
			</header>

			<main>
				<slot name="main"></slot>
			</main>

			<footer>
				<slot name="footer"></slot>
			</footer>
		`;
		this._update();
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this._update();
	}

	_update() {
		if (this.shadowRoot) {
			const title = this.shadowRoot.getElementById("title");
			if (title) {
				title.innerHTML = this.title;
			}
		}
	}

	get title() {
		return this.getAttribute("title");
	}

	set title(title) {
		this.setAttribute("title", title);
	}
}

class CardDeckHTMLElement extends HTMLElement {

	constructor() {
		super();
	}

	connectedCallback() {
		const shadow = this.attachShadow({ mode: 'open' });
		shadow.innerHTML = `
			<link rel="stylesheet" href="assets/css/card.css">
			<div class="card-deck">
				<slot></slot>
			</div>
		`;
	}
}

class CardHTMLElement extends HTMLElement {

	constructor() {
		super();
	}

	static get observedAttributes() {
		return ['header', 'footer'];
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this._update();
	}

	connectedCallback() {
		this.attachShadow({ mode: 'open' });
		this._update();
	}

	_update() {
		if (this.shadowRoot) {
			this.shadowRoot.innerHTML = `
				<link rel="stylesheet" href="assets/css/card.css">
				<div class="card">
					<div class="card-header">
						<slot name="header">
							<div id="header" class="card-text">${this.header}</div>
						</slot>
					</div>
					<div class="card-body">
						<slot name="body"></slot>
					</div>
					${ this.footer
					? `<div class="card-footer">
							<slot name="footer">
								<div  class="text-muted small">${this.footer}</div>
							</slot>
						</div>`
					: ""
				}
				</div>
				`;
		}
	}

	get header() {
		return this.getAttribute("header");
	}

	set header(title) {
		this.setAttribute("header", title);
	}

	get footer() {
		return this.getAttribute("footer");
	}

	set footer(title) {
		this.setAttribute("footer", title);
	}
}

class CopyrightHTMLELement extends HTMLElement {

	constructor() {
		super();
	}

	static get observedAttributes() {
		return ['company', 'year'];
	}

	attributeChangedCallback(name, oldValue, newValue) {
		this._update();
	}

	connectedCallback() {
		const shadow = this.attachShadow({ mode: 'open' });
		shadow.innerHTML = `<span id="text" style="color: gray; font-size: 70%;"></span>`;
		this._update();
	}

	_update() {
		if (this.shadowRoot) {
			const text = this.shadowRoot.getElementById("text")
			if (text) {
				text.innerHTML = `&copy; ${this.year} ${this.company}`
			}
		}
	}

	get company() {
		return this.getAttribute("company");
	}

	set company(company) {
		this.setAttribute("company", company);
	}

	get year() {
		return this.getAttribute("year");
	}

	set year(year) {
		this.setAttribute("year", year);
	}
}

(function () {
	'use strict';

	// Feature detect
	if (!(window.customElements && document.body.attachShadow)) {
		return;
	}

	customElements.define('rt-layout', LayoutHTMLElement);
	customElements.define('rt-card-deck', CardDeckHTMLElement);
	customElements.define('rt-card', CardHTMLElement);
	customElements.define('rt-copyright', CopyrightHTMLELement);

	const replaceCardData = [{ header: "Card Left", footer: "Left" }, { header: "Card Middle", footer: "Middle" }, { header: "Card Right", footer: "Right" }]
	const replaceButtonClasses = [["btn-success", "btn-primary"], ["btn-warning", "btn-secondary"], ["btn-danger", "btn-success"]];

	const change = document.getElementById("change")
	change.onclick = () => {
		// Find all light dom cards
		const cards = document.querySelectorAll("rt-card");
		cards.forEach((node, i) => {
			const data = replaceCardData[i];
			node.header = data.header;
			node.footer = data.footer;
		})

		// Find light dom copyright
		const copy = document.getElementById("copyright");
		copy.year = 2021;

		// Find all light dom buttons within a card body
		const buttons = document.querySelectorAll("rt-card button");
		buttons.forEach((node, i) => {
			const buttonClasses = replaceButtonClasses[i];
			node.classList.replace(buttonClasses[0], buttonClasses[1]);
		});

		// Disable button, since change is only one way
		change.disabled = true;
	}
})();

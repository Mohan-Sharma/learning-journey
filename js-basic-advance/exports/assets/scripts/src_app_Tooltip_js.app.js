"use strict";
/*
 * ATTENTION: The "eval" devtool has been used (maybe by default in mode: "development").
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
(self["webpackChunkexports"] = self["webpackChunkexports"] || []).push([["src_app_Tooltip_js"],{

/***/ "./src/app/Component.js":
/*!******************************!*\
  !*** ./src/app/Component.js ***!
  \******************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export */ __webpack_require__.d(__webpack_exports__, {\n/* harmony export */   \"Component\": () => (/* binding */ Component)\n/* harmony export */ });\nclass Component {\n\n    constructor(hostElementId, insertBefore = false) {\n        if (hostElementId) {\n            this.hostElement = document.getElementById(hostElementId);\n        } else {\n            this.hostElement = document.body;\n        }\n        this.insertBefore = insertBefore;\n    }\n\n    detach() {\n        this.element.remove();\n    }\n\n    attach() {\n        this.hostElement.insertAdjacentElement(this.insertBefore ? 'beforebegin' : 'beforeend', this.element);\n    }\n    \n}\n\n//# sourceURL=webpack://exports/./src/app/Component.js?");

/***/ }),

/***/ "./src/app/Tooltip.js":
/*!****************************!*\
  !*** ./src/app/Tooltip.js ***!
  \****************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

eval("__webpack_require__.r(__webpack_exports__);\n/* harmony export */ __webpack_require__.d(__webpack_exports__, {\n/* harmony export */   \"Tooltip\": () => (/* binding */ Tooltip)\n/* harmony export */ });\n/* harmony import */ var _Component_js__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./Component.js */ \"./src/app/Component.js\");\n\n\nclass Tooltip extends _Component_js__WEBPACK_IMPORTED_MODULE_0__.Component {\n\n    constructor(handleTooltipCallback, hostElementId) {\n        super(hostElementId);\n        this.handleTooltipCallback = handleTooltipCallback;\n        this.create();\n    }\n\n    closeTooltip = () => {\n        this.detach();\n        this.handleTooltipCallback();\n    };\n\n    create() {\n        const tooltip = document.createElement('div');\n        tooltip.className = 'card';\n        const tooltipTmp = document.getElementById('tooltip');\n        const tooltipBody = document.importNode(tooltipTmp.content, true);\n        tooltipBody.querySelector('p').textContent = this.hostElement.dataset.extraInfo\n        tooltip.append(tooltipBody);\n        const elOLeft = this.hostElement.offsetLeft;\n        const elOTop = this.hostElement.offsetTop;\n        const eCHeight = this.hostElement.clientHeight;\n        const scrollHeight = this.hostElement.parentElement.scrollTop;\n\n        const x = elOLeft + 20 + 'px';\n        const y = elOTop + eCHeight - 10 - scrollHeight + 'px';\n        tooltip.style.position = 'absolute';\n        tooltip.style.left = x;\n        tooltip.style.top = y;\n        tooltip.addEventListener('click', this.closeTooltip); // alternatively use => function detach = () => {}\n        this.element = tooltip;\n    }\n\n}\n\n//# sourceURL=webpack://exports/./src/app/Tooltip.js?");

/***/ })

}]);
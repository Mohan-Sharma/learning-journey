export class DOMHelper {

    static cloneElement(element) {
        const clonedElement = element.cloneNode(true);
        element.replaceWith(clonedElement);
        return clonedElement;
    }

    static moveElement(elementId, destQuerySelector) {
        const element = document.getElementById(elementId);
        const destElement = document.querySelector(destQuerySelector);
        destElement.append(element);
        element.scrollIntoView({behavior: 'smooth'});
    }
}
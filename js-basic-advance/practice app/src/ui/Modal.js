export class Modal {

    constructor(contentId, fallbackMsg) {
        this.modalTemplateElem = document.getElementById('modal-template');
        this.contentTemplateElem = document.getElementById(contentId);
    }

    show() {
        if ('content' in document.createElement('template')) {
            const modalElements = document.importNode(this.modalTemplateElem.content, true);
            this.modalElement = modalElements.querySelector('.modal');
            this.backdropElement = modalElements.querySelector('.backdrop');
            const contentElement = document.importNode(this.contentTemplateElem.content, true);
            this.modalElement.appendChild(contentElement);
            document.body.insertAdjacentElement('afterbegin', this.modalElement);
            document.body.insertAdjacentElement('afterbegin', this.backdropElement);
        } else {
            alert(this.fallbackMsg);
        }
    }

    hide() {
        if (this.modalElement) {
            //document.body.removeChild(this.modalElement);
            this.modalElement.remove();
            this.modalElement = null;
        }
        if (this.backdropElement) {
            //old browsers
            //document.body.removeChild(this.backdropElement);
            this.backdropElement.remove();
            this.backdropElement = null; // eligible for gc
        }
    }
}
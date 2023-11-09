import {DOMHelper} from "../util/DOMHelper.js";
import {Project} from "./Project.js";

export class ProjectList {

    projects = [];
    constructor(projectType) {
        this.projectType = projectType;
        const projectListItems = document.querySelectorAll(`#${this.projectType}-projects li`);
        projectListItems.forEach(p => {
            this.projects.push(new Project(p.id, this.projectType, this.switchProject.bind(this)));
        });
        this.connectDraggable();
    }

    connectDraggable() {
        const list = document.querySelector(`#${this.projectType}-projects ul`);
        list.addEventListener('dragenter', event => {
            if (event.dataTransfer.types[0] == 'text/plain') {
                event.preventDefault();
                list.parentElement.classList.add('droppable');
            }
        });
        list.addEventListener('dragover', event => {
            if (event.dataTransfer.types[0] == 'text/plain') {
                event.preventDefault();
            }
        });

        list.addEventListener('dragleave', event => {
            if (event.relatedTarget.closest && event.relatedTarget.closest(`#${this.projectType}-projects ul`) !== list) {
                list.parentElement.classList.remove('droppable');
            }
        });

        list.addEventListener('drop', event => {
            event.preventDefault();
            const projectId = event.dataTransfer.getData('text/plain');
            if (this.projects.find(p => p.id === projectId)) {
                return;
            }
            document
                    .getElementById(projectId)
                    .querySelector('button:last-of-type')
                    .click();
            list.parentElement.classList.remove('droppable');
        });
    }

    setSwitchHanlderCallback(switchHandlerCallback) {
        this.switchHandlerCallback = switchHandlerCallback;
    }

    addProject(project) {
        this.projects.push(project);
        DOMHelper.moveElement(project.id,`#${this.projectType}-projects ul`);
        project.update(this.switchProject.bind(this), this.projectType);
    }

    switchProject(pId) {
        this.switchHandlerCallback(this.projects.find(p => p.id == pId));
        this.projects = this.projects.filter(p => p.id !== pId);
    }
}
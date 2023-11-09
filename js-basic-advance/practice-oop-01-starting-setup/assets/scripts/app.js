class DOMHelper {

    // clear event listeners
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

class Component {

    constructor(hostElementId, insertBefore = false) {
        if (hostElementId) {
            this.hostElement = document.getElementById(hostElementId);
        } else {
            this.hostElement = document.body;
        }
        this.insertBefore = insertBefore;
    }

    detach() {
        this.element.remove();
    }

    attach() {
        this.hostElement.insertAdjacentElement(this.insertBefore ? 'beforebegin' : 'beforeend', this.element);
    }
    
}
class Tooltip extends Component {

    constructor(handleTooltipCallback, hostElementId) {
        super(hostElementId);
        this.handleTooltipCallback = handleTooltipCallback;
        this.create();
    }

    closeTooltip = () => {
        this.detach();
        this.handleTooltipCallback();
    };

    create() {
        const tooltip = document.createElement('div');
        tooltip.className = 'card';
        const tooltipTmp = document.getElementById('tooltip');
        const tooltipBody = document.importNode(tooltipTmp.content, true);
        tooltipBody.querySelector('p').textContent = this.hostElement.dataset.extraInfo
        tooltip.append(tooltipBody);
        const elOLeft = this.hostElement.offsetLeft;
        const elOTop = this.hostElement.offsetTop;
        const eCHeight = this.hostElement.clientHeight;
        const scrollHeight = this.hostElement.parentElement.scrollTop;

        const x = elOLeft + 20 + 'px';
        const y = elOTop + eCHeight - 10 - scrollHeight + 'px';
        tooltip.style.position = 'absolute';
        tooltip.style.left = x;
        tooltip.style.top = y;
        tooltip.addEventListener('click', this.closeTooltip); // alternatively use => function detach = () => {}
        this.element = tooltip;
    }

}
class Project {

    constructor(id, projectType,switchProjectCallback) {
        this.id = id;
        this.projectType = projectType;
        this.switchProjectCallback = switchProjectCallback;
        this.connectMoreInfoBtn();
        this.connectSwitchBtn(projectType);
        this.connectDrag();
        this.hasActiveTooltip = false;
    }

    moreInfoBtnHandler() {
        if (this.hasActiveTooltip)
            return;
        const tooltip = new Tooltip(() => { this.hasActiveTooltip = false; }, this.id);
        tooltip.attach();
        this.hasActiveTooltip = true;
    }

    connectMoreInfoBtn() {
        const projectLi = document.getElementById(this.id);
        const moreInfoBtn = projectLi.querySelector('button:first-of-type');
        moreInfoBtn.addEventListener('click', this.moreInfoBtnHandler.bind(this))
    }

    connectSwitchBtn(projectType) {
        const projectLi = document.getElementById(this.id);
        let swtichBtn = projectLi.querySelector('button:last-of-type');
        swtichBtn = DOMHelper.cloneElement(swtichBtn);
        swtichBtn.textContent = projectType === ProjectType.ACTIVE ? 'Finish' : 'Activate';
        swtichBtn.addEventListener('click', this.switchProjectCallback.bind(null, this.id));
    }

    connectDrag () {
        document.getElementById(this.id).addEventListener('dragstart', event => {
            event.dataTransfer.setData('text/plain', this.id);
            event.dataTransfer.effectAllowed = 'move';
        });
        document.getElementById(this.id).addEventListener('dragend', event => {
            console.log('drag ended!', event);
        });
    }

    update(switchProjectCallback, projectType) {
        this.switchProjectCallback = switchProjectCallback;
        this.connectSwitchBtn(projectType);
    }
}

const ProjectType = {
    ACTIVE: 'active',
    FINISHED: 'finished'
} 

class ProjectList {

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

class App {
    static init() {
        const activeProjects = new ProjectList(ProjectType.ACTIVE);
        const finishedProjects = new ProjectList(ProjectType.FINISHED);
        activeProjects.setSwitchHanlderCallback(finishedProjects.addProject.bind(finishedProjects));
        finishedProjects.setSwitchHanlderCallback(activeProjects.addProject.bind(activeProjects));
    }
}

App.init();
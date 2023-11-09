import {DOMHelper} from '../util/DOMHelper.js';
import {ProjectType} from './ProjectType.js';

export class Project {

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
        import('./Tooltip.js').then(module => {
            const tooltip = new module.Tooltip(() => { this.hasActiveTooltip = false; }, this.id);
            tooltip.attach();
            this.hasActiveTooltip = true;
        });
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
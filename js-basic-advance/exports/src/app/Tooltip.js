import {Component} from "./Component.js";

export class Tooltip extends Component {

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
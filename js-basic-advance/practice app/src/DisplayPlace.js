import {MapUtil} from "./util/MapUtil";

export class DisplayPlace {

    constructor(coordinate, address) {
        this.mapDisplayDiv = document.getElementById('map');
        const header = document.querySelector('header h1');
        header.textContent = address;
        const mapUtil = new MapUtil(coordinate, address);
        mapUtil.render(this.mapDisplayDiv);
        mapUtil.mark();
    }
    
}
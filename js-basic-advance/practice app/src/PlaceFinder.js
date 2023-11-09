import {Coordinate} from "./Coordinate";
import {Modal} from "./ui/Modal";
import {MapUtil} from "./util/MapUtil";
import {LocationRetriever} from "./util/LocationRetriever";
import {SharePlace} from "./SharePlace";

export class PlaceFinder {
    constructor() {
        const findPlaceForm = document.querySelector('form');
        const locateButton = document.getElementById('locate-btn');
        this.mapDisplayDiv = document.getElementById('map');
        findPlaceForm.addEventListener('submit', this.addressHandler.bind(this));
        locateButton.addEventListener('click', this.userLocationHandler.bind(this));
    }

    async userLocationHandler() {
        if (!navigator.geolocation) {
            alert('Could not locate you unfortunately, please use modern browser!');
            return;
        }
        const modal = new Modal('loading-modal-content', 'Loading please wait!');
        modal.show();
        navigator.geolocation.getCurrentPosition(async success => {
            const latitude = success.coords.latitude;
            const longitude = success.coords.longitude;
            const coordinate = new Coordinate(latitude, longitude);
            const retriever = new LocationRetriever();
            const address = await retriever.locateAddress(coordinate);
            const mapMarker = new MapUtil(coordinate, address);
            mapMarker.render(this.mapDisplayDiv);
            mapMarker.mark();
            const shareableLink = mapMarker.shareableLink();
            const sharePlace = new SharePlace(shareableLink);
            modal.hide();
        }, error => {
            alert('Could not locate you unfortunately!');
            modal.hide();
        });
    }

    async saveLocation(address, coordinate) {
        const response = await fetch('http://localhost:3000/save-location', {
            method: 'POST',
            body: JSON.stringify({
                address: address,
                lat: coordinate.lat,
                lng: coordinate.lng
            }),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        const jsonData = await response.json();
        return jsonData.locationId;
    }

    async addressHandler(event) {
        event.preventDefault();
        const address = event.target.querySelector('input').value;
        if (!address || address.trim().length == 0) {
            alert('invalid address!');
            return;
        }
        try {
            const retriever = new LocationRetriever();
            retriever.locateCoordinate(address);
            const modal = new Modal('loading-modal-content', 'Loading please wait!');
            modal.show();
            const coordinate = await retriever.locateCoordinate(address);
            const mapMarker = new MapUtil(coordinate, address);
            mapMarker.render(this.mapDisplayDiv);
            mapMarker.mark();
            const locationId = await this.saveLocation(address, coordinate);
            const shareableLink = mapMarker.shareableLink(locationId);
            const sharePlace = new SharePlace(shareableLink);
            modal.hide();
        } catch(err) {
            modal.hide();
            alert(err.message);
        }
    }
}
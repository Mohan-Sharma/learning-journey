import {Coordinate} from './Coordinate';
import {DisplayPlace} from './DisplayPlace';

class DisplayApp {
    static init() {
        const url = new URL(location.href);
        const queryParams = url.searchParams;
        const locationId = queryParams.get('locationId');
        DisplayApp.displayPlace(locationId);
    }

    static async displayPlace(locationId) {
        const response = await fetch(`http://localhost:3000/get-location?locationId=${locationId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        const data = await response.json();
        const addressData = await data.data;
        console.log(addressData);
        const coordiinate = new Coordinate(+addressData.coordinates.lat, +addressData.coordinates.lng)
        new DisplayPlace(coordiinate, addressData.address);
    }
}

DisplayApp.init();
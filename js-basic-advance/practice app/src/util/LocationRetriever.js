import {AppConstants} from "./AppConstants"

export class LocationRetriever {

    async locateCoordinate(address) {
        const urlAddress = encodeURI(address);
        const response = await fetch(`https://maps.googleapis.com/maps/api/geocode/json?address=${urlAddress}&key=${AppConstants.getAPIKey()}`)
        if (!response.ok) {
            throw new Error('Failed to fetch co-ordinates!')
        }
        const data = await response.json();
        if (data.error_message) {
            throw new Error(data.error_message);
        }
        return data.results[0].geometry.location;
    }

    async locateAddress(coordinate) {
        const response = await fetch(`https://maps.googleapis.com/maps/api/geocode/json?latlng=${coordinate.lat},${coordinate.lng}&key=${AppConstants.getAPIKey()}`)
        if (!response.ok) {
            throw new Error('Failed to fetch address!')
        }
        const data = await response.json();
        if (data.error_message) {
            throw new Error(data.error_message);
        }
        return data.results[0].formatted_address;
    }

}
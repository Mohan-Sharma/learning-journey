export class MapUtil {
    
    constructor(coordinate, address) {
        this.coordinate = coordinate;
        this.address = address;
    }

    render(mapElement) {
        if (!google) {
            alert('Could not load map!')
            return;
        }
        this.map = new google.maps.Map(mapElement,  
            { 
                center: { 
                    lat: this.coordinate.lat, 
                    lng: this.coordinate.lng
                }, 
                zoom: 20
            }
        );
    }

    mark() {
        new google.maps.Marker({
            position: { 
                lat: this.coordinate.lat, 
                lng: this.coordinate.lng
            },
            map: this.map
        });
    }

    shareableLink(locationId) {
        return `${location.origin}/my-place?locationId=${locationId}`
    }
}
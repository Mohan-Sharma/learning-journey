import {Component} from "@angular/core";

@Component({
    selector: 'warning-alert',
    templateUrl: './warning.alert.component.html',
    styles: [
        `
            .warn-alert {
                padding: 20px;
                background-color: yellow; /* Red */
                color: black;
                text-align: center;
                margin-bottom: 15px;
            }
        `
    ]
})
export class WarningAlertComponent {

}

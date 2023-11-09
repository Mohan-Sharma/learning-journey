import {Component} from '@angular/core';
import {AccountService} from "../shared/account.service";

@Component({
    selector: 'app-new-account',
    templateUrl: './new-account.component.html',
    styleUrls: ['./new-account.component.css'],
})
export class NewAccountComponent {

    onCreateAccount(accountName: string, accountStatus: string) {
        this.accountService.addAccount(accountName, accountStatus);
    }

    constructor(private accountService: AccountService) {}
}

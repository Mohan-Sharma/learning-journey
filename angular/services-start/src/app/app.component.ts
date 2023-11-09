import {Component} from '@angular/core';
import {AccountService} from "./shared/account.service";
import {AccountModel} from "./shared/account.model";

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css'],
    providers: [AccountService]
})
export class AppComponent {
    accounts: AccountModel[] = this.accountService.accounts;
    constructor(private accountService: AccountService) {}
}

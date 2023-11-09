import {AccountModel} from "./account.model";
import {LoggingService} from "./logging.service";
import {Injectable} from "@angular/core";

@Injectable()
export class AccountService {
    private _accounts: AccountModel[] = [
        new AccountModel('Active Account', 'active'),
        new AccountModel('Inactive Account', 'inactive'),
        new AccountModel('Unknown Account', 'unknown'),
    ];

    addAccount(name: string, status: string) {
        this._accounts.push(new AccountModel(name, status));
        this.loggingService.logStatus(status);
    }

    updateStatus(index: number, status: string) {
        this._accounts[index].status = status;
        this.loggingService.logStatus(status);
    }


    get accounts(): AccountModel[] {
        return this._accounts;
    }


    constructor(private loggingService: LoggingService) {}
}

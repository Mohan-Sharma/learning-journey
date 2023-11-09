export class AccountModel {

    private _name: string;
    private _status: string;

    constructor(name: string, status: string) {
        this._name = name;
        this._status = status;
    }

    get name(): string {
        return this._name;
    }

    get status(): string {
        return this._status;
    }

    set status(value: string) {
        this._status = value;
    }
}

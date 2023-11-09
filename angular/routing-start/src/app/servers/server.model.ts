export class ServerModel {
    private _id: number;
    private _name: string;
    private _status: string;

    constructor(id: number, name: string, status: string) {
        this._id = id;
        this._name = name;
        this._status = status;
    }

    get id(): number {
        return this._id;
    }

    set id(value: number) {
        this._id = value;
    }

    get name(): string {
        return this._name;
    }

    set name(value: string) {
        this._name = value;
    }

    get status(): string {
        return this._status;
    }

    set status(value: string) {
        this._status = value;
    }
}
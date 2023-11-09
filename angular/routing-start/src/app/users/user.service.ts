import {Injectable} from "@angular/core";
import {UserModel} from "./user.model";

@Injectable()
export class UserService {
    private _users = [
        new UserModel(1,'Max'),
        new UserModel(2,'Anna'),
        new UserModel(3,'Chris')
    ]

    get users(): UserModel[] {
        return this._users.slice();
    }

    getUserById(id: number): UserModel {
        return this._users.find( (u: UserModel) => u.id == id);
    }
}
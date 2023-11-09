import {Injectable} from "@angular/core";

@Injectable()
export class AuthService {
    loggedIn: boolean = false;
    constructor() {}

    login(): void {
        this.loggedIn = true;
    }

    logout(): void {
        this.loggedIn = false;
    }

    isAuthenticated(): Promise<Boolean> {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve(this.loggedIn);
            }, 800)
        });
    }
}
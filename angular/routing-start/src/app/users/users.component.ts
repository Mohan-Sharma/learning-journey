import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {UserService} from "./user.service";
import {UserModel} from "./user.model";

@Component({
    selector: 'app-users',
    templateUrl: './users.component.html',
    styleUrls: ['./users.component.css'],
    providers: [UserService]
})
export class UsersComponent implements OnInit {

    users: UserModel[];

    constructor(private route: ActivatedRoute, private userService: UserService) {}

    ngOnInit(): void {
        this.users = this.userService.users;
    }

}

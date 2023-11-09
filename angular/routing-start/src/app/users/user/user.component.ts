import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from "@angular/router";
import {UserService} from "../user.service";

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

    user: {id: number, name: string};

    constructor(private route: ActivatedRoute, private userService: UserService) { }

    ngOnInit() {
        this.user = this.userService.getUserById(1);
        let id:number = +this.route.snapshot.params['id']
        this.route.params.subscribe((params: Params) => {
            id = params['id'];
            const userFound = this.userService.getUserById(id);
            if (userFound) {
                this.user = userFound;
            }
        });
    }

}

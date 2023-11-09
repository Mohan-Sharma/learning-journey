import {Component, OnInit} from '@angular/core';

import {ServersService} from '../servers.service';
import {ActivatedRoute, Params, Router} from "@angular/router";

@Component({
  selector: 'app-server',
  templateUrl: './server.component.html',
  styleUrls: ['./server.component.css']
})
export class ServerComponent implements OnInit {

  server: {id: number, name: string, status: string};
  editMode: boolean = false;

  constructor(private serversService: ServersService, private route: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    let id: number = +this.route.snapshot.params['id'];
    this.route.params.subscribe((params: Params) => {
      id = +params['id'];
      if (id) {
        this.server = this.serversService.getServerById(id);
        this.editMode = false;
      }
    });
  }

  onSelect() {
    this.editMode = true;
    this.router.navigate(['edit'], {relativeTo: this.route, queryParams: {id: this.server.id}});
  }
}

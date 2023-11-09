import {Component, OnInit} from '@angular/core';

import {ServersService} from '../servers.service';
import {ActivatedRoute, Params} from "@angular/router";

@Component({
  selector: 'app-edit-server',
  templateUrl: './edit-server.component.html',
  styleUrls: ['./edit-server.component.css']
})
export class EditServerComponent implements OnInit {

  server: {id: number, name: string, status: string};
  serverName = '';
  serverStatus = '';

  constructor(private serversService: ServersService, private route: ActivatedRoute) {}

  ngOnInit() {
    let id: number = +this.route.snapshot.queryParams['id'];
    this.route.queryParams.subscribe((params: Params) => {
      id = +params['id'];
      if (id) {
        this.server = this.serversService.getServerById(id);
      }
    });
  }

  onUpdateServer() {
    this.serversService.updateServer(this.server.id, {name: this.serverName, status: this.serverStatus});
  }

}

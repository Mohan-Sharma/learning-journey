import {Component, OnInit} from '@angular/core';
import {ServersService} from './servers.service';
import {ServerModel} from "./server.model";

@Component({
  selector: 'app-servers',
  templateUrl: './servers.component.html',
  styleUrls: ['./servers.component.css'],
  providers: [ServersService]
})
export class ServersComponent implements OnInit {

  public servers: ServerModel[] = [];

  constructor(private serversService: ServersService) { }

  ngOnInit() {
    this.servers = this.serversService.servers;
  }

}

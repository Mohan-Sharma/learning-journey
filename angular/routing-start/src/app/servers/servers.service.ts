import {ServerModel} from "./server.model";
import {EventEmitter, Injectable, Output} from "@angular/core";

@Injectable()
export class ServersService {

    @Output()
    serverUpdatedEvent = new EventEmitter<ServerModel[]>();

    private _servers: ServerModel[] = [
        new ServerModel(1, 'Productionserver', 'online'),
        new ServerModel(2, 'Testserver', 'offline'),
        new ServerModel(3, 'Devserver', 'offline')
    ];

    get servers(): ServerModel[] {
        return this._servers.slice();
    }

    getServerById(id: number): ServerModel {
        return this._servers.find((s: ServerModel) =>  s.id === id);
    }

    updateServer(id: number, serverInfo: {name: string, status: string}) {
        const server = this.getServerById(id);
        if (server) {
            server.name = serverInfo.name;
            server.status = serverInfo.status;
        }
        this.serverUpdatedEvent.emit(this._servers.slice())
    }
}

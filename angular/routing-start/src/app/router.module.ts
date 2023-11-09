import {NgModule} from "@angular/core";
import {RouterModule, Routes} from "@angular/router";
import {HomeComponent} from "./home/home.component";
import {UsersComponent} from "./users/users.component";
import {UserComponent} from "./users/user/user.component";
import {ServersComponent} from "./servers/servers.component";
import {ServerComponent} from "./servers/server/server.component";
import {EditServerComponent} from "./servers/edit-server/edit-server.component";
import {NotFoundComponent} from "./not-found/not-found.component";
import {AuthService} from "./shared/auth.service";
import {AuthGuardService} from "./shared/auth.guard.service";

const appRoutes: Routes = [
    { path: '', component: HomeComponent },
    { path: 'users', component: UsersComponent, children: [
            { path: ':id', component: UserComponent }
        ]
    },
    { path: 'servers', canActivateChild: [AuthGuardService], component: ServersComponent, children:[
            { path: ':id', component: ServerComponent, children: [
                    { path: 'edit', component: EditServerComponent }
                ]
            },
        ]
    },
    { path: 'not-found', component: NotFoundComponent },
    { path: '**', redirectTo: 'not-found' } // must be the last
]
@NgModule({
    imports: [RouterModule.forRoot(appRoutes)],
    exports: [RouterModule],
    providers: [AuthGuardService, AuthService]
})
export class AppRouterModule {

}
import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';

import {AppComponent} from './app.component';
import {TemplateFormComponent} from './template-form/template-form.component';
import {HeaderComponent} from './header/header.component';
import {RouterModule, Routes} from '@angular/router';
import {ReactiveFormComponent} from './reactive-form/reactive-form.component';

const appRoutes: Routes = [
  { path: '', redirectTo: '/template', pathMatch: 'full' },
  { path: 'template', component: TemplateFormComponent },
  { path: 'reactive', component: ReactiveFormComponent }
];
@NgModule({
  declarations: [AppComponent, TemplateFormComponent, HeaderComponent, ReactiveFormComponent],
  imports: [BrowserModule, FormsModule, RouterModule.forRoot(appRoutes), ReactiveFormsModule],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}

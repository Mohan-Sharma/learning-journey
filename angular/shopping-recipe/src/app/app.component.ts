import {Component} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'shopping-recipe';
  navURL: string = '';

  onUserNavigation(navURL: string) {
    if (navURL) {
      this.navURL = navURL;
    }
  }
}

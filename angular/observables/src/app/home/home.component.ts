import {Component, OnDestroy, OnInit} from '@angular/core';
import {interval, Observable, Subscription} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit, OnDestroy {

  private inbuiltIntervalSubs: Subscription;
  private customIntervalSubs: Subscription;

  constructor() { }

  ngOnDestroy(): void {
        this.inbuiltIntervalSubs.unsubscribe();
        this.customIntervalSubs.unsubscribe();
    }

  ngOnInit() {
    this.inbuiltIntervalSubs = interval(1000).subscribe(count => console.log('inbuilt: ', count));
    const customObservable = new Observable(observer => {
      const maximum = 5, minimum = 1;
      setInterval(() => {
        let randomNumber = Math.floor(Math.random() * (maximum - minimum + 1)) + minimum;
        if (randomNumber === 4) {
          observer.error('Error random number generated: ' + randomNumber);
        } else if (randomNumber == 5) {
          observer.complete();
        } else {
          observer.next(randomNumber);
        }
        }, 1000);
    });

    //intercept and modify using operators
    const interceptedObservable= customObservable.pipe(map(data => 'Random number generated: ' + data));

    this.customIntervalSubs = interceptedObservable.subscribe(
      randomNumber => console.log('custom:', randomNumber),
        error => console.log('error message:', error),
      () => console.log('Completed'));
  }

}

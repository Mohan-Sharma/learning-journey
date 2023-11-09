import {AfterViewInit, Component, ElementRef, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, AfterViewInit {
  title = 'bootstrap-learning';
  @ViewChild('form')
  formRef: ElementRef;

  ngAfterViewInit(): void {
    console.log(this.formRef)
    const form = this.formRef.nativeElement;
    form.addEventListener('submit', (e: any) => {
      if (!form.checkValidity()) {
        e.preventDefault();
      }
      form.classList.add('was-validated')
    })
  }

  ngOnInit(): void {
  }


}

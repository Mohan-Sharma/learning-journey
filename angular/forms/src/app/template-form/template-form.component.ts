import {Component, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';

@Component({
  selector: 'app-template-form',
  templateUrl: './template-form.component.html',
  styleUrls: ['./template-form.component.css'],
})
export class TemplateFormComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}

  @ViewChild('f')
  form: NgForm;

  suggestUserName() {
    const suggestedName = 'Superuser';
  }

  protected readonly onsubmit = onsubmit;
  defaultSecret: String = 'pet';
  secretReply: String;
  genders: String[] = ['male', 'female'];

  onSubmit() {
    console.log(this.form);
  }
}

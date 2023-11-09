import {Component, OnInit} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {Observable} from 'rxjs';

@Component({
    selector: 'app-reactive-form',
    templateUrl: './reactive-form.component.html',
    styleUrls: ['./reactive-form.component.css'],
})
export class ReactiveFormComponent implements OnInit {
    genders: String[] = ['male', 'female'];

    form: FormGroup;

    constructor() {}

    /*html validators dont work for reactive approach*/
    ngOnInit(): void {
        // new FormControl(defaultValue, validators, asyncValidators);
        this.form = new FormGroup({
            /* grouping of data using nested form group in template we use ngModelGroup */
            userData: new FormGroup({
                username: new FormControl(null, [
                    Validators.required,
                    this.customValidator,
                ]),
                email: new FormControl(
                    null,
                    [Validators.required, Validators.email],
                    this.asyncValidator,
                ),
            }),
            gender: new FormControl('male', Validators.required),
            hobbies: new FormArray([]),
        });

        // observable to subscribe to changes in the form group values
        this.form.valueChanges.subscribe((values) => {
            console.log(values);
        });

        this.form.statusChanges.subscribe((status) => {
            console.log(status);
        });

        this.form.setValue({
            userData: {
                username: 'Mohan',
                email:  'email@test.com'
            },
            gender: 'male',
            hobbies: []
        });

        this.form.patchValue({
            userData: {
                username: 'Mohan Sharma',
            }
        });
    }

    onSubmit() {
        console.log(this.form);
        //this.form.reset();
        this.form.reset({gender: 'female', userData: {email: 'test@test.com'}})
    }

    addHobby() {
        const hobby = new FormControl(null, Validators.required);
        (<FormArray>this.form.get('hobbies')).push(hobby);
    }

    getHobbies() {
        return (<FormArray>this.form.get('hobbies')).controls;
    }

    customValidator(control: FormControl): { [s: string]: boolean } {
        const invalidName = ['test', 'dummy'];
        if (invalidName.includes(control.value)) {
            return { forbiddenName: true };
        }
        return null; // need to return null in case of happy path mandatory
    }

    asyncValidator(control: FormControl): Promise<any> | Observable<any> {
        const promise = new Promise((resolve, reject) => {
            setTimeout(() => {
                if (control.value === 'test@email.com') {
                    resolve({ invalidEmail: true });
                } else {
                    resolve(null);
                }
            }, 2000);
        });
        return promise;
    }
}

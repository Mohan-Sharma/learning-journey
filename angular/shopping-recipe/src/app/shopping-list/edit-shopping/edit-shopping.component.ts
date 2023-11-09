import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {NgForm} from '@angular/forms';
import {ShoppingService} from "../shopping.service";
import {Subscription} from 'rxjs';

@Component({
    selector: 'app-edit-shopping',
    templateUrl: './edit-shopping.component.html',
    styleUrls: ['./edit-shopping.component.css']
})
export class EditShoppingComponent implements OnInit, OnDestroy {

    @ViewChild("f")
    form: NgForm
    minAmount: number = 10;
    private selectIngredientSubscription: Subscription;
    addUpdate: string = 'Add';

    constructor(private shoppingService: ShoppingService) {}

    ngOnDestroy(): void {
        this.selectIngredientSubscription.unsubscribe();
    }

    ngOnInit(): void {
        this.selectIngredientSubscription = this.shoppingService.selectedIngredientEvent.subscribe((index: number) => {
            const ingredient = this.shoppingService.ingredients[index];
            this.addUpdate = 'Update';
            this.form.setValue({
                'name':  ingredient.name,
                'amount': ingredient.amount
            });
        })
    }

    addIngredient() {
        if (this.form.value.name && this.form.value.amount) {
            this.shoppingService.addIngredient(this.form.value.name, +this.form.value.amount);
            this.clearIngredient();
        }
    }

    deleteIngredient() {
        if (this.form.value.name && this.form.value.amount) {
            this.shoppingService.deleteIngredient(this.form.value.name, +this.form.value.amount);
            this.clearIngredient();
        }
    }

    clearIngredient() {
        this.form.reset();
        this.addUpdate = 'Add';
    }
}

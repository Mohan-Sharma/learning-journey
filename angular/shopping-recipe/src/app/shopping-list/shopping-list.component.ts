import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {Ingredient} from "../shared/ingredient.model";
import {ShoppingService} from "./shopping.service";

@Component({
    selector: 'app-shopping-list',
    templateUrl: './shopping-list.component.html',
    styleUrls: ['./shopping-list.component.css'],
})
export class ShoppingListComponent implements OnInit, OnDestroy {

    private _ingredients: Ingredient[];
    private ingredientsSubscription: Subscription;

    constructor(private shoppingService: ShoppingService) {
    }

    ngOnDestroy(): void {
        this.ingredientsSubscription.unsubscribe();
    }

    ngOnInit(): void {
        this._ingredients = this.shoppingService.ingredients;
        this.ingredientsSubscription = this.shoppingService.ingredientsEvent.subscribe((ingredients: Ingredient[]) => {
            this._ingredients = ingredients;
        });
    }

    get ingredients(): Ingredient[] {
        return this._ingredients;
    }

    editShoppingItem(index: number) {
        this.shoppingService.selectIngredient(index);
    }
}

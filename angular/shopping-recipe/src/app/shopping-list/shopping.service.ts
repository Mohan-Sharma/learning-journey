import {Injectable} from "@angular/core";
import {Subject} from "rxjs";
import {Ingredient} from "../shared/ingredient.model";

@Injectable({providedIn: 'root'})
export class ShoppingService {


    private _ingredientsEvent = new Subject<Ingredient[]>();
    private _selectedIngredientEvent: Subject<number> = new Subject<number>();

    private _ingredients: Ingredient[] = [
        new Ingredient("apple", 10),
        new Ingredient("tomatoes", 10)
    ]

    get ingredients(): Ingredient[] {
        return this._ingredients.slice();
    }

    addIngredient(name: string, amount: number) {
        let foundIngredientIdx = this.ingredients.findIndex(ing => ing.name === name);
        if (foundIngredientIdx != -1) {
            const ingredient = this.ingredients[foundIngredientIdx];
            ingredient.amount = amount;
            this._ingredients[foundIngredientIdx] = ingredient;
        } else {
            this._ingredients.push(new Ingredient(name, amount));
        }
        this.ingredientsEvent.next(this.ingredients.slice());
    }

    addAllIngredient(ingredients: Ingredient[]) {
        this._ingredients.push(...ingredients);
        this.ingredientsEvent.next(this.ingredients.slice());
    }

    deleteIngredient(name: string, amount: number) {
        let foundIngredientIdx = this.ingredients.findIndex(ing => ing.name === name && ing.amount === amount);
        if (foundIngredientIdx != -1) {
            this._ingredients.splice(foundIngredientIdx, 1)
            this.ingredientsEvent.next(this.ingredients.slice());
        }
    }

    selectIngredient(index: number) {
        this.selectedIngredientEvent.next(index);
    }

    get ingredientsEvent(): Subject<Ingredient[]> {
        return this._ingredientsEvent;
    }

    get selectedIngredientEvent(): Subject<number>{
        return this._selectedIngredientEvent;
    }
}

import {Injectable} from "@angular/core";
import {Recipe} from "./recipe.model";
import {Ingredient} from "../shared/ingredient.model";
import {Subject} from "rxjs";
import {v4 as uuidv4} from 'uuid';

@Injectable()
export class RecipeService {

    private _selectedRecipeEvent = new Subject<Recipe>();

    private _recipes: Recipe[] = [
        new Recipe(
            uuidv4(),
            "recipe 1",
            "recipe desc",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSucJCG5qVqfSGf6c7LduBqQOJbIenzzl_toDhcwDq2lbwvmUcp5HwYRdh-3nwlb5qQPNA&usqp=CAU",
            [new Ingredient("ing1", 10), new Ingredient("ing2", 10)]),
        new Recipe(
            uuidv4(),
            "recipe 2",
            "recipe desc",
            "https://www.forksoverknives.com/uploads/new-sloppy-joe-picture-wordpress.jpg",
            [new Ingredient("ing3", 10), new Ingredient("ing4", 10)])
    ];

    selectRecipe(recipe: Recipe) {
        this._selectedRecipeEvent.next(recipe);
    }

    get recipes(): Recipe[] {
        return this._recipes.slice();
    }

    get selectedRecipeEvent(): Subject<Recipe> {
        return this._selectedRecipeEvent;
    }

    getRecipeById(id: string): Recipe {
        return this._recipes.find( (r:Recipe) => r.id === id);
    }
}

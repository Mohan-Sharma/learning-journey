import {Component, OnInit} from '@angular/core';
import {Recipe} from "../recipe.model";
import {ShoppingService} from "../../shopping-list/shopping.service";
import {ActivatedRoute, Params} from '@angular/router';
import {RecipeService} from "../recipe.service";

@Component({
  selector: 'app-recipe-detail',
  templateUrl: './recipe-detail.component.html',
  styleUrls: ['./recipe-detail.component.css']
})
export class RecipeDetailComponent implements OnInit {


    constructor(private shoppingService: ShoppingService, private route: ActivatedRoute, private recipeService: RecipeService) {}

    ngOnInit(): void {
       this.route.params.subscribe((params: Params) => {
           const id = params['id'];
           console.log(id);
           this.recipe = this.recipeService.getRecipeById(id);
       })
    }

    recipe: Recipe;

    addToShoppingList() {
        this.shoppingService.addAllIngredient(this.recipe.ingredients)
    }
}

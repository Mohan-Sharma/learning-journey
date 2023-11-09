import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {RecipeService} from "../recipe.service";
import {Recipe} from "../recipe.model";
import {ShoppingService} from "../../shopping-list/shopping.service";
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {Ingredient} from "../../shared/ingredient.model";

@Component({
  selector: 'app-recipe-edit',
  templateUrl: './recipe-edit.component.html',
  styleUrls: ['./recipe-edit.component.css']
})
export class RecipeEditComponent implements OnInit {

    recipeForm: FormGroup;
    editMode:boolean;
    constructor(private route: ActivatedRoute, private recipeService: RecipeService, private shoppingService: ShoppingService) {}

    ngOnInit(): void {
        this.route.params.subscribe((params: Params) => {
            const recipeId:string = params['id'];
            this.editMode = recipeId != null;
            this.initializeForm(this.editMode, recipeId)
        });
    }

    private initializeForm(editMode: boolean, recipeId: string) {

        let name:string = null;
        let desc:string = null;
        let imgURL:string = null;
        let ingredients:Ingredient[] = [];

        if (editMode) {
            const recipe: Recipe = this.recipeService.getRecipeById(recipeId);
            desc = recipe.name;
            desc = recipe.description;
            imgURL = recipe.imageURL;
            ingredients = recipe.ingredients;
        }
        this.recipeForm = new FormGroup({
            'name': new FormControl(desc, Validators.required),
            'desc': new FormControl(desc, [Validators.required]),
            'imgURL': new FormControl(imgURL, [Validators.required]),
            'ingredients': new FormArray([])
        });
    }

    getIngredients() {
        this.shoppingService.ingredients;
    }

    persistRecipe() {
        console.log(this.recipeForm)
    }
}

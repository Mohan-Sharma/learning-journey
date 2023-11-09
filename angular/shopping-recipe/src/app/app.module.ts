import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppComponent} from './app.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HeaderComponent} from './header/header.component';
import {ShoppingListComponent} from './shopping-list/shopping-list.component';
import {EditShoppingComponent} from './shopping-list/edit-shopping/edit-shopping.component';
import {RecipesComponent} from './recipes/recipes.component';
import {RecipeDetailComponent} from './recipes/recipe-detail/recipe-detail.component';
import {RecipeListComponent} from './recipes/recipe-list/recipe-list.component';
import {RecipeItemComponent} from './recipes/recipe-item/recipe-item.component';
import {DropdownDirective} from './shared/dropdown.directive';
import {AppRoutingModule} from "./app.routing.module";
import {SelectRecipeComponent} from "./recipes/select-recipe/select-recipe.component";
import {RecipeEditComponent} from './recipes/recipe-edit/recipe-edit.component';
import {FooterComponent} from "./footer/footer.component";

@NgModule({
    declarations: [
        AppComponent,
        HeaderComponent,
        ShoppingListComponent,
        EditShoppingComponent,
        RecipesComponent,
        SelectRecipeComponent,
        RecipeDetailComponent,
        RecipeListComponent,
        RecipeItemComponent,
        DropdownDirective,
        RecipeEditComponent,
        FooterComponent
    ],
    imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        AppRoutingModule
    ],
    providers: [],
    bootstrap: [AppComponent]
})
export class AppModule { }

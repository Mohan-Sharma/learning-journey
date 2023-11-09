import {Ingredient} from "../shared/ingredient.model";

export class Recipe {
    private _id: string;
    private _name: string;
    private _description: string;
    private _imageURL: string;
    private _ingredients: Ingredient[];

    constructor(id: string, name: string, description: string, imageURL: string, ingredients: Ingredient[]) {
        this._id = id;
        this._name = name;
        this._description = description;
        this._imageURL = imageURL;
        this._ingredients = ingredients;
    }

    public get description(): string {
        return this._description;
    }

    public set description(value: string) {
        this._description = value;
    }

    public get name(): string {
        return this._name;
    }

    public set name(value: string) {
        this._name = value;
    }

    public get imageURL(): string {
        return this._imageURL;
    }

    public set imageURL(value: string) {
        this._imageURL = value;
    }

    get ingredients(): Ingredient[] {
        return this._ingredients;
    }

    get id(): string {
        return this._id;
    }

    set id(value: string) {
        this._id = value;
    }
}

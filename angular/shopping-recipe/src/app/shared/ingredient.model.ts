export class Ingredient {

  private _name: string;
  private _amount: number;

  constructor(name: string, amount: number) {
    this._name = name;
    this._amount = amount;
  }

  get name(): string {
    return this._name;
  }

  set name(value: string) {
    this._name = value;
  }

  get amount(): number {
    return this._amount;
  }

  set amount(value: number) {
    this._amount = value;
  }
}

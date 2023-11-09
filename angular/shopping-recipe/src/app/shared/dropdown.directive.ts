import {Directive, ElementRef, HostListener, Renderer2} from '@angular/core';

@Directive({
    selector: '[appDropdown]'
})
export class DropdownDirective {

    @HostListener('mouseover', ['$event.target'])
    showDropdown(mouseClickEvent: Event) {
        const dropdownUL = this.getDropdownUL();
        const shown = dropdownUL.classList.contains('show');
        if (!shown) {
            this.renderer.addClass(dropdownUL, 'show');
        }
    }

    @HostListener('mouseleave', ['$event.target'])
    hideDropdown(mouseClickEvent: Event) {
        const dropdownUL = this.getDropdownUL();
        const shown = dropdownUL.classList.contains('show');
        if (shown) {
            this.renderer.removeClass(dropdownUL, 'show');
        }
    }

    private getDropdownUL() {
        const element = this.elementRef.nativeElement;
        return element.querySelector("ul.dropdown-menu");
    }

    constructor(private renderer: Renderer2, private elementRef: ElementRef) {}

}

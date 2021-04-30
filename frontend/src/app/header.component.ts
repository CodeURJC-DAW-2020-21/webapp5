import { Component } from '@angular/core';

@Component({
    selector: 'header',
    templateUrl: './header.component.html' 
})

export class HeaderComponent {

    logged:boolean = false; 
    token:boolean = false;
    userName: string = '' ;


}
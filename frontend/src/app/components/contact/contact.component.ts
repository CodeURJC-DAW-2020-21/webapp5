import { Component } from "@angular/core";
import { Router } from "@angular/router";

@Component({
    selector: 'contact',
    templateUrl: './contact.component.html'
})

export class ContactComponent {
    constructor(private router: Router){
    }
}
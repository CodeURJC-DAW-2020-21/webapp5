import { Component } from "@angular/core";
import { Router } from "@angular/router";

@Component({
    selector: 'app-index',
    templateUrl: './index.component.html'
})

export class IndexComponent {
    active=1;
    constructor(private router: Router){
    }
}
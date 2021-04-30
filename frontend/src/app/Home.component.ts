import  { Component } from '@angular/core';
import { Team } from './model/Team.model';


@Component({
    selector : 'home',
    templateUrl: './Home.component.html'
})

export class HomeComponent {

    settingsError?: boolean ;
    logged?: boolean ;


}
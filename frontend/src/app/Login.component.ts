import  { Component } from '@angular/core';
import { Team } from './model/Team.model';


@Component({
    selector : 'login',
    templateUrl: './Login.component.html'
})

export class LoginComponent {

    teams: Team[] = [];
    token: boolean = false; 
    loginError: boolean = false; 

}
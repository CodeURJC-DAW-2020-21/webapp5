import  { Component } from '@angular/core';
import { Team } from './model/Team.model';
import { User } from './model/LgUser.model';


@Component({
    selector : 'settings',
    templateUrl: './Settings.component.html'
})

export class SettingsComponent {

    teams: Team[] = [];
    user?: User; 
    token?: boolean; 

}
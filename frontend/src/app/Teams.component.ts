import  { Component } from '@angular/core';
import { Team } from './model/Team.model'


@Component({
    selector : 'teams',
    templateUrl: './Teams.component.html'
})

export class TeamsComponent {

    teams: Team[] = [];

}
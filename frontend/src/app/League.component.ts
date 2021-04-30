import  { Component } from '@angular/core';
import { Team } from './model/Team.model';


@Component({
    selector : 'league',
    templateUrl: './League.component.html'
})

export class LeagueComponent {

    teams: Team[] = [];

}
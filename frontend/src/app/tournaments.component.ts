import  { Component } from '@angular/core';
import { Tournament } from './model/Tournament.model';


@Component({
    selector : 'tournaments',
    templateUrl: './tournaments.componenent.html'
})

export class TournamentsComponent {
    joinError?: boolean;

    tournaments: Tournament[] = [];

    logged?: boolean;

}
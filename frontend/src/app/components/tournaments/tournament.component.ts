import { Component, OnInit } from "@angular/core";
import { TournamentService } from '../../services/tournaments.service';
import { Tournament } from '../../models/tournament.model';
import { Router } from "@angular/router";

@Component({
    selector: 'tournaments',
    templateUrl: './tournament.component.html'
})

export class TournamentsComponent implements OnInit{

    private tournaments: Tournament[] = [];

    constructor(private router: Router, private tournamentService: TournamentService){}

    ngOnInit(): void {
        this.tournamentService.getTournaments().subscribe(
          data => {
            this.tournaments = data;
            console.log('Tournaments: ', data);
          },
          error => console.error(error)
        );
      }

      getTournaments() {
        return this.tournaments;
      }
}
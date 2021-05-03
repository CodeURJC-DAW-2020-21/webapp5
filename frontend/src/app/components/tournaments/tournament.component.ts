import { Component, OnInit } from "@angular/core";
import { TournamentService } from './tournaments.service';
import { Tournament } from './tournament.model';

@Component({
    selector: 'tournaments',
    templateUrl: './tournament.component.html'
})

export class TournamentsComponent implements OnInit{

    private tournaments: Tournament[] = [];

    constructor(private tournamentService: TournamentService){}

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
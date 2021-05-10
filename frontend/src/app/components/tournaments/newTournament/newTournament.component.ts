import { Component } from "@angular/core";
import { Router } from "@angular/router";

import { TournamentService } from "../../../services/tournaments.service";
import { Tournament } from "../../../models/tournament.model";

@Component({
    selector: 'newTournament',
    templateUrl: './newTournament.component.html'
})

export class NewTournamentComponent{

    tournament: Tournament;
    error: boolean;
    errorText: string;

    constructor(private router: Router, private tournamentService: TournamentService){
        this.tournament={
            name: '',
            description: '',
            currentPlayers: 0,
            maxPlayers: 0,
            iniDate: '',
            endDate: '',
            participants: null,
            winner: null,
            rounds: null,
            game: null,
            roundNumber: 0,
            started: false,
            finished: false,
            admin: null,
            checked: false
        }
    }
    newTournament(){
        this.tournamentService.newTournament(this.tournament).subscribe(
            (tournament:Tournament) => this.router.navigate(['/tournaments']),
            error => {
                this.error = true;
                this.errorText = "Data Error, Invalid Tournament Name";
                window.scrollTo(0, 0);
            });
     }
}
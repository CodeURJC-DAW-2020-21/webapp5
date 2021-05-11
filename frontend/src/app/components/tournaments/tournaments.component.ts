import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { Tournament } from "src/app/models/tournament.model";
import { LoginService } from "src/app/services/login.sevice";
import { TournamentService } from "src/app/services/tournaments.service";

@Component({
    selector: 'tournaments',
    templateUrl: './tournaments.component.html'
})

export class TournamentsComponent{
    
    constructor(private router: Router, public loginService: LoginService, private tournamentService: TournamentService){
    }

      goToNewTournament(){
        this.router.navigate(['/newTournament'])
      }

      goToLogin(){this.router.navigate(['/login']);}

      join(tournament: Tournament){
        this.tournamentService.joinTournament(tournament).subscribe(
          _ => location.reload(),
          error => console.error(error)
        );
      }
}
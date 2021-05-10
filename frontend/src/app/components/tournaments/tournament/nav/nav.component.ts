import {Component} from '@angular/core';

import { ActivatedRoute, Router } from "@angular/router";
import { Match } from 'src/app/models/match.model';
import { Team } from 'src/app/models/team.model';
import { Tournament } from "src/app/models/tournament.model";
import { LoginService } from "src/app/services/login.sevice";
import { TournamentService } from "src/app/services/tournaments.service";

@Component({
  selector: 'tNav',
  templateUrl: './nav.component.html'
})
export class TNav {
  active = 1;
  tabs = [2, 3, 4, 5, 6];

  tournament: Tournament;
  id: number;
  error: boolean;
  errorText: string;

  constructor(private router: Router, activatedRoute: ActivatedRoute, private tournamentService: TournamentService, public loginService: LoginService){
      this.id = activatedRoute.snapshot.params.id;
  }

  ngOnInit(): void {
      this.tournamentService.getTournament(this.id).subscribe(
        data => {
          this.tournament = data;
        },
        error => console.error(error)
      );
  }

  nextRound(tournament: Tournament){
    this.tournamentService.nextRound(tournament).subscribe(
      _ => location.reload(),
      error => console.error(error)
    );
  }

  submitScore(tournament: Tournament, match: Match, roundId: number){
    this.tournamentService.submitScore(tournament, match, roundId).subscribe(
      _ => location.reload(),
      error => console.error(error)
    );
  }
  join(tournament: Tournament){
    this.tournamentService.joinTournament(tournament).subscribe(
      _ => location.reload(),
      error => console.error(error)
    );
  }

  teamImage(team: Team){
    return team.image? '/api/teams/' + team.id + '/image' :  '/assets/images/sample_images/team_default.jpg';
  }
}